package ru.megantcs.enhancer;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import ru.megantcs.enhancer.platform.Resolver.ResolverClient;
import ru.megantcs.enhancer.platform.loader.LuaEngine;
import ru.megantcs.enhancer.platform.loader.LuaScriptManager;
import ru.megantcs.enhancer.platform.loader.libraries.*;
import ru.megantcs.enhancer.platform.loader.modules.impl.FabricEventsModule;
import ru.megantcs.enhancer.platform.loader.modules.impl.MixinsModule;
import ru.megantcs.enhancer.platform.render.api.Font.FontRenderer;
import ru.megantcs.enhancer.platform.render.api.Font.FontRenderers;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsContext;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsSystem;
import ru.megantcs.enhancer.platform.render.api.Shaders.BlurShader;
import ru.megantcs.enhancer.platform.render.api.Shaders.RectangleShader;
import ru.megantcs.enhancer.platform.render.engine.EnhancerEngine;
import ru.megantcs.enhancer.platform.render.engine.math.Vec3d;
import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;
import ru.megantcs.enhancer.platform.toolkit.Colors.Brush;
import ru.megantcs.enhancer.platform.toolkit.Fabric.CommandBuilder;
import ru.megantcs.enhancer.platform.toolkit.Fabric.ModEntrypoint;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class Enhancer extends ModEntrypoint
{
    private LuaScriptManager scriptManager;
    public Enhancer() {
        super("Enhancer");
    }

    boolean p = false;
    @Override
    public void bootstrap()
    {
        var scriptManager = new LuaScriptManager();

        scriptManager.loadModule(new FabricEventsModule());
        scriptManager.loadModule(new MixinsModule());

        LOGGER.info("load file: {}",scriptManager.loadFile(combinePath("config/enhancer/main.lua")));

        CommandBuilder.create("luaEnc")
                .orArg("reload", (e) -> {
                    scriptManager.reload();
                    e.getSource().sendFeedback(Text.of("Scripts reloaded successfully"));
                })
                .orArg("loadScript", null).thenArg("scriptPath", (e)->{
                    String path = combinePath("config/enhancer/" + StringArgumentType.getString(e, "scriptPath"));
                    var state = scriptManager.loadFile(path);

                    e.getSource().sendFeedback(Text.of(state? "Scripts load successfully" : "Scripts load error"));
                })
                .orArg("clear", (e) -> {
                    scriptManager.shutdown();
                    e.getSource().sendFeedback(Text.of("All scripts cleared"));
                })
                .register();

        EnhancerEngine enhancerEngine = new EnhancerEngine(scriptManager);
        enhancerEngine.inititalize();
    }
}