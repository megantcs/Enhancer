package ru.megantcs.enhancer.platform.render.api.Graphics;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import ru.megantcs.enhancer.platform.render.api.Shaders.BlurShader;
import ru.megantcs.enhancer.platform.render.api.Shaders.RectangleShader;

import java.awt.*;

@Deprecated
public class GraphicsContext
{
    public static RectangleShader RECTANGLE_SHADER;
    public static BlurShader BLUR_SHADER;

    public static void horizontalGradient(MatrixStack matrixStack, float x, float y, float wight, float height, Color first, Color second)
    {
        GraphicsSystem.begin();
        GraphicsSystem.usePositionColor();

        var buffer = GraphicsSystem.instanceBuffer();
        var matrix = matrixStack.peek().getPositionMatrix();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x, y, 0).color(first.getRGB()).next();
        buffer.vertex(matrix,x, height, 0).color(first.getRGB()).next();
        buffer.vertex(matrix,wight, height, 0).color(second.getRGB()).next();
        buffer.vertex(matrix,wight, y, 0).color(second.getRGB()).next();

        GraphicsSystem.draw();
        GraphicsSystem.end();
    }

    public static void rect(MatrixStack matrixStack, float x1, float y1, float x2, float y2, Color color) {
        int rgb = color.getRGB();

        GraphicsSystem.begin();
        GraphicsSystem.usePositionColor();

        var buffer = GraphicsSystem.instanceBuffer();
        var matrix = matrixStack.peek().getPositionMatrix();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x1, y1, 0).color(rgb).next();
        buffer.vertex(matrix, x1, y2, 0).color(rgb).next();
        buffer.vertex(matrix, x2, y2, 0).color(rgb).next();
        buffer.vertex(matrix, x2, y1, 0).color(rgb).next();


        GraphicsSystem.draw();
        GraphicsSystem.end();
    }

    public static void verticalGradient(MatrixStack matrixStack, float x, float y, float wight, float height, Color first, Color second)
    {
        GraphicsSystem.begin();
        GraphicsSystem.usePositionColor();

        var buffer = GraphicsSystem.instanceBuffer();
        var matrix = matrixStack.peek().getPositionMatrix();

        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex(matrix, x, y, 0).color(first.getRGB()).next();
        buffer.vertex(matrix,x, height, 0).color(second.getRGB()).next();
        buffer.vertex(matrix,wight, height, 0).color(second.getRGB()).next();
        buffer.vertex(matrix,wight, y, 0).color(first.getRGB()).next();

        GraphicsSystem.draw();
        GraphicsSystem.end();
    }

    public static BufferBuilder preShaderDraw(MatrixStack matrices, float x, float y, float width, float height)  {
        return preShaderDraw(matrices, x, y, 0, width, height);
    }

    public static BufferBuilder preShaderDraw(MatrixStack matrices, float x, float y, float z, float width, float height) {
        GraphicsSystem.begin();
        BufferBuilder buffer = GraphicsSystem.instanceBuffer();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        setRectanglePoints(buffer, matrix, x, y, z, x + width, y + height);

        return buffer;
    }

    public static void setRectanglePoints(BufferBuilder buffer, Matrix4f matrix, float x, float y, float z, float x1, float y1) {
        buffer.vertex(matrix, x, y, z).next();
        buffer.vertex(matrix, x, y1, z).next();
        buffer.vertex(matrix, x1, y1, z).next();
        buffer.vertex(matrix, x1, y, z).next();
    }

    public static void roundHorizontal(MatrixStack matrices, float x, float y, float width, float height, float radius, Color colorLeft, Color colorRight) {
        renderRoundedQuadHorizontal(matrices, colorLeft, colorRight, x, y, width + x, height + y, radius, 4);
    }

    public static void roundVertical(MatrixStack matrices, float x, float y, float width, float height, float radius, Color colorTop, Color colorBottom) {
        renderRoundedQuadVertical(matrices, colorTop, colorBottom, x, y, width + x, height + y, radius, 4);
    }

    public static void roundGradient(MatrixStack matrices, float x, float y, float width, float height, float radius, Color colorTopLeft, Color colorTopRight, Color colorBottomLeft, Color colorBottomRight) {
        renderRoundedQuadGradient(matrices, colorTopLeft, colorTopRight, colorBottomLeft, colorBottomRight, x, y, width + x, height + y, radius);
    }

    public static void renderRoundedQuadHorizontal(MatrixStack matrices, Color colorLeft, Color colorRight, double fromX, double fromY, double toX, double toY, double radius, double samples) {
        GraphicsSystem.begin();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        renderRoundedQuadInternalHorizontal(matrices.peek().getPositionMatrix(),
                colorLeft.getRed() / 255f, colorLeft.getGreen() / 255f, colorLeft.getBlue() / 255f, colorLeft.getAlpha() / 255f,
                colorRight.getRed() / 255f, colorRight.getGreen() / 255f, colorRight.getBlue() / 255f, colorRight.getAlpha() / 255f,
                fromX, fromY, toX, toY, radius, samples);
        GraphicsSystem.end();
    }

    public static void renderRoundedQuadVertical(MatrixStack matrices, Color colorTop, Color colorBottom, double fromX, double fromY, double toX, double toY, double radius, double samples) {
        GraphicsSystem.begin();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        renderRoundedQuadInternalVertical(matrices.peek().getPositionMatrix(),
                colorTop.getRed() / 255f, colorTop.getGreen() / 255f, colorTop.getBlue() / 255f, colorTop.getAlpha() / 255f,
                colorBottom.getRed() / 255f, colorBottom.getGreen() / 255f, colorBottom.getBlue() / 255f, colorBottom.getAlpha() / 255f,
                fromX, fromY, toX, toY, radius, samples);
        GraphicsSystem.end();
    }

    public static void renderRoundedQuadGradient(MatrixStack matrices, Color colorTopLeft, Color colorTopRight, Color colorBottomLeft, Color colorBottomRight, double fromX, double fromY, double toX, double toY, double radius) {
        GraphicsSystem.begin();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        renderRoundedQuadInternalGradient(matrices.peek().getPositionMatrix(),
                colorTopLeft.getRed() / 255f, colorTopLeft.getGreen() / 255f, colorTopLeft.getBlue() / 255f, colorTopLeft.getAlpha() / 255f,
                colorTopRight.getRed() / 255f, colorTopRight.getGreen() / 255f, colorTopRight.getBlue() / 255f, colorTopRight.getAlpha() / 255f,
                colorBottomLeft.getRed() / 255f, colorBottomLeft.getGreen() / 255f, colorBottomLeft.getBlue() / 255f, colorBottomLeft.getAlpha() / 255f,
                colorBottomRight.getRed() / 255f, colorBottomRight.getGreen() / 255f, colorBottomRight.getBlue() / 255f, colorBottomRight.getAlpha() / 255f,
                fromX, fromY, toX, toY, radius);
        GraphicsSystem.end();
    }

    public static void renderRoundedQuadInternalHorizontal(Matrix4f matrix,
                                                           float leftR, float leftG, float leftB, float leftA,
                                                           float rightR, float rightG, float rightB, float rightA,
                                                           double fromX, double fromY, double toX, double toY, double radius, double samples) {

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        double[][] corners = new double[][]{
                new double[]{toX - radius, toY - radius, radius},
                new double[]{toX - radius, fromY + radius, radius},
                new double[]{fromX + radius, fromY + radius, radius},
                new double[]{fromX + radius, toY - radius, radius}
        };

        for (int i = 0; i < 4; i++) {
            double[] current = corners[i];
            double rad = current[2];

            for (double r = i * 90d; r < (90 + i * 90d); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);

                float vertexX = (float) current[0] + sin;
                float vertexY = (float) current[1] + cos;

                float t = (vertexX - (float)fromX) / ((float)toX - (float)fromX);
                t = MathHelper.clamp(t, 0.0f, 1.0f);

                float rColor = leftR + (rightR - leftR) * t;
                float gColor = leftG + (rightG - leftG) * t;
                float bColor = leftB + (rightB - leftB) * t;
                float aColor = leftA + (rightA - leftA) * t;

                bufferBuilder.vertex(matrix, vertexX, vertexY, 0.0F)
                        .color(rColor, gColor, bColor, aColor)
                        .next();
            }
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void renderRoundedQuadInternalVertical(Matrix4f matrix,
                                                         float topR, float topG, float topB, float topA,
                                                         float bottomR, float bottomG, float bottomB, float bottomA,
                                                         double fromX, double fromY, double toX, double toY, double radius, double samples) {

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        double[][] corners = new double[][]{
                new double[]{toX - radius, toY - radius, radius},
                new double[]{toX - radius, fromY + radius, radius},
                new double[]{fromX + radius, fromY + radius, radius},
                new double[]{fromX + radius, toY - radius, radius}
        };

        for (int i = 0; i < 4; i++) {
            double[] current = corners[i];
            double rad = current[2];

            for (double r = i * 90d; r < (90 + i * 90d); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);

                float vertexX = (float) current[0] + sin;
                float vertexY = (float) current[1] + cos;

                float t = 1.0f - (vertexY - (float)fromY) / ((float)toY - (float)fromY);
                t = MathHelper.clamp(t, 0.0f, 1.0f);

                float rColor = topR + (bottomR - topR) * t;
                float gColor = topG + (bottomG - topG) * t;
                float bColor = topB + (bottomB - topB) * t;
                float aColor = topA + (bottomA - topA) * t;

                bufferBuilder.vertex(matrix, vertexX, vertexY, 0.0F)
                        .color(rColor, gColor, bColor, aColor)
                        .next();
            }
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }

    public static void renderRoundedQuadInternalGradient(Matrix4f matrix,
                                                         float topLeftR, float topLeftG, float topLeftB, float topLeftA,
                                                         float topRightR, float topRightG, float topRightB, float topRightA,
                                                         float bottomLeftR, float bottomLeftG, float bottomLeftB, float bottomLeftA,
                                                         float bottomRightR, float bottomRightG, float bottomRightB, float bottomRightA,
                                                         double fromX, double fromY, double toX, double toY, double radius) {

        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        double[][] corners = new double[][]{
                new double[]{toX - radius, toY - radius, radius, bottomRightR, bottomRightG, bottomRightB, bottomRightA},
                new double[]{toX - radius, fromY + radius, radius, topRightR, topRightG, topRightB, topRightA},
                new double[]{fromX + radius, fromY + radius, radius, topLeftR, topLeftG, topLeftB, topLeftA},
                new double[]{fromX + radius, toY - radius, radius, bottomLeftR, bottomLeftG, bottomLeftB, bottomLeftA}
        };

        for (int i = 0; i < 4; i++) {
            double[] current = corners[i];
            double rad = current[2];

            float cornerR = (float) current[3];
            float cornerG = (float) current[4];
            float cornerB = (float) current[5];
            float cornerA = (float) current[6];

            int prevIdx = (i + 3) % 4;
            int nextIdx = (i + 1) % 4;

            double[] prevCorner = corners[prevIdx];
            double[] nextCorner = corners[nextIdx];

            for (double r = i * 90; r < (90 + i * 90); r += 10) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);

                float vertexX = (float) current[0] + sin;
                float vertexY = (float) current[1] + cos;

                float normalizedX = (vertexX - (float)fromX) / ((float)toX - (float)fromX);
                float normalizedY = 1.0f - (vertexY - (float)fromY) / ((float)toY - (float)fromY);

                normalizedX = MathHelper.clamp(normalizedX, 0.0f, 1.0f);
                normalizedY = MathHelper.clamp(normalizedY, 0.0f, 1.0f);

                float topR = topLeftR + (topRightR - topLeftR) * normalizedX;
                float topG = topLeftG + (topRightG - topLeftG) * normalizedX;
                float topB = topLeftB + (topRightB - topLeftB) * normalizedX;
                float topA = topLeftA + (topRightA - topLeftA) * normalizedX;

                float bottomR = bottomLeftR + (bottomRightR - bottomLeftR) * normalizedX;
                float bottomG = bottomLeftG + (bottomRightG - bottomLeftG) * normalizedX;
                float bottomB = bottomLeftB + (bottomRightB - bottomLeftB) * normalizedX;
                float bottomA = bottomLeftA + (bottomRightA - bottomLeftA) * normalizedX;

                float rColor = topR + (bottomR - topR) * (1 - normalizedY);
                float gColor = topG + (bottomG - topG) * (1 - normalizedY);
                float bColor = topB + (bottomB - topB) * (1 - normalizedY);
                float aColor = topA + (bottomA - topA) * (1 - normalizedY);

                bufferBuilder.vertex(matrix, vertexX, vertexY, 0.0F)
                        .color(rColor, gColor, bColor, aColor)
                        .next();
            }
        }

        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }



    public static void drawRect(MatrixStack matrices, float x, float y, float width, float height, float radius, float alpha, Color c1, Color c2, Color c3, Color c4) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        float x1 = x - 10;
        float y1 = y - 10;
        float x2 = x1 + width + 20;
        float y2 = y1 + height + 20;

        buffer.vertex(matrix, x1, y1, 0).next();
        buffer.vertex(matrix, x1, y2, 0).next();
        buffer.vertex(matrix, x2, y2, 0).next();
        buffer.vertex(matrix, x2, y1, 0).next();

        RECTANGLE_SHADER.setParameters(x, y, width, height, radius, alpha, c1, c2, c3, c4);
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

    public static void drawRoundedBlur(MatrixStack matrices, float x, float y, float width, float height, float radius, Color c1, float blurStrenth, float blurOpacity) {
        BufferBuilder bb = preShaderDraw(matrices, x - 10, y - 10, width + 20, height + 20);
        BLUR_SHADER.setParameters(x, y, width, height, radius, c1, blurStrenth, blurOpacity);
        BLUR_SHADER.use();
        BufferRenderer.drawWithGlobalProgram(bb.end());
        GraphicsSystem.end();
    }
}
