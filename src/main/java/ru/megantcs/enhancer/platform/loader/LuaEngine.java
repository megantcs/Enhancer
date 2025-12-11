package ru.megantcs.enhancer.platform.loader;

import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportConstructor;
import ru.megantcs.enhancer.platform.loader.api.LuaExportField;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;
import ru.megantcs.enhancer.platform.toolkit.Events.RunnableEvent;

import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static ru.megantcs.enhancer.platform.loader.LuaConverter.convertArgs;

public class LuaEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuaEngine.class);
    public static LuaEngine INSTANCE = new LuaEngine();
    private Globals environment;
    private final List<Class<?>> registeredClasses = new ArrayList<>();
    private LuaPreprocessor preprocessor;
    public final RunnableEvent onLoadedScript = new RunnableEvent();
    private final Set<LuaChunk> chunks;

    private LuaEngine() {
        environment = JsePlatform.standardGlobals();
        preprocessor = new LuaPreprocessor();
        chunks = new HashSet<>();
    }

    public void reloadEnvironment() {
        environment = JsePlatform.standardGlobals();
        chunks.clear();

        List<Class<?>> classesToReregister = new ArrayList<>(registeredClasses);
        registeredClasses.clear();

        for (Class<?> clazz : classesToReregister) {
            registerClass(clazz);
        }
    }

    public void updateChunks() {
        chunks.forEach(LuaChunk::updateContent);
    }

    public boolean loadScript(@NotNull String code, @NotNull String chunkName) {
        return loadScript(code, chunkName, null);
    }

    public boolean loadScript(@NotNull String code, @NotNull String chunkName, @Nullable Path path) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(chunkName);

        try {
            var newChunk = new LuaChunk(path, code, chunkName);
            if (!executeChunk(newChunk, false)) {
                 throw new RuntimeException("error execute chunk");
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("error load script: {}", chunkName, e);
            return false;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private boolean executeChunk(LuaChunk value) {
        return executeChunk(value, false);
    }

    private boolean executeChunk(LuaChunk value, boolean addValue) {
        try {
            if (addValue) chunks.add(value);
            value.load(environment).call();
            onLoadedScript.emit();
            LOGGER.info("execute chunk: " + value.name);
            return true;
        } catch (Exception e) {
            LOGGER.error("error execute chunk", e);
            LOGGER.error(value.getContent());
            return false;
        }
    }

    public boolean loadFile(@NotNull String fileName) {
        var path = Path.of(fileName);
        if (!Files.exists(path)) {
            try {
                Files.writeString(path, "");
                LOGGER.warn("create empty file for load: {}", fileName);
            } catch (IOException e) {
                LOGGER.warn("failed create empty file for loaded: {}", fileName);
                LOGGER.warn(e.getMessage());
                return false;
            }
        }

        try {
            String code = Files.readString(path);
            return loadScript(code, fileName, path);
        } catch (IOException e) {
            LOGGER.error("failed load script from file {}", fileName, e);
            return false;
        }
    }

    public boolean registerModule(String moduleName, Object instance) {
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(instance);

        try {
            Class<?> clazz = instance.getClass();
            LuaTable moduleTable = new LuaTable();

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                LuaExportMethod annotation = method.getAnnotation(LuaExportMethod.class);
                if (annotation != null && Modifier.isPublic(method.getModifiers())) {
                    String methodName = annotation.name().isEmpty() ? method.getName() : annotation.name();
                    moduleTable.set(methodName, LuaUtils.createMethodWrapper(method, instance, LOGGER));
                    LOGGER.info("  - method: " + methodName);
                }
            }

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                LuaExportField annotation = field.getAnnotation(LuaExportField.class);
                if (annotation != null && Modifier.isPublic(field.getModifiers())) {
                    if (annotation.read() || annotation.write()) {
                        moduleTable.set(field.getName(), new FieldWrapper(field, instance, annotation));
                        LOGGER.info("  - field: " + field.getName() +
                                " [read=" + annotation.read() +
                                ", write=" + annotation.write() + "]");
                    }
                }
            }

            environment.set(moduleName, moduleTable);
            LOGGER.info("register module: " + moduleName);
            return true;

        } catch (Exception e) {
            LOGGER.error("failed register module: {}", moduleName, e);
            return false;
        }
    }

    public LuaValue getType(String name) {
        return environment.get(LuaUtils.fixName(name));
    }

    public LuaValue getMethod(String name) {
        var type = getType(name);
        return type.isfunction() ? type : null;
    }

    public boolean registerClass(Class<?> clazz) {
        try {
            LuaExportClass annotation = clazz.getAnnotation(LuaExportClass.class);
            if (annotation == null) {
                LOGGER.error("Class {} is not annotated with @LuaExportClass", clazz.getName());
                return false;
            }

            String className = annotation.name().isEmpty() ? clazz.getSimpleName() : annotation.name();
            LuaTable classTable = createClassTable(clazz);

            environment.set(className, classTable);
            registeredClasses.add(clazz);
            LOGGER.info("Registered class: {} (Java: {})", className, clazz.getName());
            return true;

        } catch (Exception e) {
            LOGGER.error("Failed to register class: " + clazz.getName(), e);
            return false;
        }
    }

    public List<Class<?>> getRegisteredClasses() {
        return Collections.unmodifiableList(registeredClasses);
    }

    private LuaTable createClassTable(Class<?> clazz) {
        LuaTable classTable = new LuaTable();
        addConstructors(classTable, clazz);
        addStaticMethods(classTable, clazz);
        addStaticFields(classTable, clazz);
        setClassMetatable(classTable, clazz);
        return classTable;
    }

    private void addConstructors(LuaTable classTable, Class<?> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            LuaExportMethod methodAnnotation = constructor.getAnnotation(LuaExportMethod.class);
            LuaExportConstructor constrAnnotation = constructor.getAnnotation(LuaExportConstructor.class);

            if (methodAnnotation != null || constrAnnotation != null ||
                    Modifier.isPublic(constructor.getModifiers())) {

                String name = "new";
                if (methodAnnotation != null && !methodAnnotation.name().isEmpty()) {
                    name = methodAnnotation.name();
                } else if (constrAnnotation != null && !constrAnnotation.name().isEmpty()) {
                    name = constrAnnotation.name();
                }

                classTable.set(name, createConstructorWrapper(constructor));
            }
        }
    }

    private LuaValue createConstructorWrapper(Constructor<?> constructor) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                try {
                    Object[] javaArgs = convertConstructorArgs(constructor, args);
                    Object instance = constructor.newInstance(javaArgs);
                    return LuaConverter.toLua(instance);
                } catch (Exception e) {
                    LOGGER.error("Error creating instance of " + constructor.getDeclaringClass().getName(), e);
                    return LuaValue.NIL;
                }
            }
        };
    }

    private Object[] convertConstructorArgs(Constructor<?> constructor, Varargs args) {
        Parameter[] parameters = constructor.getParameters();
        Object[] javaArgs = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (i < args.narg()) {
                javaArgs[i] = LuaConverter.fromLua(args.arg(i + 1), parameters[i].getType());
            } else {
                javaArgs[i] = null;
            }
        }

        return javaArgs;
    }

    private void addStaticMethods(LuaTable classTable, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {
                LuaExportMethod annotation = method.getAnnotation(LuaExportMethod.class);
                String methodName = annotation != null && !annotation.name().isEmpty()
                        ? annotation.name() : method.getName();

                classTable.set(methodName, createStaticMethodWrapper(method, clazz));
            }
        }
    }

    private LuaValue createStaticMethodWrapper(Method method, Class<?> clazz) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                try {
                    Object[] javaArgs = convertArgs(method, args);
                    Object result = method.invoke(null, javaArgs);
                    return LuaConverter.toLua(result);
                } catch (Exception e) {
                    LOGGER.error("Error calling static method " + method.getName(), e);
                    return LuaValue.NIL;
                }
            }
        };
    }

    private void addStaticFields(LuaTable classTable, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())) {
                try {
                    classTable.set(field.getName(), LuaConverter.toLua(field.get(null)));
                } catch (Exception e) {
                    LOGGER.error("Error accessing static field " + field.getName(), e);
                }
            }
        }
    }

    private void setClassMetatable(LuaTable classTable, Class<?> clazz) {
        LuaTable metatable = new LuaTable();
        metatable.set(LuaValue.CALL, new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                try {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    if (constructor != null && Modifier.isPublic(constructor.getModifiers())) {
                        Object instance = constructor.newInstance();
                        return LuaConverter.toLua(instance);
                    }
                } catch (Exception e) {
                }
                return LuaValue.NIL;
            }
        });
        classTable.setmetatable(metatable);
    }

    public void execute(LuaValue value)
    {
        try {
            if(value == null) return;

            value.call();
        }
        catch (Exception e) {
            LOGGER.error("error execute value", e);
        }
    }

    public void execute(LuaValue value, LuaTable table)
    {
        try {
            if(value == null) return;

            value.call(table);
        }
        catch (Exception e) {
            LOGGER.error("error execute value", e);
        }
    }


    private LuaValue loadChunk(@NotNull String content, @NotNull String name, @NotNull Globals environment)
    {
        return environment.load(content, "chunk@" + name, environment);
    }

    private static class LuaChunk {
        @Nullable
        private final Path path;
        @NotNull
        private final String name;
        @NotNull
        private String content;

        public LuaChunk(@Nullable Path path, @NotNull String content, @NotNull String name) {
            this.path = path;
            this.content = content;
            this.name = name;
            updateContent();
        }

        public boolean isFile() {
            return path != null;
        }

        public void updateContent() {
            String newContent = content;
            if (isFile()) {
                newContent = readFile();
                if (newContent == null) {
                    LOGGER.error("newContent == null");
                    return;
                }
            }
            var preprocessor = new LuaPreprocessor();
            content = preprocessor.processCode(newContent);
        }

        public String readFile() {
            if (path == null) return null;
            try {
                return Files.readString(path);
            } catch (IOException e) {
                LOGGER.error("error read file: {}", path, e);
                return null;
            }
        }

        public @Nullable Path getPath() {
            return path;
        }

        public @NotNull String getContent() {
            return content;
        }

        public @NotNull String getName() {
            return name;
        }

        public LuaValue load(@NotNull Globals environment) {
            LOGGER.debug("load chunk: {}", name);
            return INSTANCE.loadChunk(content, name, environment);
        }
    }
}