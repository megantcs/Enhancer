package ru.megantcs.enhancer.platform.loader.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.interfaces.Returnable;
import ru.megantcs.enhancer.platform.loader.LuaEngine;
import ru.megantcs.enhancer.platform.toolkit.api.FinishedFieldReflection;

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
            FinishedFieldReflection.finishObj(this);
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

    protected final void log(String message) {
        LOGGER.info(message);
    }

    protected final void logOr(String message, boolean expression) {
        if(expression) log(message);
    }

    protected final void logOr(String message, Returnable<Boolean> expression) {
        if(expression.get()) log(message);
    }
}