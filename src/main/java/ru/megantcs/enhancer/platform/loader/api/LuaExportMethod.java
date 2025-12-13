package ru.megantcs.enhancer.platform.loader.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface LuaExportMethod {
    String name() default "";
    boolean requireInit() default false;
}