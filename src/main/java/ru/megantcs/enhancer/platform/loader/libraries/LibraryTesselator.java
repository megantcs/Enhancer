package ru.megantcs.enhancer.platform.loader.libraries;

import net.minecraft.client.render.Tessellator;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

@LuaExportClass(name = "Tesselator")
public class LibraryTesselator
{
    public LibraryTesselator() {}

    @LuaExportMethod
    public static LibraryBufferBuilder getBuffer()
    {
        return new LibraryBufferBuilder();
    }

    @LuaExportMethod
    public static void draw()
    {
        Tessellator.getInstance().draw();
    }
}
