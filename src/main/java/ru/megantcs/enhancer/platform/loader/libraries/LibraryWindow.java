package ru.megantcs.enhancer.platform.loader.libraries;

import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

@LuaExportClass(name = "Window")
public class LWindow
{
    @SuppressWarnings("nouse")
    @LuaExportMethod(requireInit = true)
    public int getHeight() {
        return Minecraft.mc.getWindow().getHeight();
    }

    @LuaExportMethod(requireInit = true)
    public int getWidth() {
        return Minecraft.mc.getWindow().getWidth();
    }

    @LuaExportMethod(requireInit = true)
    public double getScaleFactor() {
        return Minecraft.mc.getWindow().getScaleFactor();
    }
}
