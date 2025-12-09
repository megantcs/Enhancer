package ru.megantcs.enhancer.platform.loader;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import ru.megantcs.enhancer.platform.loader.api.LuaField;

import java.lang.reflect.Field;

public class FieldWrapper extends VarArgFunction {
    private final Field field;
    private final Object instance;
    private final LuaField annotation;

    FieldWrapper(Field field, Object instance, LuaField annotation) {
        this.field = field;
        this.instance = instance;
        this.annotation = annotation;
    }

    public static Object convertLuaToJava(LuaValue luaValue, Class<?> targetType) {
        if (targetType == String.class) return luaValue.tojstring();
        if (targetType == int.class || targetType == Integer.class) return luaValue.toint();
        if (targetType == long.class || targetType == Long.class) return (long) luaValue.todouble();
        if (targetType == double.class || targetType == Double.class) return luaValue.todouble();
        if (targetType == float.class || targetType == Float.class) return (float) luaValue.todouble();
        if (targetType == boolean.class || targetType == Boolean.class) return luaValue.toboolean();
        if (LuaValue.class.isAssignableFrom(targetType)) return luaValue;
        if (targetType == Object.class) return luaValue.tojstring();
        return luaValue.touserdata(targetType);
    }

    @Override
    public Varargs invoke(Varargs args) {
        try {
            if (args.narg() == 0 && annotation.read()) {
                Object value = field.get(instance);
                return CoerceJavaToLua.coerce(value);
            } else if (args.narg() == 1 && annotation.write()) {
                Object value = convertLuaToJava(args.arg(1), field.getType());
                field.set(instance, value);
                return LuaValue.NIL;
            } else {
                return LuaValue.NIL;
            }
        } catch (Exception e) {
            return LuaValue.NIL;
        }
    }
}