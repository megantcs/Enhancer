package ru.megantcs.enhancer.platform.interfaces;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

public interface Minecraft
{
    MinecraftClient mc = MinecraftClient.getInstance();

    default String combinePath(String... paths) {
        StringBuilder result = new StringBuilder(mc.runDirectory.getPath());
        for(var path : paths) {
            result.append("\\").append(path);
        }

        return result.toString();
    }
}
