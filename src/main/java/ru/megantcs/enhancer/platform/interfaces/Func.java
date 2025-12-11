package ru.megantcs.enhancer.platform.interfaces;

@FunctionalInterface
public interface Func<TFirstArgument, TReturnArgument>
{
    static <T> Func<T, Boolean> FALSE() {return e -> false;}
    static <T> Func<T, Boolean> TRUE() {return e -> true;}

    TReturnArgument get(TFirstArgument argument);
}
