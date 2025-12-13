package ru.megantcs.enhancer.platform.toolkit.api;

import java.lang.reflect.Field;

public interface Deleter
{
     void delete(Object sender, Field field) throws IllegalAccessException;
}
