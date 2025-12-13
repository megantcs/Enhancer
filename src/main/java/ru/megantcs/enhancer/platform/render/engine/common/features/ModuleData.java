package ru.megantcs.enhancer.platform.render.engine.common.features;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleData
{
    String name();
    String version() default "0.0.1";
    String author() default "(null)";
}
