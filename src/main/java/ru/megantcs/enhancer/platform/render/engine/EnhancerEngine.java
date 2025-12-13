package ru.megantcs.enhancer.platform.render.engine;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.loader.LuaEngine;
import ru.megantcs.enhancer.platform.loader.LuaScriptManager;
import ru.megantcs.enhancer.platform.render.engine.common.abstracts.Engine;
import ru.megantcs.enhancer.platform.render.engine.common.features.impl.BaseRendererModule;
import ru.megantcs.enhancer.platform.render.engine.common.features.impl.FontRenderModule;
import ru.megantcs.enhancer.platform.render.engine.common.luaLibraries.*;
import ru.megantcs.enhancer.platform.render.engine.events.DrawCallback;
import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;
import ru.megantcs.enhancer.platform.render.engine.render.RenderThread;
import ru.megantcs.enhancer.platform.toolkit.api.FinishedField;

public class EnhancerEngine extends Engine
{
    private RenderThread renderThread;
    public DrawCallback drawCallback;

    @FinishedField
    private RenderThread.RenderContext renderCallback;

    public EnhancerEngine(@NotNull LuaScriptManager scriptManager) {
        super(scriptManager);
    }

    @Override
    protected void init()
    {
        renderThread = new RenderThread();
        drawCallback = new DrawCallback();

        renderCallback = this::mainRenderThread;
        renderThread.addHandler(renderCallback);

        registerClasses();
    }

    private void registerClasses()
    {
        LuaEngine.INSTANCE.registerClass(RenderObject.class);
        LuaEngine.INSTANCE.registerClass(LuaMinecraft.class);
        LuaEngine.INSTANCE.registerClass(LuaPlayer.class);
        LuaEngine.INSTANCE.registerClass(LuaRenderSystem.class);
        LuaEngine.INSTANCE.registerClass(LuaRenderUtil.class);
        LuaEngine.INSTANCE.registerClass(LuaWindow.class);
    }

    @Override
    protected void dispose()
    {
        if(renderCallback != null){
            renderThread.remove(renderCallback);
        }
    }

    private RenderObject getRenderObject(MatrixStack matrixStack) {
        return  new RenderObject(matrixStack, getModule(BaseRendererModule.class).get());
    }

    private void mainRenderThread(DrawContext drawContext,
                                  MatrixStack matrixStack,
                                  float delta)
    {
        LuaRenderUtil.cur = getRenderObject(matrixStack);
        drawCallback.EVENT.invoker().render(LuaRenderUtil.cur, delta);
    }
}
