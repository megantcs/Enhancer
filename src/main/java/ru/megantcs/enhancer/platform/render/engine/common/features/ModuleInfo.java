package ru.megantcs.enhancer.platform.render.engine.common.features;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ModuleInfo(String name, String author, String version)
{
    public ModuleInfo copy() {
        return new ModuleInfo(name, author, version);
    }

    @Override
    public @NotNull String toString() {
        return String.format("%s, %s, %s", name, author, version);
    }

    public static ModuleInfo fromModuleData(ModuleData data) {
        Objects.requireNonNull(data);
        return new ModuleInfo(data.name(), data.author(), data.version());
    }
}
