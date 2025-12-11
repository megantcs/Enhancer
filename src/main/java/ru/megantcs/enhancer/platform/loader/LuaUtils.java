package ru.megantcs.enhancer.platform.loader;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LuaUtils
{
    public static LuaValue createMethodWrapper(Method method, Object instance, Logger LOGGER) {
        return new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                try {
                    Class<?>[] paramTypes = method.getParameterTypes();
                    Object[] javaArgs = new Object[paramTypes.length];

                    for (int i = 0; i < paramTypes.length; i++) {
                        if (i < args.narg()) {
                            javaArgs[i] = FieldWrapper.convertLuaToJava(args.arg(i + 1), paramTypes[i]);
                        }
                    }

                    Object result = method.invoke(instance, javaArgs);
                    return CoerceJavaToLua.coerce(result);
                } catch (Exception e) {
                    LOGGER.error("method invocation error: {}", method.getName(), e);
                    return LuaValue.NIL;
                }
            }
        };
    }

    public static Map<String, String> fixNamesMap() {
        Map<String, String> map = new HashMap<>();
        map.put("~", "_");
        map.put("@", "_");
        map.put("$", "_");

        return map;
    }

    public static String fixName(String name)
    {
        var result = name;
        for(var key : fixNamesMap().keySet()) {
            result = result.replace(key, fixNamesMap().get(key));
        }

        return result;
    }
}
