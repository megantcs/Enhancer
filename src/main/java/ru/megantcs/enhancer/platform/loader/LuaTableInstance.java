package ru.megantcs.enhancer.platform.loader;

import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaTable;
import ru.megantcs.enhancer.platform.loader.api.LuaTableExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaTableExportField;

import java.lang.reflect.Field;
import java.util.Objects;

public class LuaTableInstance
{
    public static LuaTable generate(@NotNull Object obj)
    {
        Objects.requireNonNull(obj);
        if(!isTableExportClass(obj.getClass())) return null;

        LuaTable table = new LuaTable();
        var fields = obj.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            LuaTableExportField fieldData = field.getAnnotation(LuaTableExportField.class);
            if(fieldData == null) continue;;

            String name = fieldData.name().isEmpty()? field.getName()
                    : fieldData.name();

            try {
                var value = field.get(obj);
                table.set(name, LuaConverter.toLua(value));
            } catch (IllegalAccessException e)
            {
                /*
                *
                * */
            }
        }

        return table;
    }

    private static boolean isTableExportClass(@NotNull Class<?> clazz) {
        Objects.requireNonNull(clazz);
        LuaTableExportClass searched = clazz.getAnnotation(LuaTableExportClass.class);
        return searched != null;
    }
}
