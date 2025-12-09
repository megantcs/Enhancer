package ru.megantcs.enhancer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import ru.megantcs.enhancer.platform.Resolver.ResolverClient;
import ru.megantcs.enhancer.platform.loader.LuaLoader;
import ru.megantcs.enhancer.platform.loader.LuaPreprocessor;
import ru.megantcs.enhancer.platform.loader.api.LuaMethod;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsContext;
import ru.megantcs.enhancer.platform.render.api.Graphics.GraphicsSystem;
import ru.megantcs.enhancer.platform.toolkit.Colors.ColorConvertor;
import ru.megantcs.enhancer.platform.toolkit.Events.interfaces.Action;
import ru.megantcs.enhancer.platform.toolkit.Fabric.CommandBuilder;
import ru.megantcs.enhancer.platform.toolkit.Fabric.ModEntrypoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Enhancer extends ModEntrypoint
{

    public static class DrawCluster {
        private static List<Action<DrawContext>> draws = new ArrayList<>();

        public static void execute(Action<DrawContext> draw) {
            draws.add(draw);
        }

        public static void clearAll() {
            draws.clear();
        }

        public static void drawAll() {
            HudRenderCallback.EVENT.register((e,t)->{
            for(var draw : draws)
                draw.invoke(e);
            });
        }
    }

    public static class RenderModule
    {
        @LuaMethod
        public void renderDrawRect(float x1, float y1, float x2, float y2, String hex)
        {
            DrawCluster.execute((e)->{
                GraphicsContext.rect(e.getMatrices(), x1, y1, x2,y2, ColorConvertor.hexToColor(hex));
            });
        }
    }

    public Enhancer() {
        super("Enhancer");
    }

    @Override
    public void bootstrap()
    {
        ResolverClient client = new ResolverClient();

        client.register(LuaLoader.class);
        client.initializeAll();

        var ll = client.get(LuaLoader.class);
        ll.useDebug();
        var p = ll.usePreprocessor();

        CommandBuilder.create("luaEnc").orArg("reload", (e)->{
            DrawCluster.clearAll();
            ll.registerModule("rm",new RenderModule());
            ll.loadFile(combinePath("test.lua"));
            e.getSource().sendFeedback(Text.of("success reload"));
        }).register();

        DrawCluster.drawAll();
    }
}
