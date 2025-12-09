package ru.megantcs.enhancer.platform.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.megantcs.enhancer.platform.events.listeners.ScreenRenderCallback;
import ru.megantcs.enhancer.platform.interfaces.mixins.DrawableMixin;

@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin implements DrawableMixin
{
    @Override
    @Inject(at = @At("TAIL"), method = "render")
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci)
    {
        ScreenRenderCallback.GAME_MENU_POST.invoker().render(context, mouseX, mouseY, delta, ci);
    }
}
