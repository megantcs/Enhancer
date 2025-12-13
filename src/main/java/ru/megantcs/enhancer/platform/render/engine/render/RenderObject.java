package ru.megantcs.enhancer.platform.render.engine.render;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;
import ru.megantcs.enhancer.platform.render.engine.common.features.impl.BaseRendererModule;
import ru.megantcs.enhancer.platform.render.engine.math.Vec3d;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.Colors.ColorConvertor;


import java.util.Objects;

@LuaExportClass(name = "RenderObject")
public class RenderObject
{
    final MatrixStack matrixStack;
    final BaseRendererModule rendererModule;

    public RenderObject(MatrixStack matrixStack, BaseRendererModule rendererModule) {
        this.matrixStack = Objects.requireNonNull(matrixStack);
        this.rendererModule = rendererModule;
    }

    public void drawLine(Vec3d pos, Vec3d endPos, Brush brush, float lineWidth)
    {
        rendererModule.drawLine(matrixStack, pos, endPos, brush, lineWidth);
    }

    @LuaExportMethod
    public void drawLine(float x,float y, float z, float x1, float y2, float z2, String hex1, String hex2, String hex3, String hex4, float lineWidth)
    {
        rendererModule.drawLine(matrixStack, new Vec3d(x, y, z), new Vec3d(x1, y2, z2), new Brush(ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2), ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)), lineWidth);
    }

    @LuaExportMethod
    public void drawRectLC(float x, float y, float z,
                         float height, float width, float radius, float alpha,
                         String hex1, String hex2, String hex3, String hex4) {
        rendererModule.drawRect(matrixStack, x, y, z, width, height, radius, alpha,
                new Brush(ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                        ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));
    }

    @LuaExportMethod
    public void drawBlurLC(float x, float y, float z, float height, float width, float radius, float alpha, float blur,
                         String hex1, String hex2, String hex3, String hex4) {
        Brush brush = new Brush(ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4));
        rendererModule.drawBlur(matrixStack, x, y, z, width, height, radius, brush.color1(), blur, alpha);
    }

    @LuaExportMethod
    public void drawEffectLC(float x, float y, float z, float height, float width, float radius,
                           String hex1, String hex2, String hex3, String hex4) {

        rendererModule.drawEffect(matrixStack, x, y, z, width, height, radius,
                new Brush(ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2),
                        ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4)));
    }

    public void drawRect(Vec3d pos, float height, float width, float radius, float alpha, Brush brush) {
        rendererModule.drawRect(matrixStack, (float) pos.x, (float) pos.y, (float) pos.z, width, height, radius, alpha, brush);
    }

    public void drawBlur(Vec3d pos, float height, float width, float radius, float alpha, float blur, Brush brush) {
        rendererModule.drawBlur(matrixStack, (float) pos.x, (float) pos.y, (float) pos.z, width, height, radius, brush.color1(), blur,alpha);
    }

    public void drawEffect(Vec3d pos, float height, float width, float radius, Brush brush) {
        rendererModule.drawEffect(matrixStack, (float)pos.x, (float)pos.y, (float)pos.z, width, height,radius, brush);
    }

    @LuaExportMethod
    public void drawImageLC(String filename, double x0, double y0,
                          double width, double height, float u, float v,
                          double regionWidth, double regionHeight,
                          double textureWidth, double textureHeight,
                          String hex1, String hex2, String hex3, String hex4) {
        rendererModule.drawTexture(matrixStack, filename, x0, y0,
                width, height, u, v, regionWidth, regionHeight, textureWidth, textureHeight,
                new Brush(ColorConvertor.hexToColor(hex1),
                          ColorConvertor.hexToColor(hex2),
                          ColorConvertor.hexToColor(hex3),
                          ColorConvertor.hexToColor(hex4)));
    }

    @LuaExportMethod
    public boolean drawFontLC(String font, float size, float x, float y, String text, String hex) {
        return rendererModule.drawFont(matrixStack, font, size, x, y, text, ColorConvertor.hexToColor(hex));
    }

    @LuaExportMethod
    public void drawMCFontLC(float x, float y, String text, String hex, boolean shadow)
    {
        var color = ColorConvertor.hexToColor(hex).getRGB();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());

        try {
            Minecraft.mc.textRenderer.draw(Text.of(text), x, y, color, shadow, matrixStack.peek().getPositionMatrix(), immediate, TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        }
        finally {
            immediate.draw();
        }
    }
}
