package ru.megantcs.enhancer.platform.toolkit.Events;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.toolkit.Events.api.EventInvoker;
import ru.megantcs.enhancer.platform.toolkit.Events.interfaces.Action;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ActionEvent<T> extends EventInvoker<Action<T>>
{
    private final List<Action<T>> subscribes;

    public ActionEvent(@NotNull List<Action<T>> listType) {
        subscribes = Objects.requireNonNull(listType);
        this.invoker = this::emit;
    }

    public void register(@NotNull Action<T> sub)
    {
        Objects.requireNonNull(sub);
        subscribes.add(sub);
    }

    public boolean unregister(@NotNull Action<T> sub) {
        return subscribes.remove(Objects.requireNonNull(sub));
    }

    public void emit(T argument) {
        subscribes.forEach((tAction -> tAction.invoke(argument)));
    }

    public List<Action<T>> getSubscribes() {
        return Collections.unmodifiableList(subscribes);
    }
}
