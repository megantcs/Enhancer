package ru.megantcs.enhancer.platform.render.engine.common.luaLibraries;

import net.minecraft.client.render.Tessellator;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;
import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;

@LuaExportClass(name = "RenderUtil")
public class LuaRenderUtil
{
    public static RenderObject cur;

    @LuaExportMethod
    public LuaBufferBuilder getBuffer() {
        return new LuaBufferBuilder();
    }

    @LuaExportMethod
    public static void draw()
    {
        Tessellator.getInstance().draw();
    }

    @LuaExportMethod
    public static RenderObject getRenderObject() {
        return cur;
    }
}
