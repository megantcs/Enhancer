package ru.megantcs.enhancer.platform.loader.libraries;

import net.minecraft.entity.Entity;
import ru.megantcs.enhancer.platform.loader.api.LuaExportClass;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;

/**
 * update libraries see {@link ru.megantcs.enhancer.platform.render.engine.common.luaLibraries}
 */
@Deprecated
@LuaExportClass(name = "Entity")
public class LibraryEntity
{
    private final Entity entity;

    public LibraryEntity(Entity entity) {
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
