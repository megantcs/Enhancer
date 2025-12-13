package ru.megantcs.enhancer.platform.toolkit.api.deleters;

import ru.megantcs.enhancer.platform.toolkit.api.Deleter;

import java.lang.reflect.Field;

public class NullValuesDeleter implements Deleter {

    @Override
    public void delete(Object sender, Field field) throws IllegalAccessException {
        field.set(sender, null);
    }
}
