package ru.megantcs.enhancer.platform.toolkit.api;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Objects;

public class FinishedFieldReflection
{
    public static void finishObj(@NotNull Object obj) throws IllegalAccessException {
        Objects.requireNonNull(obj);

        var fields = obj.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                FinishedField data = field.getAnnotation(FinishedField.class);
                if(data == null) continue;

                field.set(obj, null);
            }
            catch (IllegalAccessException e) {
                /*
                *
                * */
            }
        }
    }
}
