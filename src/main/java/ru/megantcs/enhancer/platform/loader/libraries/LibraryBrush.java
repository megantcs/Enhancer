package ru.megantcs.enhancer.platform.loader.libraries;

import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.Colors.ColorConvertor;

/**
 * update libraries see {@link ru.megantcs.enhancer.platform.render.engine.common.luaLibraries}
 */
@Deprecated
@LuaExportClass(name = "Brush")
public class LibraryBrush
{
    private final Brush brush;

    public LibraryBrush(Brush b) {
        brush = b;
    }

    @LuaExportMethod(name = "create")
    public static LibraryBrush LibraryBrush(String hex) {
        return new LibraryBrush(Brush.of(hex));
    }

    @LuaExportMethod(name = "create2c")
    public static LibraryBrush LibraryBrush(String first, String second) {
        return new LibraryBrush(new Brush(ColorConvertor.hexToColor(first), ColorConvertor.hexToColor(second)));
    }

    @LuaExportMethod(name = "create3c")
    public static LibraryBrush LibraryBrush(String color1, String color2, String color3) {
        var b = new Brush(
                ColorConvertor.hexToColor(color1),
                ColorConvertor.hexToColor(color2),
                ColorConvertor.hexToColor(color3));

        return new LibraryBrush(b);
    }

    @LuaExportMethod(name = "create4c")
    public static LibraryBrush LibraryBrush(String color1, String color2, String color3, String color4) {
        var b = new Brush(
                ColorConvertor.hexToColor(color1),
                ColorConvertor.hexToColor(color2),
                ColorConvertor.hexToColor(color3),
                ColorConvertor.hexToColor(color4));

        return  new LibraryBrush(b);
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
