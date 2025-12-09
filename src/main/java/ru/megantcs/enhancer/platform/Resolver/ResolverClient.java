package ru.megantcs.enhancer.platform.Resolver;

import ru.megantcs.enhancer.platform.interfaces.ResolveShutdown;
import ru.megantcs.enhancer.platform.interfaces.ResolverInit;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResolverClient
{
    private final List<Class<?>> classes;
    private final List<Object> entities;

    public ResolverClient() {
        classes = new CopyOnWriteArrayList<>();
        entities = new CopyOnWriteArrayList<>();
    }

    public void register(Class<?> clazz) {
        classes.add(Objects.requireNonNull(clazz));
        entities.add(instanceEntity(clazz));
    }

    private Object instanceEntity(Class<?> clazz) {
        try {
            return clazz.newInstance();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T get(Class<T> klass) {
        return (T)entities.stream()
                .filter((e)->e.getClass() == klass)
                .findFirst().orElse(null);
    }

    public void reload() {
        List<Class<?>> classesToReload = new CopyOnWriteArrayList<>(classes);

        shutdownAll();
        entities.clear();
        classes.clear();

        for (Class<?> klass : classesToReload) {
            register(klass);
        }

        initializeAll();
    }

    public void initializeAll()
    {
        entities.forEach((e)->{
            if(e instanceof ResolverInit i) i.init();
        });
    }

    public void shutdownAll() {
        entities.forEach((e)->{
            if(e instanceof ResolveShutdown i) i.shutdown();
        });
    }
}
