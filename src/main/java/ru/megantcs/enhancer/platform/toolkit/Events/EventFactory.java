package ru.megantcs.enhancer.platform.toolkit.Events;

import ru.megantcs.enhancer.platform.interfaces.Func;
import ru.megantcs.enhancer.platform.toolkit.Events.api.Event;
import ru.megantcs.enhancer.platform.toolkit.Events.impl.ArrayBabkendEvent;
import ru.megantcs.enhancer.platform.toolkit.Events.interfaces.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventFactory
{
    public static <T> Event<T> makeArrayBackend(Class<T> tClass, Func<T[], T> invokerHandler) {
        return new ArrayBabkendEvent<>(tClass, invokerHandler);
    }

    public static <T> ActionEvent<T> makeActionEvent(List<Action<T>> listType) {
        return new ActionEvent<>(listType);
    }

    public static <T> ActionEvent<T> makeActionEventSync() {
        return new ActionEvent<>(new CopyOnWriteArrayList<>());
    }

    public static <T> ActionEvent<T> makeActionEventArray() {
        return new ActionEvent<>(new ArrayList<>());
    }

    public static RunnableEvent makeRunnable() {
        return new RunnableEvent();
    }

    public static <T1, T2> FuncEvent<T1, T2> makeFuncEvent() {
        return new FuncEvent<>(new CopyOnWriteArrayList<>(), null);
    }

    public static <T1, T2> FuncEvent<T1, T2> makeFuncEvent(T2 defaultReturnType) {
        return new FuncEvent<>(new CopyOnWriteArrayList<>(), defaultReturnType);
    }

    public static <T1, T2> FuncEvent<T1, T2> makeFuncEventArray(T2 defaultReturnType) {
        return new FuncEvent<>(new ArrayList<>(), defaultReturnType);
    }
}
