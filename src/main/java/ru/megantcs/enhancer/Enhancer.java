package ru.megantcs.enhancer;

import net.minecraft.text.Text;
import ru.megantcs.enhancer.platform.Resolver.ResolverClient;
import ru.megantcs.enhancer.platform.loader.LuaEngine;
import ru.megantcs.enhancer.platform.loader.LuaScriptManager;
import ru.megantcs.enhancer.platform.loader.libraries.*;
import ru.megantcs.enhancer.platform.loader.modules.impl.FabricEventsModule;
import ru.megantcs.enhancer.platform.loader.modules.impl.MixinsModule;
import ru.megantcs.enhancer.platform.toolkit.Fabric.CommandBuilder;
import ru.megantcs.enhancer.platform.toolkit.Fabric.ModEntrypoint;

public class Enhancer extends ModEntrypoint
{
    private LuaScriptManager scriptManager;

    public Enhancer() {
        super("Enhancer");
    }

    @Override
    public void bootstrap() {
        ResolverClient client = new ResolverClient();
        client.register(LuaScriptManager.class);
        client.initializeAll();

        var scriptManager = new LuaScriptManager();

        LuaEngine.INSTANCE.registerClass(LibraryBufferBuilder.class);
        LuaEngine.INSTANCE.registerClass(LibraryTesselator.class);
        LuaEngine.INSTANCE.registerClass(LibraryRenderSystem.class);
        LuaEngine.INSTANCE.registerClass(LibraryRenderUtil.class);
        LuaEngine.INSTANCE.registerClass(LibraryMinecraft.class);
        LuaEngine.INSTANCE.registerClass(LibraryEntity.class);
        LuaEngine.INSTANCE.registerClass(LibraryWindow.class);

        scriptManager.loadModule(new FabricEventsModule());
        scriptManager.loadModule(new MixinsModule());

        LOGGER.error("load file: {}",scriptManager.loadFile(combinePath("test.lua")));

        CommandBuilder.create("luaEnc")
                .orArg("reload", (e) -> {
                    scriptManager.reload();
                    e.getSource().sendFeedback(Text.of("Scripts reloaded successfully"));
                })
                .orArg("clear", (e) -> {
                    scriptManager.shutdown();
                    e.getSource().sendFeedback(Text.of("All scripts cleared"));
                })
                .register();
    }
}