package ru.megantcs.enhancer.platform.loader.modules.impl;

import org.luaj.vm2.LuaValue;
import ru.megantcs.enhancer.platform.hook.ScoreboardRenderHook;
import ru.megantcs.enhancer.platform.interfaces.Func;
import ru.megantcs.enhancer.platform.loader.LuaEngine;
import ru.megantcs.enhancer.platform.loader.modules.LuaModule;
import ru.megantcs.enhancer.platform.loader.modules.LuaModuleData;

@LuaModuleData(name = "Mixins")
public class MixinsModules extends LuaModule
{
    private Runnable chunkExecute;
    private LuaEngine engine;

    private LuaValue scoreboard$background;
    private LuaValue scoreboard$separator;
    private LuaValue scoreboard$header;

    @Override
    protected void init(LuaEngine engine)
    {
        if(chunkExecute == null)
            chunkExecute = this::chunkExecuteEvent;

        engine.onLoadedScript.register(chunkExecute);
        updateHooks();
        this.engine = engine;
    }

    @Override
    protected void cleanup() {
        if(engine == null) return;
        if(chunkExecute != null)
            engine.onLoadedScript.unregister(chunkExecute);

        ScoreboardRenderHook.RENDER_HEADER.unregister("scoreboard$background");
        ScoreboardRenderHook.RENDER_SEPARATOR.unregister("scoreboard$separator");
        ScoreboardRenderHook.RENDER_SEPARATOR.unregister("scoreboard$header");
    }

    private void updateHooks()
    {
        ScoreboardRenderHook.RENDER_BACKGROUND.register((data)->{
            if(scoreboard$background == null) return false;
            try {
                scoreboard$background.call();
                return true;
            } catch (RuntimeException e) {
                LOGGER.error("error hook", e);
            }
            return true;
        }, "scoreboard$background");

        ScoreboardRenderHook.RENDER_SEPARATOR.register((data)->{
            if(scoreboard$separator == null) return false;
            try {
                scoreboard$separator.call();
                return true;
            } catch (RuntimeException e) {
                LOGGER.error("error hook", e);
            }
            return true;
        }, "scoreboard$separator");

        ScoreboardRenderHook.RENDER_HEADER.register((data)->{
            if(scoreboard$header == null) return false;
            try {
                scoreboard$header.call();
                return true;
            } catch (RuntimeException e) {
                LOGGER.error("error hook", e);
            }
            return true;
        }, "scoreboard$header");
    }

    private void chunkExecuteEvent()
    {
        loadExecuteEvents();
    }

    private void loadExecuteEvents()
    {
        scoreboard$background = engine.getMethod("scoreboard$background");
        scoreboard$separator  = engine.getMethod("scoreboard$separator");
        scoreboard$header     = engine.getMethod("scoreboard$header");

        logOr("mixin found scoreboard$background", scoreboard$background != null);
        logOr("mixin found scoreboard$separator", scoreboard$separator != null);
        logOr("mixin found scoreboard$header", scoreboard$header != null);
    }
}
