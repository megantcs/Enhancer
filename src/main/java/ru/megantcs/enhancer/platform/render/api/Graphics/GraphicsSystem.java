package ru.megantcs.enhancer.platform.render.api.Graphics;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;

@Deprecated
public class GraphicsSystem
{
    public static void begin()
    {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void end()
    {
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void usePositionColor() {
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
    }

    public static BufferBuilder instanceBuffer() {
        return Tessellator.getInstance().getBuffer();
    }

    public static void draw()
    {
        Tessellator.getInstance().draw();
    }
}
