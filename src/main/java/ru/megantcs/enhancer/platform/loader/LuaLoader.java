package ru.megantcs.enhancer.platform.loader;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.interfaces.ResolveClient;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class LuaLoader implements ResolveClient {
    private static final Logger LOGGER = LoggerFactory.getLogger("Language Loader");

    private boolean debug;
    private Globals environment;
    private List<LuaValue> chunks;
    private LuaPreprocessor luaPreprocessor;

    @Override
    public void init() {
        chunks = new CopyOnWriteArrayList<>();
        environment = JsePlatform.standardGlobals();
        debug = false;
        luaPreprocessor = null;
    }

    public void useDebug() {
        debug = true;
    }

    public void offDebug() {
        debug = false;
    }

    public LuaPreprocessor usePreprocessor() {
        if (luaPreprocessor == null) {
            luaPreprocessor = new LuaPreprocessor();
        }
        return luaPreprocessor;
    }

    public void offPreprocessor() {
        luaPreprocessor = null;
    }

    public boolean isPreprocessorEnabled() {
        return luaPreprocessor != null;
    }

    private void message(String message) {
        if(debug) LOGGER.info(message);
    }

    private void warn(String message) {
        if(debug) LOGGER.warn(message);
    }

    private void error(String message) {
        if(debug) LOGGER.error(message);
    }

    public boolean loadFile(String path) {
        Objects.requireNonNull(path);
        if(!Files.exists(Path.of(path))) {
            error("file not found: " + path);
            return false;
        }

        try {
            message("load file: " + path);

            String fileContent = Files.readString(Path.of(path));

            if (luaPreprocessor != null) {
                fileContent = luaPreprocessor.processCode(fileContent);
                message("file preprocessed");
            }

            chunks.add(environment.load(fileContent, path, environment));
            updateChunkLast();
        } catch (Exception e) {
            LOGGER.error("failed load file: {}", path, e);
            return false;
        }

        return true;
    }

    public boolean registerModule(String moduleName, Object instance) {
        Objects.requireNonNull(moduleName);
        Objects.requireNonNull(instance);

        try {
            message("register module: " + moduleName);

            Class<?> clazz = instance.getClass();
            LuaTable moduleTable = new LuaTable();

            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                LuaMethod annotation = method.getAnnotation(LuaMethod.class);
                if (annotation != null && Modifier.isPublic(method.getModifiers())) {
                    String methodName = annotation.name().isEmpty() ? method.getName() : annotation.name();
                    moduleTable.set(methodName, LuaUtils.createMethodWrapper(method, instance, LOGGER));
                    message("  - method: " + methodName);
                }
            }

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                LuaField annotation = field.getAnnotation(LuaField.class);
                if (annotation != null && Modifier.isPublic(field.getModifiers())) {
                    if (annotation.read() || annotation.write()) {
                        moduleTable.set(field.getName(), new FieldWrapper(field, instance, annotation));
                        message("  - field: " + field.getName() +
                                " [read=" + annotation.read() +
                                ", write=" + annotation.write() + "]");
                    }
                }
            }

            environment.set(moduleName, moduleTable);
            message("module registered: " + moduleName);
            return true;

        } catch (Exception e) {
            LOGGER.error("failed register module: {}", moduleName, e);
            return false;
        }
    }

    public boolean loadCode(String code, String chunkName) {
        Objects.requireNonNull(code);

        if(code.isEmpty()) {
            error("code is empty: " + code);
            return false;
        }

        try {
            String processedCode = code;
            if (luaPreprocessor != null) {
                processedCode = luaPreprocessor.processCode(code);
                message("code preprocessed for chunk: " + chunkName);
            }

            chunks.add(environment.load(processedCode, "@" + chunkName, environment));
            updateChunkLast();
        } catch (Exception e) {
            LOGGER.error("failed load chunk: {} ;; source: {}", chunkName, code, e);
            return false;
        }

        return true;
    }

    public boolean loadCodeWithContext(String code, String chunkName, Map<String, Object> context) {
        Objects.requireNonNull(code);
        Objects.requireNonNull(context);

        if(code.isEmpty()) {
            error("code is empty: " + code);
            return false;
        }

        try {
            String processedCode = code;
            if (luaPreprocessor != null) {
                for (Map.Entry<String, Object> entry : context.entrySet()) {
                    if (entry.getValue() instanceof String) {
                        luaPreprocessor.addDefine(entry.getKey(), (String) entry.getValue());
                    }
                }
                processedCode = luaPreprocessor.processCode(code);
                message("code preprocessed with context for chunk: " + chunkName);
            }

            chunks.add(environment.load(processedCode, "@" + chunkName, environment));
            updateChunkLast();
        } catch (Exception e) {
            LOGGER.error("failed load chunk with context: {} ;; source: {}", chunkName, code, e);
            return false;
        }

        return true;
    }

    private LuaValue peek() {
        if(chunks.isEmpty()) return null;
        return chunks.get(chunks.size() - 1);
    }

    private void updateChunkLast() {
        message("execute chunk last");
        var chunk = peek();
        assert chunk != null;

        try {
            chunk.call();
        } catch (Exception e) {
            LOGGER.error("error update chunk", e);
        }
    }

    public void addPreprocessorDefine(String name, String value) {
        if (luaPreprocessor != null) {
            luaPreprocessor.addDefine(name, value);
        }
    }

    public void removePreprocessorDefine(String name) {
        if (luaPreprocessor != null) {
            luaPreprocessor.removeDefine(name);
        }
    }

    public void addPreprocessorProcessor(TextPipeline.Processor processor) {
        if (luaPreprocessor != null) {
            luaPreprocessor.addProcessor(processor);
        }
    }

    public Globals getEnvironment() {
        return environment;
    }

    public List<LuaValue> getChunks() {
        return new java.util.ArrayList<>(chunks);
    }

    public int getChunkCount() {
        return chunks.size();
    }

    public void executeAllChunks() {
        message("executing all " + chunks.size() + " chunks");
        for (LuaValue chunk : chunks) {
            try {
                chunk.call();
            } catch (Exception e) {
                LOGGER.error("failed to execute chunk", e);
            }
        }
    }

    public void reloadAllChunks() {
        message("reloading all " + chunks.size() + " chunks");
        List<LuaValue> oldChunks = new java.util.ArrayList<>(chunks);
        chunks.clear();

        for (LuaValue chunk : oldChunks) {
            try {
                chunk.call();
                chunks.add(chunk);
            } catch (Exception e) {
                LOGGER.error("failed to reload chunk", e);
            }
        }
    }

    @Override
    public void shutdown() {
        environment = null;
        chunks.clear();
        luaPreprocessor = null;
    }
}