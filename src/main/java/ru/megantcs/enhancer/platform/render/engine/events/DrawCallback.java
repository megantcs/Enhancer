package ru.megantcs.enhancer.platform.render.engine.events;

import net.minecraft.client.util.math.MatrixStack;
import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.Events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.Events.api.EventInvoker;

public class DrawCallback
{
    public EventInvoker<DataCallback> EVENT = EventFactory.makeArrayBackend(DataCallback.class, (dataCallbackDraws ->
            ((object, delta) -> {
                for (var item : dataCallbackDraws)
                    item.render(object, delta);
            })));

    public static interface DataCallback {
        void render(RenderObject object, float delta);
    }
}
