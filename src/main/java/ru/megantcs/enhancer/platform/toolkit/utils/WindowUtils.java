package ru.megantcs.enhancer.platform.toolkit.utils;

import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.Vector2f;

public class WindowUtils
{
    public static Vector2f getCenter(Window window) {
        return new Vector2f(window.getScaledWidth() / 2.0f, window.getScaledHeight() / 2.0f);
    }

    public static Vector2f getScaledCenter(Window window) {
        float scaleFactor = (float) window.getScaleFactor();
        return new Vector2f(
                window.getWidth() / (2.0f * scaleFactor),
                window.getHeight() / (2.0f * scaleFactor)
        );
    }

    public static Vector2f getAbsoluteCenter(Window window) {
        return new Vector2f(window.getWidth() / 2.0f, window.getHeight() / 2.0f);
    }
}
