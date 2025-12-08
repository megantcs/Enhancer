package ru.megantcs.enhancer.platform.toolkit.Events.impl;

import ru.megantcs.enhancer.platform.interfaces.Func;
import ru.megantcs.enhancer.platform.toolkit.Events.api.Event;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class ArrayBabkendEvent<T> extends Event<T>
{
    Func<T[], T> invokerHandler;
    Set<T> subscribes;

    final Class<T> type;

    public ArrayBabkendEvent(Class<T> type, Func<T[], T> invokerHandler)
    {
        this.type = type;
        this.invokerHandler = invokerHandler;
        subscribes = new HashSet<>();

        updateInvoker();
    }

    void updateInvoker() {
        T[] array = (T[]) Array.newInstance(type, subscribes.size());
        array = subscribes.toArray(array);
        invoker = invokerHandler.get(array);
    }

    @Override
    public void register(T listener)
    {
        subscribes.add(listener);
        updateInvoker();
    }
}
