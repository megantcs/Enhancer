package ru.megantcs.enhancer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.toolkit.Fabric.CommandBuilder;
import ru.megantcs.enhancer.platform.widgets.screens.CodeEditor;

public class Enhancer implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        HudRenderCallback.EVENT.register((dc,t)->
        {

        });
    }
}
