package ru.megantcs.enhancer.platform.toolkit.Dependency.impl;

import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.toolkit.Dependency.api.DependencyContainer;
import ru.megantcs.enhancer.platform.toolkit.Dependency.api.Initializable;
import ru.megantcs.enhancer.platform.toolkit.Dependency.api.Shutdownable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DependencyContainerImpl implements DependencyContainer {

    private final Map<Class<?>, Object> singletonInstances = new ConcurrentHashMap<>();
    private final Map<Class<?>, Class<?>> typeMappings = new ConcurrentHashMap<>();
    private final Map<Class<?>, ObjectFactory<?>> factories = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object> scopedInstances = new HashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private volatile boolean isShutdown = false;

    private static class ObjectFactory<T> {
        private final Class<T> type;
        private final DependencyContainerImpl container;

        ObjectFactory(Class<T> type, DependencyContainerImpl container) {
            this.type = type;
            this.container = container;
        }

        T create() {
            return container.createInstanceWithDependencies(type);
        }
    }

    @Override
    public <T> T registerClass(@NotNull Class<T> type) {
        checkShutdown();

        try {
            lock.writeLock().lock();

            if (singletonInstances.containsKey(type)) {
                return type.cast(singletonInstances.get(type));
            }

            T instance = createInstanceWithDependencies(type);
            singletonInstances.put(type, instance);
            typeMappings.put(type, type);
            factories.put(type, new ObjectFactory<>(type, this));

            return instance;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public <T> T registerService(@NotNull Class<T> type, @NotNull Class<? extends T> realization) {
        checkShutdown();

        try {
            lock.writeLock().lock();

            if (!type.isAssignableFrom(realization)) {
                throw new IllegalArgumentException(
                        "Class " + realization.getName() +
                                " does not implement/extends " + type.getName()
                );
            }

            typeMappings.put(type, realization);
            factories.put(type, new ObjectFactory<>(realization, this));

            singletonInstances.remove(type);

            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public <T> T get(@NotNull Class<T> type) {
        checkShutdown();

        try {
            lock.readLock().lock();

            if (singletonInstances.containsKey(type)) {
                return type.cast(singletonInstances.get(type));
            }

            if (scopedInstances.containsKey(type)) {
                return type.cast(scopedInstances.get(type));
            }

            ObjectFactory<?> factory = factories.get(type);
            if (factory == null) {
                return registerClass(type);
            }

            @SuppressWarnings("unchecked")
            T instance = (T) factory.create();

            singletonInstances.put(type, instance);

            return instance;
        } finally {
            lock.readLock().unlock();
        }
    }

    private <T> T createInstanceWithDependencies(@NotNull Class<T> type) {
        try {
            Constructor<T> constructor = findSuitableConstructor(type);
            constructor.setAccessible(true);

            Parameter[] parameters = constructor.getParameters();
            Object[] args = new Object[parameters.length];

            for (int i = 0; i < parameters.length; i++) {
                Class<?> paramType = parameters[i].getType();
                args[i] = get(paramType);
            }

            T instance = constructor.newInstance(args);
            initializeInstance(instance);

            return instance;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(
                    "No suitable constructor found for " + type.getName(), e
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to create instance of " + type.getName(), e
            );
        }
    }

    private <T> Constructor<T> findSuitableConstructor(@NotNull Class<T> type)
            throws NoSuchMethodException {

        Constructor<T>[] constructors = (Constructor<T>[]) type.getDeclaredConstructors();

        for (Constructor<T> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }

        if (constructors.length > 0) {
            return constructors[0];
        }

        throw new NoSuchMethodException("No constructors found in " + type.getName());
    }

    @Override
    public boolean shutdown() {
        if (isShutdown) {
            return false;
        }

        try {
            lock.writeLock().lock();
            isShutdown = true;

            for (Object instance : singletonInstances.values()) {
                if (instance instanceof AutoCloseable) {
                    try {
                        ((AutoCloseable) instance).close();
                    } catch (Exception ignored) {}
                }
                if(instance instanceof Shutdownable) {
                    try {
                        ((Shutdownable) instance).shutdown();
                    } catch (Exception ignored) {

                    }
                }
            }

            singletonInstances.clear();
            typeMappings.clear();
            factories.clear();
            scopedInstances.clear();

            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean reload() {
        if (isShutdown) {
            return false;
        }

        try {
            lock.writeLock().lock();

            Map<Class<?>, Class<?>> oldMappings = new HashMap<>(typeMappings);
            Map<Class<?>, ObjectFactory<?>> oldFactories = new HashMap<>(factories);

            singletonInstances.clear();
            scopedInstances.clear();

            typeMappings.putAll(oldMappings);
            factories.putAll(oldFactories);

            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private <T> void initializeInstance(@NotNull T instance) {
        try {
            if(instance instanceof Initializable e) {
                e.inititalize();
            }
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Error initializing instance of " + instance.getClass().getName(), e
            );
        }
    }

    private void checkShutdown() {
        if (isShutdown) {
            throw new IllegalStateException("Dependency container is shutdown");
        }
    }

    public void beginScope() {
        checkShutdown();

        try {
            lock.writeLock().lock();
            scopedInstances.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void endScope() {
        checkShutdown();

        try {
            lock.writeLock().lock();
            scopedInstances.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public <T> T getScoped(@NotNull Class<T> type) {
        checkShutdown();

        try {
            lock.readLock().lock();

            if (scopedInstances.containsKey(type)) {
                return type.cast(scopedInstances.get(type));
            }

            T instance = get(type);
            scopedInstances.put(type, instance);

            return instance;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isRegistered(@NotNull Class<?> type) {
        try {
            lock.readLock().lock();
            return typeMappings.containsKey(type) || singletonInstances.containsKey(type);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<Class<?>, Class<?>> getRegistrations() {
        try {
            lock.readLock().lock();
            return new HashMap<>(typeMappings);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void clearCache() {
        checkShutdown();

        try {
            lock.writeLock().lock();
            singletonInstances.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}