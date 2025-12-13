package ru.megantcs.enhancer.platform.render.engine.common.features.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import ru.megantcs.enhancer.platform.render.engine.common.features.Module;
import ru.megantcs.enhancer.platform.render.engine.common.features.ModuleData;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@ModuleData(name = "Texture Module")
public class TextureRendererModule extends Module
{
    private final Map<String, Identifier> TEXTURE_CACHE = new HashMap<>();

    public Identifier loadTexture(Path filePath) {
        if (TEXTURE_CACHE.containsKey(filePath.toString())) {
            return TEXTURE_CACHE.get(filePath.toString());
        }

        try {
            File textureFile = filePath.toFile();

            if (!textureFile.exists()) {
                System.err.println("Texture file not found: " + filePath);
                return null;
            }

            String textureName = "local_storage_" + filePath.getFileName().toString().replaceAll("[^a-zA-Z0-9]", "_");
            Identifier textureId = new Identifier("enhancer", textureName);

            NativeImage image = NativeImage.read(Files.newInputStream(filePath));
            TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();

            NativeImageBackedTexture texture = new NativeImageBackedTexture(image);
            textureManager.registerTexture(textureId, texture);

            TEXTURE_CACHE.put(filePath.toString(), textureId);

            return textureId;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clearTextureCache() {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();

        for (Identifier textureId : TEXTURE_CACHE.values()) {
            textureManager.destroyTexture(textureId);
        }

        TEXTURE_CACHE.clear();
    }

    public void reloadTexture(Path filename) {
        if (TEXTURE_CACHE.containsKey(filename)) {
            Identifier oldId = TEXTURE_CACHE.remove(filename);
            MinecraftClient.getInstance().getTextureManager().destroyTexture(oldId);
            loadTexture(filename);
        }
    }
}
