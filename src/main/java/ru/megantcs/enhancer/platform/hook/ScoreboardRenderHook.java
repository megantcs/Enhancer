package ru.megantcs.enhancer.platform.hook;

import net.minecraft.client.gui.DrawContext;
import ru.megantcs.enhancer.platform.interfaces.Func;
import ru.megantcs.enhancer.platform.interfaces.Returnable;

public interface ScoreboardRenderHook
{
    Func<RenderInfo, Boolean> RENDER_BACKGROUND = Func.FALSE();
    Func<RenderInfo, Boolean> RENDER_SEPARATOR = Func.FALSE();
    Func<RenderInfo, Boolean> RENDER_HEADER = Func.FALSE();

    public static record RenderInfo(DrawContext context,
                                    int left, int top,
                                    int right, int bottom,
                                    int color) {}
}
