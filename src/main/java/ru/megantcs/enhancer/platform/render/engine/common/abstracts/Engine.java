package ru.megantcs.enhancer.platform.render.engine.common.abstracts;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.loader.LuaScriptManager;
import ru.megantcs.enhancer.platform.render.engine.common.features.Module;
import ru.megantcs.enhancer.platform.render.engine.common.features.impl.BaseRendererModule;
import ru.megantcs.enhancer.platform.render.engine.common.features.impl.FontRenderModule;
import ru.megantcs.enhancer.platform.render.engine.common.features.impl.TextureRendererModule;
import ru.megantcs.enhancer.platform.render.engine.common.storage.LocalStorage;
import ru.megantcs.enhancer.platform.toolkit.Dependency.api.Lifecycle;
import ru.megantcs.enhancer.platform.toolkit.api.FinishedFieldReflection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Engine implements Lifecycle
{
    private final Logger LOGGER = LoggerFactory.getLogger("Engine");
    private List<Module> modules;
    protected final LuaScriptManager scriptManager;
    protected final LocalStorage localStorage = new LocalStorage("LocalStorage");

    public Engine(@NotNull LuaScriptManager scriptManager) {
        this.scriptManager = Objects.requireNonNull(scriptManager);
    }

    protected final <T extends Module> T addModule(@NotNull
                          T module) {
        modules.add(Objects.requireNonNull(
                module.setLocalStorage(localStorage).toggle(), "module cannot be is null"));
        LOGGER.info("add module: {}", module.getModuleInfo().toString());
        return module;
    }

    protected final boolean removeModule(@NotNull
                                Module module) {
        var state = modules.remove(Objects.requireNonNull(module, "module cannot be is null"));

        if(state) LOGGER.info("remove module: {}", module.getModuleInfo().toString());
        else
        {
            LOGGER.error("error remove module: {}", module.getModuleInfo().toString());
        }
        return state;
    }

    @SuppressWarnings("unchecked")
    protected <T extends Module> Optional<T> getModule(Class<T> klass)
    {
        if (klass == null) {
            return Optional.empty();
        }

        for (Module module : modules) {
            if (klass.isInstance(module)) {
                return Optional.of((T) module);
            }
        }

        return Optional.empty();
    }

    protected final boolean containsModule(@NotNull
                                  Module module) {
        return modules.contains(Objects.requireNonNull(module, "module cannot be is null"));
    }

    @Override
    public final void inititalize()
    {
        modules = new ArrayList<>();

        addModule(new BaseRendererModule(
                addModule(new TextureRendererModule()),
                addModule(new FontRenderModule())));

        init();

        LOGGER.info("initialize finished");
    }

    @Override
    public final void shutdown()
    {
        LOGGER.info("shutdown");

        dispose();
        FinishedFieldReflection.finishObj(this);
    }

    public LuaScriptManager getScriptManager() {
        return scriptManager;
    }

    /** events **/
    protected void init() {}
    protected void dispose() {}
}
