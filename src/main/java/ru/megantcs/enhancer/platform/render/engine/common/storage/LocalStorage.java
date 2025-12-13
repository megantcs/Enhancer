package ru.megantcs.enhancer.platform.render.engine.common.storage;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class LocalStorage
{
    static final String prefix;

    static {
        String mcPath = Minecraft.mc.runDirectory.getPath();
        if (mcPath == null || mcPath.isEmpty()) {
            mcPath = ".";
        }
        prefix = Paths.get(mcPath, "config", "enhancer").toString();

        try {
            Files.createDirectories(Paths.get(prefix));
        } catch (IOException e) {
            System.err.println("Failed to create storage directory: " + e.getMessage());
        }
    }

    final String name;
    final String path;

    public LocalStorage(@NotNull String name) {
        Objects.requireNonNull(name, "Storage name cannot be null");

        if (name.contains("..") || name.contains("/") || name.contains("\\") || name.contains(":")) {
            throw new IllegalArgumentException("Invalid storage name: " + name);
        }

        this.name = name;
        this.path = Paths.get(prefix, name).toString();

        try {
            Files.createDirectories(Paths.get(this.path));
            System.out.println("Created storage directory: " + this.path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory: " + this.path, e);
        }
    }

    public String getFilename(@NotNull String fileName) {
        Objects.requireNonNull(fileName, "File name cannot be null");

        if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\") || fileName.contains(":")) {
            throw new IllegalArgumentException("Invalid file name: " + fileName);
        }

        return Paths.get(this.path, fileName).toString();
    }

    public Path getPath(@NotNull String fileName) {
        return Paths.get(getFilename(fileName));
    }

    // Вспомогательные методы

    public boolean fileExists(@NotNull String fileName) {
        return Files.exists(getPath(fileName));
    }

    public void deleteFile(@NotNull String fileName) throws IOException {
        Files.deleteIfExists(getPath(fileName));
    }

    public void createFile(@NotNull String fileName) throws IOException {
        Path filePath = getPath(fileName);
        if (!Files.exists(filePath)) {
            Files.createFile(filePath);
        }
    }
}