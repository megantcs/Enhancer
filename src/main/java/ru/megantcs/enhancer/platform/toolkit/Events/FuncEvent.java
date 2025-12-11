package ru.megantcs.enhancer.platform.toolkit.Events;

import ru.megantcs.enhancer.platform.interfaces.Func;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FuncEvent<ReturnType, ArgumentType>
{
    private final List<Func<ArgumentType, ReturnType>> subscribes;
    private final ReturnType defaultValue;

    public FuncEvent(List<Func<ArgumentType, ReturnType>> listType, ReturnType returnType) {
        subscribes = Objects.requireNonNull(listType);
        defaultValue = returnType;
    }

    public void register(Func<ArgumentType, ReturnType> event) {
        subscribes.add(Objects.requireNonNull(event));
    }

    public boolean unregister(Func<ArgumentType, ReturnType> event) {
        return subscribes.remove(Objects.requireNonNull(event));
    }

    public ReturnType emit(ArgumentType argumentType) {
        var result = defaultValue;
        for (Func<ArgumentType, ReturnType> subscribe : subscribes) {
            result = subscribe.get(argumentType);
            if(defaultValue != null &&
                    result == null) result = defaultValue;
        }
        return result;
    }

    public List<Func<ArgumentType, ReturnType>> getSubscribes() {
        return Collections.unmodifiableList(subscribes);
    }
}
