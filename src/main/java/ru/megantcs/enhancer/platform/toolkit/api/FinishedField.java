package ru.megantcs.enhancer.platform.toolkit.api;

import ru.megantcs.enhancer.platform.toolkit.api.deleters.NullValuesDeleter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FinishedField
{
    Class<? extends Deleter> deleter() default NullValuesDeleter.class;
}
