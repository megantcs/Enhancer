package ru.megantcs.enhancer.platform.loader.modules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LuaModuleData {
    String name();
    String version() default "1.0.0";
    String description() default "";
}
