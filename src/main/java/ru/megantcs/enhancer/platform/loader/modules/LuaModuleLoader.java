package ru.megantcs.enhancer.platform.loader.modules;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.loader.LuaLoader;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Deprecated
public class LuaModuleLoader
{
    static final Logger LOGGER = LoggerFactory.getLogger(LuaModuleLoader.class);

    private final Set<LuaModule> modules;
    private final LuaLoader luaLoader;

    public LuaModuleLoader(@NotNull LuaLoader lua) {
        modules = new HashSet<>();
        luaLoader = Objects.requireNonNull(lua);
    }

    public void registerModule(final @NotNull
                               LuaModule module) {
        modules.add(Objects.requireNonNull(module));
    }

    public boolean unregisterModule(final @NotNull
                                    LuaModule module) {
        return modules.remove(Objects.requireNonNull(module));
    }

    public void load()
    {
        modules.forEach((module)->
        {
            try {
                LOGGER.info("init module: module@" + module.getClass());
                module.moduleInit(null);

                luaLoader.registerModule(Objects.
                        requireNonNull(module.moduleName()), module);
            } catch (Exception e) {
                LOGGER.error("error load module: " + (module.moduleName() == null ? "null@" + module.getClass() : module.moduleName()), e);
            }
        });
    }
}
