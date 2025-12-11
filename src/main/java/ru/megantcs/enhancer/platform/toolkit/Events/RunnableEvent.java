package ru.megantcs.enhancer.platform.toolkit.Events;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class RunnableEvent
{
    private final List<Runnable> subscribes = new CopyOnWriteArrayList<>();

    public boolean contains(Runnable runnable) {
        return subscribes.contains(runnable);
    }

    public void register(Runnable runnable) {
        subscribes.add(Objects.requireNonNull(runnable));
    }

    public boolean unregister(Runnable runnable) {
        return subscribes.remove(Objects.requireNonNull(runnable));
    }

    public void emit() {
        subscribes.forEach(Runnable::run);
    }
}
