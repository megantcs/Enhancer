package ru.megantcs.enhancer.platform.toolkit.Events;

import ru.megantcs.enhancer.platform.interfaces.Func;
import ru.megantcs.enhancer.platform.toolkit.Events.api.Event;
import ru.megantcs.enhancer.platform.toolkit.Events.impl.ArrayBabkendEvent;

public class EventFactory
{
    public static <T> Event<T> createArrayBackend(Class<T> tClass, Func<T[], T> invokerHandler) {
        return new ArrayBabkendEvent<>(tClass, invokerHandler);
    }
}
