package ru.megantcs.enhancer.platform.loader.libraries;

import net.minecraft.client.util.Window;
import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

import java.util.Objects;

@LuaExportClass(name = "Window")
public class LibraryWindow
{
    public final Window window;

    @SuppressWarnings("unused")
    public LibraryWindow() {
        window = Objects.requireNonNull(Minecraft.mc.getWindow());
    }

    @LuaExportMethod(name = "create")
    public LibraryWindow(Window window) {
        this.window = Objects.requireNonNull(window);
    }

    @SuppressWarnings("unused")
    @LuaExportMethod(requireInit = true)
    public int getHeight() {
        return window.getHeight();
    }

    @SuppressWarnings("unused")
    @LuaExportMethod(requireInit = true)
    public int getWidth() {
        return window.getWidth();
    }

    @SuppressWarnings("unused")
    @LuaExportMethod(requireInit = true)
    public double getScaleFactor() {
        return window.getScaleFactor();
    }
}
