package ru.megantcs.enhancer.platform.loader;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.loader.modules.LuaModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class LuaScriptManager {
    private final Logger LOGGER = LoggerFactory.getLogger(LuaScriptManager.class);
    private final LuaEngine engine;
    private final List<LuaModule> modules;
    private final List<String> loadedFiles = new ArrayList<>();

    public LuaScriptManager() {
        engine = LuaEngine.INSTANCE;
        modules = new CopyOnWriteArrayList<>();
    }

    public boolean loadScript(@NotNull String code, @NotNull String scriptName) {
        return engine.loadScript(
                Objects.requireNonNull(code),
                Objects.requireNonNull(scriptName));
    }

    public boolean loadFile(@NotNull String filename) {
        boolean result = engine.loadFile(Objects.requireNonNull(filename));
        loadedFiles.add(filename);

        return result;
    }

    public void shutdown() {
        modules.forEach(LuaModule::moduleCleanup);
        modules.clear();
        loadedFiles.clear();
    }

    public void reload() {
        List<Class<? extends LuaModule>> moduleClasses = new ArrayList<>();
        for (LuaModule module : modules) {
            moduleClasses.add(module.getClass());
        }

        List<String> filesToReload = new ArrayList<>(loadedFiles);

        shutdown();

        engine.reloadEnvironment();

        for (Class<? extends LuaModule> moduleClass : moduleClasses) {
            try {
                LuaModule newInstance = moduleClass.getDeclaredConstructor().newInstance();
                loadModule(newInstance);
            } catch (Exception e) {
                LOGGER.error("Failed to recreate module: " + moduleClass.getName(), e);
            }
        }

        for (String file : filesToReload) {
            loadFile(file);
        }
    }

    public void loadModule(@NotNull LuaModule module) {
        Objects.requireNonNull(module);

        if (handleModule(module)) {
            module.moduleInit(engine);
            if (!modules.contains(module)) {
                modules.add(module);
            }
        } else {
            LOGGER.error("error handle module: " + module.moduleName());
        }
    }

    public List<@NotNull LuaModule> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public List<@NotNull LuaModule> getCopyModules() {
        return List.copyOf(modules);
    }

    private boolean handleModule(LuaModule module) {
        try {
            var moduleName = Objects.requireNonNull(module.moduleName());
            return engine.registerModule(moduleName, module);
        } catch (Exception e) {
            LOGGER.error("handle module skip: " + module.moduleName(), e);
            return false;
        }
    }
}