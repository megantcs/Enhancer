package ru.megantcs.enhancer.platform.toolkit.Events.impl;

import ru.megantcs.enhancer.platform.toolkit.Events.api.EventInvoker;
import ru.megantcs.enhancer.platform.toolkit.Events.api.EventLambdaSupported;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class RunnableEvent extends EventInvoker<Runnable>
    implements EventLambdaSupported<Runnable>
{
    private final List<RunnableEventData> subscribes = new CopyOnWriteArrayList<>();

    public RunnableEvent() {
        this.invoker = this::emit;
    }

    public void register(Runnable runnable) {
        subscribes.add(new RunnableEventData(Objects.requireNonNull(Objects.requireNonNull(runnable, "runnable cannot be is null")),
                        "runnable#event@" + runnable.hashCode()));
    }

    public boolean unregister(Runnable runnable) {
        Objects.requireNonNull(Objects.requireNonNull(runnable, "runnable cannot be is null"));
        return subscribes.removeIf((e)->{
            return e.sub == runnable;
        });
    }

    public void emit() {
        subscribes.forEach((e)->e.sub.run());
    }

    @Override
    public void register(Runnable sub, String name) {
        Objects.requireNonNull(sub, "runnable cannot be is null");
        Objects.requireNonNull(sub, "name cannot be is null");

        subscribes.add(new RunnableEventData(sub, name));
    }

    @Override
    public boolean unregister(String name) {
        return subscribes.removeIf((e)-> Objects.equals(e.name, name));
    }

    public static record RunnableEventData(Runnable sub, String name) {}
}
