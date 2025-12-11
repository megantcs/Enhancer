package ru.megantcs.enhancer.platform.toolkit.Events;

import ru.megantcs.enhancer.platform.interfaces.Func;

import java.util.List;
import java.util.Objects;

public class FuncEvent<ArgumentType, ReturnType>
{
    private final List<FuncEventData<ArgumentType, ReturnType>> subscribes;
    private final ReturnType defaultValue;

    public FuncEvent(List<FuncEventData<ArgumentType, ReturnType>> listType, ReturnType returnType) {
        subscribes = Objects.requireNonNull(listType);
        defaultValue = returnType;
    }

    public void register(Func<ArgumentType, ReturnType> event) {
        subscribes.add(new FuncEventData<>(Objects.requireNonNull(event), "@" + event.hashCode()));
    }

    public void register(Func<ArgumentType, ReturnType> event, String name) {
        subscribes.add(new FuncEventData<>(event, name));
    }

    public boolean unregister(Func<ArgumentType, ReturnType> event)
    {
        Objects.requireNonNull(event);
        return subscribes.removeIf((data)-> data.subscribe == event);
    }

    public boolean unregister(String name)
    {
        Objects.requireNonNull(name);
        return subscribes.removeIf((data) -> Objects.equals(data.name, name));
    }

    public ReturnType emit(ArgumentType argumentType) {
        var result = defaultValue;
        for (var data : subscribes) {
            var subscribe = data.subscribe;
            result = subscribe.get(argumentType);
            if(defaultValue != null &&
                    result == null) result = defaultValue;
        }
        return result;
    }

    public static record FuncEventData<ArgumentType, ReturnType>(Func<ArgumentType, ReturnType> subscribe, String name)
    {
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof String $)
                return Objects.equals(name, $);

            return subscribe == obj;
        }
    }
}
