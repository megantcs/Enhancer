package ru.megantcs.enhancer.platform.loader.libraries;

import com.mojang.blaze3d.systems.RenderSystem;
import ladysnake.satin.api.managed.ManagedCoreShader;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.Identifier;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsContext;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsSystem;
import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.Colors.ColorConvertor;

import static ru.megantcs.enhancer.platform.loader.modules.impl.FabricEventsModule.DRAW_CONTEXT_INSTANCE;
import static ru.megantcs.enhancer.platform.loader.modules.impl.FabricEventsModule.MATRIX_STACK_INSTANCE;

/**
 * update libraries see {@link ru.megantcs.enhancer.platform.render.engine.common.luaLibraries}
 */
@Deprecated
@LuaExportClass(name = "RenderUtil")
public class LibraryRenderUtil
{
    public static RenderObject obj = null;

    public static final ManagedCoreShader RECTANGLE_SHADER = ShaderEffectManager.getInstance()
            .manageCoreShader(Identifier.of("enchancer", "rectangle"), VertexFormats.POSITION_COLOR);

    @LuaExportMethod
    public static void drawText(float x, float y, String text, String hex, boolean shadow)
    {
        DRAW_CONTEXT_INSTANCE.drawText(MinecraftClient.getInstance().textRenderer, text, (int)x, (int)y,
                ColorConvertor.hexToColor(hex).getRGB(), shadow);
    }

    @LuaExportMethod
    public static int calcTextWidth(String text) {
        return Minecraft.mc.textRenderer.getWidth(text);
    }

    @LuaExportMethod
    public static void rectangle(float x, float y, float z, float width, float height, float r1, float r2, float r3, float r4, float smoothness,
                                 String hex1,
                                 String hex2,
                                 String hex3,
                                 String hex4) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);

        RenderSystem.setShader(RECTANGLE_SHADER::getProgram);
        RECTANGLE_SHADER.findUniform2f("Size").set(width, height);
        RECTANGLE_SHADER.findUniform4f("Radius").set(r1,r2,r3,r4);
        RECTANGLE_SHADER.findUniform1f("Smoothness").set(smoothness);

        int color1 = ColorConvertor.hexToColor(hex1).getRGB();
        int color2 = ColorConvertor.hexToColor(hex2).getRGB();;
        int color3 = ColorConvertor.hexToColor(hex3).getRGB();;
        int color4 = ColorConvertor.hexToColor(hex4).getRGB();;

        var matrix = MATRIX_STACK_INSTANCE.peek().getPositionMatrix();

        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        builder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        builder.vertex(matrix, x, y, z).color(color1).next();
        builder.vertex(matrix, x, y + height, z).color(color2).next();
        builder.vertex(matrix, x + width, y + height, z).color(color3).next();
        builder.vertex(matrix, x + width, y, z).color(color4).next();

        BufferRenderer.drawWithGlobalProgram(builder.end());

        RenderSystem.depthMask(true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    @LuaExportMethod
    public static void drawRoundHorizontal(float x1, float y1, float x2, float y2, float radius, String hex1, String hex2)
    {
        GraphicsContext.roundHorizontal(MATRIX_STACK_INSTANCE, x1, y1, x2, y2, radius, ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2));
    }

    @LuaExportMethod
    public static void drawRoundVertical(float x1, float y1, float x2, float y2, float radius, String hex1, String hex2)
    {
        GraphicsContext.roundVertical(MATRIX_STACK_INSTANCE, x1, y1, x2, y2, radius, ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2));
    }

    @LuaExportMethod
    public static void drawRoundGradient(float x1, float y1, float x2, float y2, float radius, String hex1, String hex2, String hex3, String hex4)
    {
        GraphicsContext.roundGradient(MATRIX_STACK_INSTANCE, x1, y1, x2, y2, radius, ColorConvertor.hexToColor(hex1), ColorConvertor.hexToColor(hex2), ColorConvertor.hexToColor(hex3), ColorConvertor.hexToColor(hex4));
    }

    @LuaExportMethod
    public static RenderObject renderObject() {
        return obj;
    }
}
