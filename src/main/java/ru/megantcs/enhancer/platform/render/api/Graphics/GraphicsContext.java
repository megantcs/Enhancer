package ru.megantcs.enhancer.platform.render.api.Graphics;

import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;

import java.awt.*;

public class GraphicsContext
{
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

    public static void preShaderDraw(MatrixStack matrices, float x, float y, float width, float height) {
        GraphicsSystem.begin();
        BufferBuilder buffer = GraphicsSystem.instanceBuffer();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        setRectanglePoints(buffer, matrix, x, y, x + width, y + height);
    }

    public static void setRectanglePoints(BufferBuilder buffer, Matrix4f matrix, float x, float y, float x1, float y1) {
        buffer.vertex(matrix, x, y, 0).next();
        buffer.vertex(matrix, x, y1, 0).next();
        buffer.vertex(matrix, x1, y1, 0).next();
        buffer.vertex(matrix, x1, y, 0).next();
    }

}
