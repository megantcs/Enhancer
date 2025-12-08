package ru.megantcs.enhancer.platform.interfaces;

@FunctionalInterface
public interface Func<TFirstArgument, TReturnArgument>
{
    TReturnArgument get(TFirstArgument argument);
}
