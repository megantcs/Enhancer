package ru.megantcs.enhancer.platform.interfaces;

@FunctionalInterface
public interface Returnable<TReturnType>
{
    Returnable<Boolean> FALSE = () -> false;
    Returnable<Boolean> TRUE  = () -> true;

    TReturnType get();
}
