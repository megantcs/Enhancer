package ru.megantcs.enhancer.platform.loader.libraries;

import net.minecraft.entity.Entity;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

@LuaExportClass(name = "Entity")
public class LEntity
{
    private final Entity entity;

    public LEntity(Entity entity) {
        this.entity = entity;
    }

    @LuaExportMethod
    public String getDisplayName() {
        return entity.getDisplayName().getString();
    }

    @LuaExportMethod
    public String getName() {
        return entity.getName().getString();
    }
}
