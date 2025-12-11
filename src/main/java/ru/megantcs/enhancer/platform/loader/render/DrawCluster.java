package ru.megantcs.enhancer.platform.loader.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.megantcs.enhancer.platform.toolkit.Events.interfaces.Action;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Deprecated
public class DrawCluster
{
    public static DrawCluster INSTANCE = new DrawCluster();
    private final Logger LOGGER = LoggerFactory.getLogger(DrawCluster.class);

    private final Map<String, List<Action<Handler>>> children = new ConcurrentHashMap<>();

    public DrawCluster() {
        children.put("main", new CopyOnWriteArrayList<>());
    }

    public void add(Action<Handler> action) {
        children.get("main").add(action);
    }

    public boolean addGroup(String name, Action<Handler> action) {
        if(Objects.equals(name, "main"))
            return false;

        if(!children.containsKey(name)) {
            children.put(name, new CopyOnWriteArrayList<>());
        }

        children.get(name).add(action);
        return true;
    }

    public boolean clearGroup(String name) {
        if(!children.containsKey(name)) return false;

        children.get(name).clear();
        return true;
    }

    public void register() {
        HudRenderCallback.EVENT.register(((drawContext, tickDelta) -> {
            for(var child : children.values())
            {
                for(var render : child)
                {
                    try {
                        render.invoke(new Handler(drawContext, tickDelta));
                    } catch (RuntimeException e) {
                        LOGGER.error("HudRenderCallback error handler.", e);
                    }
                }
            }
        }));
    }

    public static record Handler(DrawContext drawContext, float delta) { }
}
