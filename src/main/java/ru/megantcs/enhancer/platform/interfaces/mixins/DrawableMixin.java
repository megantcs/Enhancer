package ru.megantcs.enhancer.platform.interfaces.mixins;

import net.minecraft.client.gui.DrawContext;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public interface DrawableMixin {
    void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci);
}
