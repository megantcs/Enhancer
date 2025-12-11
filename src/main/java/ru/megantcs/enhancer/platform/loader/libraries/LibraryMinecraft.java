package ru.megantcs.enhancer.platform.loader.libraries;

import net.minecraft.client.MinecraftClient;
import org.apache.commons.compress.compressors.pack200.Pack200Utils;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

@LuaExportClass(name = "Minecraft")
public class LMinecraft
{
    @LuaExportMethod
    public String fpsString() {
        return MinecraftClient.getInstance().fpsDebugString;
    }

    @LuaExportMethod
    public int getCurrentFps() {
        return MinecraftClient.getInstance().getCurrentFps();
    }


    @LuaExportMethod
    public LEntity getPlayer() {
        return new LEntity(Minecraft.mc.player);
    }

    @LuaExportMethod
    public static LMinecraft getInstance() {
        return new LMinecraft();
    }
}
