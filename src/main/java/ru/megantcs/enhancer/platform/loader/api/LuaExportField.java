package ru.megantcs.enhancer.platform.loader.api;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LuaField {
    boolean read() default true;
    boolean write() default true;
}