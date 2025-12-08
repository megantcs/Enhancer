package ru.megantcs.enhancer.platform.toolkit.Colors;

public class BrushHelper
{
    public static boolean isGradient(Brush brush) {
        return brush instanceof GradientBrush;
    }
}
