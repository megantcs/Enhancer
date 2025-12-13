package ru.megantcs.enhancer.platform.render.engine.common.luaLibraries;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

@LuaExportClass(name = "Minecraft")
public class LuaMinecraft
{
    public static LuaMinecraft getInstance() {
        return new LuaMinecraft();
    }

    public LuaPlayer getPlayer()
    {
        return new LuaPlayer(Minecraft.mc.player);
    }
}
