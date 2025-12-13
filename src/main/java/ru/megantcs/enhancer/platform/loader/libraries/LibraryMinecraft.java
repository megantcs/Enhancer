package ru.megantcs.enhancer.platform.loader.libraries;

import net.minecraft.client.MinecraftClient;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

/**
 * update libraries see {@link ru.megantcs.enhancer.platform.render.engine.common.luaLibraries}
 */
@Deprecated
@LuaExportClass(name = "Minecraft")
public class LibraryMinecraft
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
    public LibraryEntity getPlayer() {
        return new LibraryEntity(Minecraft.mc.player);
    }

    public LibraryWindow getWindow() {
        return new LibraryWindow(MinecraftClient.getInstance().getWindow());
    }

    @LuaExportMethod
    public static LibraryMinecraft getInstance() {
        return new LibraryMinecraft();
    }
}
