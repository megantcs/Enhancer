package ru.megantcs.enhancer.platform.toolkit.Events.api;

public abstract class EventInvoker<InvokerType>
{
    protected volatile InvokerType invoker;

    public final InvokerType invoker() { return invoker; }
    public abstract void register(InvokerType listener);
}
