package ru.megantcs.enhancer.platform.loader.modules.impl;

import org.luaj.vm2.LuaValue;
import ru.megantcs.enhancer.platform.hook.ScoreboardRenderHook;
import ru.megantcs.enhancer.platform.loader.LuaEngine;
import ru.megantcs.enhancer.platform.loader.LuaTableInstance;
import ru.megantcs.enhancer.platform.loader.modules.LuaModule;
import ru.megantcs.enhancer.platform.loader.modules.LuaModuleData;
import ru.megantcs.enhancer.platform.toolkit.api.FinishedField;

@LuaModuleData(name = "Mixins")
public class MixinsModule extends LuaModule
{
    private Runnable chunkExecute;
    private LuaEngine engine;

    @FinishedField
    private LuaValue scoreboard$background;

    @FinishedField
    private LuaValue scoreboard$separator;

    @FinishedField
    private LuaValue scoreboard$header;

    @FinishedField
    private LuaValue scoreboard$end;

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
        ScoreboardRenderHook.RENDER_END.unregister("scoreboard$end");
    }

    private void updateHooks()
    {
        ScoreboardRenderHook.RENDER_END.register((data)->{
            if(scoreboard$end == null) return false;

            try {
                scoreboard$end.call();
                return true;
            } catch (RuntimeException e) {
                LOGGER.error("error hook", e);
            }

            return false;
        }, "scoreboard$end");
        ScoreboardRenderHook.RENDER_BACKGROUND.register((data)->{
            if(scoreboard$background == null) return false;
            try {
                scoreboard$background.call(
                        LuaTableInstance.generate(data));
                return true;
            } catch (RuntimeException e) {
                LOGGER.error("error hook", e);
            }
            return false;
        }, "scoreboard$background");

        ScoreboardRenderHook.RENDER_SEPARATOR.register((data)->{
            if(scoreboard$separator == null) return false;
            try {
                scoreboard$separator.call(
                        LuaTableInstance.generate(data));
                return true;
            } catch (RuntimeException e) {
                LOGGER.error("error hook", e);
            }
            return true;
        }, "scoreboard$separator");

        ScoreboardRenderHook.RENDER_HEADER.register((data)->{
            if(scoreboard$header == null) return false;
            try {
                scoreboard$header.call(
                        LuaTableInstance.generate(data));
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
        scoreboard$background = engine.getMethod("mixin@scoreboard$background");
        scoreboard$separator  = engine.getMethod("mixin@scoreboard$separator");
        scoreboard$header     = engine.getMethod("mixin@scoreboard$header");
        scoreboard$end        = engine.getMethod("mixin@scoreboard$end");

        logOr("mixin found scoreboard$background", scoreboard$background != null);
        logOr("mixin found scoreboard$separator", scoreboard$separator != null);
        logOr("mixin found scoreboard$header", scoreboard$header != null);
        logOr("mixin found scoreboard$end", scoreboard$end != null);

    }
}
