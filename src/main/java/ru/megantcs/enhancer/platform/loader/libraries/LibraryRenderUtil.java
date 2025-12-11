package ru.megantcs.enhancer.platform.loader.libraries;

import net.minecraft.client.MinecraftClient;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsContext;
import ru.megantcs.enhancer.platform.toolkit.Colors.ColorConvertor;

import static ru.megantcs.enhancer.platform.loader.modules.impl.FabricEventsModules.DRAW_CONTEXT_INSTANCE;
import static ru.megantcs.enhancer.platform.loader.modules.impl.FabricEventsModules.MATRIX_STACK_INSTANCE;

@LuaExportClass(name = "RenderUtil")
public class LRenderUtil
{
    @LuaExportMethod
    public static void drawText(float x, float y, String text, String hex, boolean shadow)
    {
        DRAW_CONTEXT_INSTANCE.drawText(MinecraftClient.getInstance().textRenderer, text, (int)x, (int)y,
                ColorConvertor.hexToColor(hex).getRGB(), shadow);
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
}
