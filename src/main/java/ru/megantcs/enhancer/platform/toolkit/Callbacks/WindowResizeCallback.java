package ru.megantcs.enhancer.platform.toolkit.Callbacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import ru.megantcs.enhancer.platform.toolkit.Events.api.Event;
import ru.megantcs.enhancer.platform.toolkit.Events.impl.ArrayBabkendEvent;

public interface WindowResizeCallback
{
    Event<WindowResizeCallback> EVENT = new ArrayBabkendEvent<>(WindowResizeCallback.class,(windowResizeCallbacks ->
            (client, window) ->
    {
        for(var callback : windowResizeCallbacks)
            callback.onResized(client, window);
    }));

    void onResized(MinecraftClient client, Window window);
}
