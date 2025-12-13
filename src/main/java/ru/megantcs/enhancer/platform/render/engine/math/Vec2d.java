package ru.megantcs.enhancer.platform.render.engine.math;

import ru.megantcs.enhancer.platform.loader.api.*;

@LuaTableExportClass
@LuaExportClass(name = "Vec3d")
public class Vec2d
{
    @LuaExportField
    @LuaTableExportField
    public double x;

    @LuaExportField
    @LuaTableExportField
    public double y;

    public Vec2d() {
        this(0,0);
    }

    @LuaExportMethod(name = "create")
    public Vec2d(float x, float y) {
        this.x = x;
        this.y = y;}
}