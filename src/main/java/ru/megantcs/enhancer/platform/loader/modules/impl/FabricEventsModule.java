package ru.megantcs.enhancer.platform.loader.modules.impl;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.luaj.vm2.LuaValue;
import ru.megantcs.enhancer.platform.loader.LuaEngine;
import ru.megantcs.enhancer.platform.loader.modules.LuaModule;
import ru.megantcs.enhancer.platform.loader.modules.LuaModuleData;


@LuaModuleData(name = "FabricEvent")
public class FabricEventsModule extends LuaModule
{
    public static MatrixStack MATRIX_STACK_INSTANCE = null;
    public static DrawContext DRAW_CONTEXT_INSTANCE = null;

    private LuaValue renderCallback = null;
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
        this.renderCallback = null;
        this.engine = null;
    }

    private void executeChunk() {
        if (engine == null) return;

        var hudRenderCallback = engine.getType("hudRenderCallback");
        this.renderCallback = hudRenderCallback.isfunction() ? hudRenderCallback : null;
    }
}
