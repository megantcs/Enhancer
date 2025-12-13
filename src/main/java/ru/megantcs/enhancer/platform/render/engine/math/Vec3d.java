package ru.megantcs.enhancer.platform.render.engine.math;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import ru.megantcs.enhancer.platform.loader.api.*;

@LuaTableExportClass
@LuaExportClass(name = "Vec3d")
public class Vec3d
{
    @LuaExportField @LuaTableExportField public double x;
    @LuaExportField @LuaTableExportField public double y;
    @LuaExportField @LuaTableExportField public double z;

    public Vec3d() {
        this(0,0,0);
    }

    @LuaExportMethod(name = "create")
    public Vec3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
