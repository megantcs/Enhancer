package ru.megantcs.enhancer.platform.render.engine.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RenderThread
{
    private final List<RenderContext> renderCallbacks = new CopyOnWriteArrayList<>();
    private boolean isInitialized = false;

    public RenderThread() {
        run();
    }

    public void execute(RenderContext context) {
        if (context == null) {
            throw new IllegalArgumentException("RenderContext cannot be null");
        }

        RenderContext oneTimeCallback = new RenderContext() {
            private boolean executed = false;

            @Override
            public void draw(DrawContext drawContext, MatrixStack matrixStack, float delta) {
                if (!executed) {
                    context.draw(drawContext, matrixStack, delta);
                    executed = true;

                    try {
                        renderCallbacks.remove(this);
                    } catch (Exception ignored) {}
                }
            }
        };

        renderCallbacks.add(oneTimeCallback);
    }

    public void addHandler(RenderContext event)
    {
        if (event == null) {
            throw new IllegalArgumentException("RenderContext cannot be null");
        }

        renderCallbacks.add(event);
    }

    public void remove(RenderContext event) {
        renderCallbacks.remove(event);
    }

    public void clear() {
        renderCallbacks.clear();
    }

    private void run() {
        if (isInitialized) return;

        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            for (RenderContext callback : renderCallbacks) {
                try {
                    callback.draw(drawContext, drawContext.getMatrices(), tickDelta);
                } catch (Exception ignored) {}
            }
        }));

        isInitialized = true;
    }

    @FunctionalInterface
    public interface RenderContext {
        void draw(DrawContext drawContext, MatrixStack matrixStack, float delta);
    }
}