package ru.megantcs.enhancer.platform.render.engine.common.features.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsSystem;
import ru.megantcs.enhancer.platform.render.api.Shaders.BlurShader;
import ru.megantcs.enhancer.platform.render.api.Shaders.EffectShader;
import ru.megantcs.enhancer.platform.render.api.Shaders.RectangleShader;
import ru.megantcs.enhancer.platform.render.engine.common.features.Module;
import ru.megantcs.enhancer.platform.render.engine.common.features.ModuleData;
import ru.megantcs.enhancer.platform.render.engine.math.Vec3d;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;

import java.awt.*;

import static ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsContext.preShaderDraw;

@ModuleData(name = "Renderer", author = "megantcs", version = "1.0.0-latest")
public class RendererModule extends Module
{
    public static RectangleShader RECTANGLE_SHADER = new RectangleShader();
    public static BlurShader BLUR_SHADER = new BlurShader();
    public static EffectShader EFFECT_SHADER = new EffectShader();

    public void drawEffect(MatrixStack matrixStack, float x, float y, float z, float width, float height, float radius, Brush brush) {
        BufferBuilder bb = preShaderDraw(matrixStack, x - 10, y - 10, z, width + 20, height + 20);
        EFFECT_SHADER.setParameters(x, y, width, height, radius, 1, 1, brush.color1(), brush.color2(), brush.color3(), brush.color4());
        EFFECT_SHADER.use();
        BufferRenderer.drawWithGlobalProgram(bb.end());
        GraphicsSystem.end();
    }

    public void drawLine(MatrixStack matrixStack, Vec3d pos, Vec3d endPos, Brush brush, float lineWidth)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();

        int first = brush.first().getRGB();
        int second = brush.second().getRGB();

        RenderSystem.lineWidth(lineWidth);

        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);

        buffer.vertex(matrix, (float) pos.x, (float) pos.y, (float) pos.z)
                .color(first)
                .next();

        buffer.vertex(matrix, (float) endPos.x, (float) endPos.y, (float) endPos.z)
                .color(second)
                .next();

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }


    public void drawRect(MatrixStack matrices, float x, float y, float z, float width, float height, float radius, float alpha, Brush brush) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        float x1 = x - 10;
        float y1 = y - 10;
        float x2 = x1 + width + 20;
        float y2 = y1 + height + 20;

        buffer.vertex(matrix, x1, y1, z).next();
        buffer.vertex(matrix, x1, y2, z).next();
        buffer.vertex(matrix, x2, y2, z).next();
        buffer.vertex(matrix, x2, y1, z).next();

        RECTANGLE_SHADER.setParameters(x, y, width, height, radius, alpha,
                brush.color1(),
                brush.color2(),
                brush.color3(),
                brush.color4());

        RECTANGLE_SHADER.use();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask(false);

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public void drawBlur(MatrixStack matrices, float x, float y, float z, float width, float height, float radius, Color c1, float blurStrenth, float blurOpacity) {
        BufferBuilder bb = preShaderDraw(matrices, x,y, z, width, height);
        BLUR_SHADER.setParameters(x, y, width, height, radius, c1, blurStrenth, blurOpacity);
        BLUR_SHADER.use();

        BufferRenderer.drawWithGlobalProgram(bb.end());
        GraphicsSystem.end();
    }

    public void drawTexture(MatrixStack matrices, String filename, double x0, double y0, double width, double height, float u, float v, double regionWidth, double regionHeight, double textureWidth, double textureHeight, Brush brush) {
        var path = root.getFilename(filename);
        Identifier textureId = loadTexture(path);
        if (textureId == null) {
            return;
        }
        
        double x1 = x0 + width;
        double y1 = y0 + height;
        double z = 0;

        var c1 = brush.color1();
        var c2 = brush.color2();
        var c3 = brush.color3();
        var c4 = brush.color4();

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrix, (float) x0, (float) y1, (float) z).texture((u) / (float) textureWidth, (v + (float) regionHeight) / (float) textureHeight).color(c1.getRGB()).next();
        bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z).texture((u + (float) regionWidth) / (float) textureWidth, (v + (float) regionHeight) / (float) textureHeight).color(c2.getRGB()).next();
        bufferBuilder.vertex(matrix, (float) x1, (float) y0, (float) z).texture((u + (float) regionWidth) / (float) textureWidth, (v) / (float) textureHeight).color(c3.getRGB()).next();
        bufferBuilder.vertex(matrix, (float) x0, (float) y0, (float) z).texture((u) / (float) textureWidth, (v + 0.0F) / (float) textureHeight).color(c4.getRGB()).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }
}
