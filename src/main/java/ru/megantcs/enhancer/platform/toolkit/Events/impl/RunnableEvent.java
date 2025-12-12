package ru.megantcs.enhancer.platform.toolkit.Events;

import ru.megantcs.enhancer.platform.toolkit.Events.api.EventInvoker;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class RunnableEvent extends EventInvoker<Runnable>
{
    private final List<Runnable> subscribes = new CopyOnWriteArrayList<>();

    public boolean contains(Runnable runnable) {
        return subscribes.contains(Objects.requireNonNull(runnable, "runnable cannot be is null"));
    }

    public void register(Runnable runnable) {
        subscribes.add(Objects.requireNonNull(Objects.requireNonNull(runnable, "runnable cannot be is null")));
    }

    public boolean unregister(Runnable runnable) {
        return subscribes.remove(Objects.requireNonNull(Objects.requireNonNull(runnable, "runnable cannot be is null")));
    }

    public void emit() {
        subscribes.forEach(Runnable::run);
    }
}
