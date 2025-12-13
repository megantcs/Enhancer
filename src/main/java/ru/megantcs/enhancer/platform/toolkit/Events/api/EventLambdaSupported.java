package ru.megantcs.enhancer.platform.toolkit.Events.api;

public interface EventLambdaSupported<InvokerType>
{
    void register(InvokerType sub, String name);

    @SuppressWarnings("UnusedReturnValue")
    boolean unregister(String name);
}
