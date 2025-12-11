package ru.megantcs.enhancer.platform.loader.modules.impl;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.luaj.vm2.LuaValue;
import ru.megantcs.enhancer.platform.loader.LuaEngine;
import ru.megantcs.enhancer.platform.loader.modules.LuaModule;
import ru.megantcs.enhancer.platform.loader.modules.LuaModuleData;


@LuaModuleData(name = "fem")
public class FabricEventsModules extends LuaModule
{
    public static MatrixStack MATRIX_STACK_INSTANCE = null;
    public static DrawContext DRAW_CONTEXT_INSTANCE = null;

    private LuaValue renderCallback = null;
    public static LuaValue renderBackgroundScoreboard = null;
    public static LuaValue renderHeaderScoreboard = null;
    public static LuaValue renderSeparatorScoreboard = null;

    private Runnable onLoadedChunk;
    private LuaEngine engine;

    @Override
    protected void init(LuaEngine engine) {
        registerEvents();

        if(onLoadedChunk == null) {
            onLoadedChunk = this::executeChunk;
        }
        this.engine = engine;
        engine.onLoadedScript.register(onLoadedChunk);
    }

    void registerEvents() {
        HudRenderCallback.EVENT.register((dc, delta) ->
        {
            MATRIX_STACK_INSTANCE = dc.getMatrices();
            DRAW_CONTEXT_INSTANCE = dc;


            if (this.renderCallback == null) return;

            try {
                engine.execute(renderCallback);
            } catch (Exception e) {
                LOGGER.error("error render callback", e);
            }
        });
    }

    @Override
    protected void cleanup() {
        LOGGER.warn("cleanup");

        this.renderCallback = null;
        renderBackgroundScoreboard = null;
        renderHeaderScoreboard = null;
        renderSeparatorScoreboard = null;

        this.engine = null;
    }

    private void executeChunk() {
        if (engine == null) return;

        var hudRenderCallback = engine.getType("hudRenderCallback");
        var renderBackgroundScoreboard = engine.getType("backgroundScoreboard");
        var renderHeaderScoreboard = engine.getType("headerScoreboard");
        var renderSeparatorScoreboard = engine.getType("separatorScoreboard");

        LOGGER.warn("execute chunk: {}",hudRenderCallback.toString());
        LOGGER.warn("execute chunk: {}",renderBackgroundScoreboard.toString());
        LOGGER.warn("execute chunk: {}",renderHeaderScoreboard.toString());

        this.renderCallback = hudRenderCallback.isfunction() ? hudRenderCallback : null;
        this.renderSeparatorScoreboard = renderSeparatorScoreboard.isfunction() ? renderSeparatorScoreboard : null;
        this.renderBackgroundScoreboard = renderBackgroundScoreboard.isfunction() ? renderBackgroundScoreboard : null;
        this.renderHeaderScoreboard = renderHeaderScoreboard.isfunction() ? renderHeaderScoreboard : null;

    }
}
