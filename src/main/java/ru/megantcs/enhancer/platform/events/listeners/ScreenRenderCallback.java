package ru.megantcs.enhancer.platform.events.listeners;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.megantcs.enhancer.platform.interfaces.mixins.DrawableMixin;
import ru.megantcs.enhancer.platform.toolkit.Events.EventFactory;
import ru.megantcs.enhancer.platform.toolkit.Events.api.Event;

public interface ScreenRenderCallback extends DrawableMixin
{
    Event<ScreenRenderCallback> GAME_MENU_POST = EventFactory
            .createArrayBackend(ScreenRenderCallback.class, (subscribes)->
                    ((context, mouseX, mouseY, delta, ci) ->
    {
        for(var sub : subscribes) sub.render(context, mouseX, mouseY, delta, ci);
    }));

    @Override
    void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci);
}
