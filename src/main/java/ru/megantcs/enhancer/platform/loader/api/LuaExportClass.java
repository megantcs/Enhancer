package ru.megantcs.enhancer.platform.loader.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LuaExportClass {
    String name() default "";

    boolean autoMethods() default true;
    boolean autoFields() default true;
}