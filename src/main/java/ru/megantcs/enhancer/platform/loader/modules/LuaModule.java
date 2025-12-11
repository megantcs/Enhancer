package ru.megantcs.enhancer.platform.loader.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.loader.LuaEngine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


public abstract class LuaModule {
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    protected void init() {}
    protected void init(LuaEngine engine) {}

    protected void cleanup() {}

    public String moduleName() {
        LuaModuleData data = getClass().getAnnotation(LuaModuleData.class);
        return data != null ? data.name() : getClass().getSimpleName().toLowerCase();
    }

    public final void moduleInit(LuaEngine engine) {
        try {
            init();
            if(engine != null) init(engine);
        } catch (Exception e) {
            LOGGER.error("Module initialization failed: {}", getClass().getName(), e);
            throw e;
        }
    }

    public final void moduleCleanup() {
        try {
            cleanup();
        } catch (Exception e) {
            LOGGER.warn("Module cleanup failed: {}", getClass().getName(), e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Finalizing module: {}", getClass().getName());
            }
        } finally {
            super.finalize();
        }
    }
}