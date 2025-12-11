package ru.megantcs.enhancer.platform.loader.libraries;

import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportField;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.Colors.ColorConvertor;

import java.awt.*;

@LuaExportClass(name = "Brush")
public class LBrush
{
    private final Brush brush;

    public LBrush() {
        brush = new Brush();
    }

    @LuaExportMethod(name = "create")
    public LBrush(String hex) {
        brush = Brush.of(hex);
    }

    @LuaExportMethod(name = "create2c")
    public LBrush(String first, String second) {
        brush = new Brush(ColorConvertor.hexToColor(first), ColorConvertor.hexToColor(second));
    }

    @LuaExportMethod(name = "create3c")
    public LBrush(String color1, String color2, String color3) {
        brush = new Brush(
                ColorConvertor.hexToColor(color1),
                ColorConvertor.hexToColor(color2),
                ColorConvertor.hexToColor(color3));
    }

    @LuaExportMethod(name = "create4c")
    public LBrush(String color1, String color2, String color3, String color4) {
        brush = new Brush(
                ColorConvertor.hexToColor(color1),
                ColorConvertor.hexToColor(color2),
                ColorConvertor.hexToColor(color3),
                ColorConvertor.hexToColor(color4));
    }

    @LuaExportMethod
    public int getColor(int i) {
        return brush.get(i).getRGB();
    }

    @LuaExportMethod
    public boolean isAvailable(int i) {
        return brush.isAvailable(i);
    }
}
