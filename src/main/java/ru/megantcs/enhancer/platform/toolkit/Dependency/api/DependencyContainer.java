package ru.megantcs.enhancer.platform.toolkit.Dependency.api;

import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.NotNull;

public interface DependencyContainer
{
    <T> T registerClass(Class<T> type);
    <T> T registerService(@NotNull Class<T> type, @NotNull Class<? extends T> realization);

    @CheckReturnValue
    <T> T get(Class<T> type);

    boolean shutdown();
    boolean reload();
}
