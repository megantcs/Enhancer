package ru.megantcs.enhancer.platform.render.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsContext;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.Colors.BrushHelper;
import ru.megantcs.enhancer.platform.toolkit.Colors.GradientBrush;

import java.util.Objects;

public class Graphics implements Minecraft
{
    private final MatrixStack matrixStack;
    private final Window window;

    private Graphics(MatrixStack matrixStack) {
        this.matrixStack = matrixStack;
        window = mc.getWindow();
    }

    private Graphics(DrawContext drawContext) {
        this.matrixStack = drawContext.getMatrices();
        window = mc.getWindow();
    }

    public void drawRect(float x, float y, float width, float height, Brush brush)
    {
        if(BrushHelper.isGradient(brush)) drawRectGradient(x, y, width, height, (GradientBrush) brush);
        else {
            drawRectDefault(x, y, width, height, brush);
        }
    }

    private void drawRectGradient(float x, float y, float width, float height, GradientBrush brush)
    {
        brush.throwIsNotAvailable(2, "for gradient require 2 colors. current colors: " + brush.size());

        switch (brush.getType()) {
            case horizontal -> GraphicsContext.horizontalGradient(matrixStack, x, y, width, height, brush.get(0), brush.get(1));
            case vertical   -> GraphicsContext.verticalGradient(matrixStack, x, y, width, height, brush.get(0), brush.get(1));
            default -> {
                throw new IllegalArgumentException("not supported type: " + brush.getType());
            }
        }
    }

    public Window getWindow() {
        return window;
    }

    private void drawRectDefault(float x, float y, float wight, float height, Brush brush)
    {
        GraphicsContext.rect(matrixStack, x, y, wight, height, brush.first());
    }

    public static Graphics Instance(MatrixStack matrixStack) {
        RenderSystem.assertOnRenderThread();
        return new Graphics(Objects.requireNonNull(matrixStack));
    }

    public static Graphics Instance(DrawContext drawContext) {
        RenderSystem.assertOnRenderThread();
        return new Graphics(Objects.requireNonNull(drawContext));
    }
}
