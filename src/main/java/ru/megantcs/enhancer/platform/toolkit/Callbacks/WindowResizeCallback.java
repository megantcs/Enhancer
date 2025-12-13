package ru.megantcs.enhancer.platform.toolkit.Callbacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import ru.megantcs.enhancer.platform.toolkit.Events.api.EventInvoker;
import ru.megantcs.enhancer.platform.toolkit.Events.impl.ArrayBackendEvent;

public interface WindowResizeCallback
{
    EventInvoker<WindowResizeCallback> EVENT_INVOKER = new ArrayBackendEvent<>(WindowResizeCallback.class,(windowResizeCallbacks ->
            (client, window) ->
    {
        for(var callback : windowResizeCallbacks)
            callback.onResized(client, window);
    }));

    void onResized(MinecraftClient client, Window window);
}
