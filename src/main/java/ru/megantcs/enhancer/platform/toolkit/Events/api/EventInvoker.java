package ru.megantcs.enhancer.platform.toolkit.Events.api;

import java.util.List;

public abstract class Event<InvokerType>
{
    protected volatile InvokerType invoker;

    public final InvokerType invoker() { return invoker; }

    public abstract void register(InvokerType listener);
}
