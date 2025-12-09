package ru.megantcs.enhancer.platform.loader.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LuaMethod {
    String name() default "";
}