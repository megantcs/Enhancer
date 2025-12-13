package ru.megantcs.enhancer.platform.render.engine.common.luaLibraries;

import net.minecraft.client.util.Window;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

@LuaExportClass(name = "Window")
public class LuaWindow
{
    private final Window window;

    public LuaWindow(Window window) {
        this.window = window;
    }

    @LuaExportMethod
    public int getWidth() {
        return window.getWidth();
    }

    @LuaExportMethod
    public int getHeight() {
        return window.getHeight();
    }

    @LuaExportMethod
    public long getHandle() {
        return window.getHandle();
    }
}
