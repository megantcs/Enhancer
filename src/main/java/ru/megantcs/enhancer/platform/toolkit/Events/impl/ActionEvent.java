package ru.megantcs.enhancer.platform.toolkit.Events.impl;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.toolkit.Events.api.EventInvoker;
import ru.megantcs.enhancer.platform.toolkit.Events.api.EventLambdaSupported;
import ru.megantcs.enhancer.platform.toolkit.Events.interfaces.Action;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ActionEvent<T> extends EventInvoker<Action<T>>
    implements EventLambdaSupported<Action<T>>
{
    private final List<ActionEventData<T>> subscribes;

    public ActionEvent(@NotNull List<ActionEventData<T>> listType) {
        subscribes = Objects.requireNonNull(listType);
        this.invoker = this::emit;
    }

    public void register(@NotNull Action<T> sub)
    {
        Objects.requireNonNull(sub, "Action cannot be is null");
        subscribes.add(new ActionEventData<>(sub, "action#event@" + sub.hashCode()));
    }

    public boolean unregister(@NotNull Action<T> sub) {
        Objects.requireNonNull(sub, "Action cannot be is null");
        return subscribes.removeIf((e)-> e.sub == sub);
    }

    public void emit(T argument) {
        subscribes.forEach((tAction -> tAction.sub.invoke(argument)));
    }

    @Override
    public void register(Action<T> sub, String name) {
        Objects.requireNonNull(sub, "Action cannot be is null");
        Objects.requireNonNull(name, "name cannot be is null");

        subscribes.add(new ActionEventData<>(sub, name));
    }

    @Override
    public boolean unregister(String name) {
        return subscribes.removeIf((e)-> Objects.equals(e.name, name));
    }

    public static record ActionEventData<T>(Action<T> sub, String name) {}
}
