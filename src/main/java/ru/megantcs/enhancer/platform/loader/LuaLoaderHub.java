package ru.megantcs.enhancer.platform.loader;

import ru.megantcs.enhancer.platform.interfaces.ResolveClient;
import ru.megantcs.enhancer.platform.loader.modules.LuaModule;
import ru.megantcs.enhancer.platform.loader.modules.LuaModuleLoader;
import ru.megantcs.enhancer.platform.toolkit.Events.RunnableEvent;

@Deprecated
public class LuaLoaderHub implements ResolveClient
{
    private final LuaLoader luaLoader;
    private final LuaModuleLoader moduleLoader;

    public final RunnableEvent onExecuteChunk = new RunnableEvent();

    public LuaLoaderHub() {
        luaLoader = new LuaLoader();
        moduleLoader = new LuaModuleLoader(luaLoader);
    }

    public void registerModule(LuaModule luaModule) {
        moduleLoader.registerModule(luaModule);
    }

    public void initializeAll() {
        luaLoader.useDebug();
        luaLoader.usePreprocessor();
        moduleLoader.load();
    }

    public void rebuildPreprocessor() {
        luaLoader.rebuildPreprocessor();
    }

    public boolean executeChunk(String code, String name) {
        onExecuteChunk.emit();
        return luaLoader.loadCode(code, name);
    }

    public boolean executeFile(String path) {
        onExecuteChunk.emit();
        return luaLoader.loadFile(path);
    }

    @Override
    public void shutdown() {
        luaLoader.shutdown();
    }

    @Override
    public void init() {
        luaLoader.init();
    }

    public LuaLoader loader() {
        return luaLoader;
    }
}
