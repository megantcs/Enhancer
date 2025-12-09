package ru.megantcs.enhancer.platform.render.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.render.api.Font.FontRender;
import ru.megantcs.enhancer.platform.render.api.FontStyle;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsContext;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsSystem;
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

    public void drawText(float x, float y, String text, Brush brush, FontStyle style) {
        GraphicsSystem.begin();
        BufferBuilder bufferBuilder = GraphicsSystem.instanceBuffer();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(bufferBuilder);

        mc.textRenderer.draw(text, x,y, brush.get(0).getRGB(),style == FontStyle.SHADOW, matrixStack.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.NORMAL, 0, 15728880, mc.textRenderer.isRightToLeft());
        immediate.draw();
        GraphicsSystem.end();
    }


    public void drawTextFont(FontRender font, float x, float y, String text, Brush brush) {
        Objects.requireNonNull(font);
        font.getFontRenderer().drawString(matrixStack, text, x, y, brush.get(0));
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
