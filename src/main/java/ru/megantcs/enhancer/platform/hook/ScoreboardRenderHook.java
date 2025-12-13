package ru.megantcs.enhancer.platform.hook;

import net.minecraft.client.gui.DrawContext;
import ru.megantcs.enhancer.platform.loader.api.LuaTableExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaTableExportField;
import ru.megantcs.enhancer.platform.toolkit.Events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.Events.impl.FuncEvent;

public interface ScoreboardRenderHook
{
    FuncEvent<RenderInfo, Boolean> RENDER_BACKGROUND = EventFactory.makeFuncEvent(false);
    FuncEvent<RenderInfo, Boolean> RENDER_SEPARATOR = EventFactory.makeFuncEvent(false);
    FuncEvent<RenderInfo, Boolean> RENDER_HEADER = EventFactory.makeFuncEvent(false);
    FuncEvent<RenderInfo, Boolean> RENDER_END = EventFactory.makeFuncEvent(false);

    @LuaTableExportClass
    public static record RenderInfo(DrawContext context,
                                    @LuaTableExportField(name = "left")   int left,
                                    @LuaTableExportField(name = "top")    int top,
                                    @LuaTableExportField(name = "right")  int right,
                                    @LuaTableExportField(name = "bottom") int bottom,
                                    @LuaTableExportField(name = "color")  int color) {}
}
