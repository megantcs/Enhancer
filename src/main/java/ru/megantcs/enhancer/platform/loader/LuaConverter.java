package ru.megantcs.enhancer.platform.loader;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;

import java.lang.reflect.*;
import java.util.*;

public class LuaConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuaConverter.class);

    /**
     * Конвертирует Java-объект в Lua-значение
     */
    public static LuaValue toLua(Object obj) {
        if (obj == null) return LuaValue.NIL;

        if (obj instanceof Integer) return LuaValue.valueOf((Integer) obj);
        if (obj instanceof Double) return LuaValue.valueOf((Double) obj);
        if (obj instanceof Float) return LuaValue.valueOf((Float) obj);
        if (obj instanceof Boolean) return LuaValue.valueOf((Boolean) obj);
        if (obj instanceof String) return LuaValue.valueOf((String) obj);
        if (obj instanceof Long) return LuaValue.valueOf((Long) obj);

        if (obj instanceof List) {
            LuaTable table = new LuaTable();
            List<?> list = (List<?>) obj;
            for (int i = 0; i < list.size(); i++) {
                table.set(i + 1, toLua(list.get(i)));
            }
            return table;
        }

        if (obj instanceof Map) {
            LuaTable table = new LuaTable();
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                table.set(toLua(entry.getKey()), toLua(entry.getValue()));
            }
            return table;
        }

        if (obj.getClass().isAnnotationPresent(LuaExportClass.class)) {
            return wrapClassInstance(obj);
        }

        // По умолчанию - пытаемся создать таблицу из публичных полей
        return createTableFromObject(obj);
    }

    /**
     * Конвертирует Lua-значение в Java-объект указанного типа
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromLua(LuaValue value, Class<T> targetType) {
        if (value.isnil()) return null;

        if (targetType == Integer.class || targetType == int.class)
            return (T) Integer.valueOf(value.toint());
        if (targetType == Double.class || targetType == double.class)
            return (T) Double.valueOf(value.todouble());
        if (targetType == Float.class || targetType == float.class)
            return (T) Float.valueOf(value.tofloat());
        if (targetType == Boolean.class || targetType == boolean.class)
            return (T) Boolean.valueOf(value.toboolean());
        if (targetType == String.class)
            return (T) value.tojstring();
        if (targetType == Long.class || targetType == long.class)
            return (T) Long.valueOf(value.tolong());

        // Для списков и мап
        if (targetType == List.class && value.istable()) {
            List<Object> list = new ArrayList<>();
            LuaTable table = value.checktable();
            for (LuaValue key = table.next(LuaValue.NIL).arg1();
                 !key.isnil();
                 key = table.next(key).arg1()) {
                if (key.isint()) {
                    list.add(fromLua(table.get(key), Object.class));
                }
            }
            return (T) list;
        }

        return null;
    }

    /**
     * Обертывает экземпляр класса в Lua-таблицу с методами
     */
    private static LuaTable wrapClassInstance(Object instance) {
        Class<?> clazz = instance.getClass();
        LuaTable table = new LuaTable();

        // Добавляем поле __instance для хранения ссылки на Java-объект
        table.set("__instance", LuaUserdata.userdataOf(instance));
        table.set("__class", LuaValue.valueOf(clazz.getSimpleName()));

        // Автоматически добавляем все публичные методы
        if (clazz.getAnnotation(ru.megantcs.enhancer.platform.loader.api.LuaExportClass.class).autoMethods()) {
            addAutoMethods(table, instance, clazz);
        }

        // Автоматически добавляем все публичные поля
        if (clazz.getAnnotation(ru.megantcs.enhancer.platform.loader.api.LuaExportClass.class).autoFields()) {
            addAutoFields(table, instance, clazz);
        }

        return table;
    }

    private static void addAutoMethods(LuaTable table, Object instance, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers()) &&
                    !method.getName().startsWith("$") &&
                    !Modifier.isStatic(method.getModifiers())) {

                String methodName = method.getName();
                table.set(methodName, createMethodWrapper(method, instance));
            }
        }
    }

    private static void addAutoFields(LuaTable table, Object instance, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers()) &&
                    !field.getName().startsWith("$") &&
                    !Modifier.isStatic(field.getModifiers()) &&
                    !field.isAnnotationPresent(ru.megantcs.enhancer.platform.loader.api.LuaExportField.class)) {

                // Автоматически создаем геттер/сеттер для поля
                table.set(field.getName(), new FieldAccessor(field, instance));
            }
        }
    }

    private static LuaValue createMethodWrapper(Method method, Object instance) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                try {
                    // Конвертируем аргументы Lua в Java
                    Object[] javaArgs = convertArgs(method, args);

                    // Вызываем метод
                    Object result = method.invoke(instance, javaArgs);

                    // Конвертируем результат обратно в Lua
                    LuaValue luaResult = toLua(result);
                    return luaResult.isnil() ? LuaValue.NIL : luaResult;
                } catch (Exception e) {
                    LOGGER.error("Error calling method " + method.getName(), e);
                    return LuaValue.NIL;
                }
            }
        };
    }

    public static Object[] convertArgs(Method method, Varargs args) {
        Parameter[] parameters = method.getParameters();
        Object[] javaArgs = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++) {
            if (i < args.narg()) {
                javaArgs[i] = fromLua(args.arg(i + 1), parameters[i].getType());
            } else {
                javaArgs[i] = null;
            }
        }

        return javaArgs;
    }

    private static LuaTable createTableFromObject(Object obj) {
        LuaTable table = new LuaTable();
        Class<?> clazz = obj.getClass();

        // Добавляем все публичные поля
        for (Field field : clazz.getDeclaredFields()) {
            if (Modifier.isPublic(field.getModifiers())) {
                try {
                    table.set(field.getName(), toLua(field.get(obj)));
                } catch (Exception e) {
                    // Игнорируем недоступные поля
                }
            }
        }

        return table;
    }

    /**
     * Класс для доступа к полям объекта из Lua
     */
    private static class FieldAccessor extends LuaUserdata {
        private final Field field;
        private final Object instance;

        public FieldAccessor(Field field, Object instance) {
            super(instance);
            this.field = field;
            this.instance = instance;
            setmetatable(createMetatable());
        }

        private LuaTable createMetatable() {
            LuaTable mt = new LuaTable();

            // При чтении поля - возвращаем значение
            mt.set(LuaValue.INDEX, new ZeroArgFunction() {
                @Override
                public LuaValue call() {
                    try {
                        return toLua(field.get(instance));
                    } catch (Exception e) {
                        return LuaValue.NIL;
                    }
                }
            });

            // При записи в поле - устанавливаем значение
            mt.set(LuaValue.NEWINDEX, new OneArgFunction() {
                @Override
                public LuaValue call(LuaValue value) {
                    try {
                        Object javaValue = fromLua(value, field.getType());
                        field.set(instance, javaValue);
                    } catch (Exception e) {
                        LOGGER.error("Error setting field " + field.getName(), e);
                    }
                    return LuaValue.NIL;
                }
            });

            return mt;
        }
    }
}