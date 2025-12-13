package ru.megantcs.enhancer.platform.loader.libraries;

import ru.megantcs.enhancer.platform.loader.api.*;

/**
 * update libraries see {@link ru.megantcs.enhancer.platform.render.engine.common.luaLibraries}
 */
@Deprecated
@LuaTableExportClass
@LuaExportClass(name = "CornerRadius")
public class LibraryCornerRadius
{
    @LuaExportField
    @LuaTableExportField public float r1;

    @LuaExportField
    @LuaTableExportField public float r2;

    @LuaExportField
    @LuaTableExportField public float r3;

    @LuaExportField
    @LuaTableExportField public float r4;

    @SuppressWarnings("unused")
    public LibraryCornerRadius()
    {
        r1 = 0; r2 = 0; r3 = 0; r4 = 0;
    }

    @SuppressWarnings("unused")
    @LuaExportMethod(name = "create4f")
    public LibraryCornerRadius(float r1, float r2, float r3, float r4)
    {
        this.r1 = r1;
        this.r2 = r2;
        this.r4 = r3;
        this.r3 = r4;
    }
}
