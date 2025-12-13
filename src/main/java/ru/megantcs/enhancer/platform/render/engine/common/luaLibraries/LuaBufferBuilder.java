package ru.megantcs.enhancer.platform.render.engine.common.luaLibraries;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import ru.megantcs.enhancer.platform.loader.api.LuaExportMethod;
import ru.megantcs.enhancer.platform.loader.libraries.LibraryBufferBuilder;
import ru.megantcs.enhancer.platform.toolkit.Colors.ColorConvertor;

import java.awt.*;

import static ru.megantcs.enhancer.platform.loader.modules.impl.FabricEventsModule.MATRIX_STACK_INSTANCE;

public class LuaBufferBuilder
{
    @LuaExportMethod
    public void begin(int modeCode, int shaderCode) {
        VertexFormat.DrawMode mode = getDrawMode(modeCode);
        VertexFormat format = getVertexFormat(shaderCode);

        Tessellator.getInstance().getBuffer().begin(mode, format);
    }

    private VertexFormat.DrawMode getDrawMode(int code) {
        switch (code) {
            case 0: return VertexFormat.DrawMode.LINES;
            case 1: return VertexFormat.DrawMode.LINE_STRIP;
            case 2: return VertexFormat.DrawMode.DEBUG_LINES;
            case 3: return VertexFormat.DrawMode.DEBUG_LINE_STRIP;
            case 4: return VertexFormat.DrawMode.TRIANGLES;
            case 5: return VertexFormat.DrawMode.TRIANGLE_STRIP;
            case 6: return VertexFormat.DrawMode.TRIANGLE_FAN;
            case 7: return VertexFormat.DrawMode.QUADS;
            default: return VertexFormat.DrawMode.QUADS;
        }
    }

    private VertexFormat getVertexFormat(int code) {
        switch (code) {
            case 0: return VertexFormats.POSITION;
            case 1: return VertexFormats.POSITION_COLOR;
            case 2: return VertexFormats.POSITION_TEXTURE;
            case 3: return VertexFormats.POSITION_COLOR_TEXTURE;
            case 4: return VertexFormats.POSITION_TEXTURE_COLOR;
            case 5: return VertexFormats.POSITION_COLOR_LIGHT;
            case 6: return VertexFormats.POSITION_TEXTURE_COLOR_LIGHT;
            case 7: return VertexFormats.POSITION_COLOR_TEXTURE_LIGHT;
            case 8: return VertexFormats.POSITION_TEXTURE_LIGHT_COLOR;
            default: return VertexFormats.POSITION_COLOR;
        }
    }

    @LuaExportMethod
    public LuaBufferBuilder vertex(float x, float y, float z, String hex) {
        try {
            Color color = ColorConvertor.hexToColor(hex);
            Tessellator.getInstance().getBuffer()
                    .vertex(MATRIX_STACK_INSTANCE.peek().getPositionMatrix(), x, y, z)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
                    .next();
        } catch (Exception e) {
            Tessellator.getInstance().getBuffer()
                    .vertex(MATRIX_STACK_INSTANCE.peek().getPositionMatrix(), x, y, z)
                    .next();
        }
        return this;
    }
}
