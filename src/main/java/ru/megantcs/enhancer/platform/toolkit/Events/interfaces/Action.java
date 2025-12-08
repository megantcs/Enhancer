package ru.megantcs.enhancer.platform.toolkit.Events.interfaces;

@FunctionalInterface
public interface Action<TArgument>
{
    void invoke(TArgument argument);
}
