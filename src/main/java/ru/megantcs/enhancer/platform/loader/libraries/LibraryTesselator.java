package ru.megantcs.enhancer.platform.loader.libraries;

import net.minecraft.client.render.Tessellator;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

@LuaExportClass(name = "Tesselator")
public class LTesselator
{
    public LTesselator() {}

    @LuaExportMethod
    public static LBufferBuilder getBuffer()
    {
        return new LBufferBuilder();
    }

    @LuaExportMethod
    public static void draw()
    {
        Tessellator.getInstance().draw();
    }
}
