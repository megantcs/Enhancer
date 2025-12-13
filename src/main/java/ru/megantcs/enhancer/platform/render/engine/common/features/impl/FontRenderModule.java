package ru.megantcs.enhancer.platform.render.engine.common.features.impl;

import net.minecraft.client.util.math.MatrixStack;
import ru.megantcs.enhancer.platform.render.api.Font.FontRenderer;
import ru.megantcs.enhancer.platform.render.engine.common.features.Module;
import ru.megantcs.enhancer.platform.render.engine.common.features.ModuleData;
import ru.megantcs.enhancer.platform.render.engine.render.RenderObject;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@ModuleData(name = "Font Render", author = "megantcs", version = "1.0.0-latest")
public class FontRenderModule extends Module
{
    public static FontRenderer create(float size, String path) throws IOException, FontFormatException {
        File fontFile = new File(path);
        if (!fontFile.exists()) {
            throw new FileNotFoundException("Font file not found: " + path);
        }

        Font baseFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
        Font sizedFont = baseFont.deriveFont(Font.PLAIN, size / 2f);

        return new FontRenderer(sizedFont, size / 2f);
    }

    private Map<String, FontRenderer> fonts;

    @Override
    protected void onInitialize() {
        fonts = new HashMap<>();
    }

    private FontRenderer getFont(String fontPath, float size) {
        if(fonts.containsKey(fontPath + "@" + size)) {
            return fonts.get(fontPath + "@" + size);
        }

        try {
            var font = create(size, fontPath);
            fonts.put(fontPath + "@" + size, font);
            logger.info("build new font: {}", fontPath + "@" + size);

            return font;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (FontFormatException e) {
            logger.error("error create font {}, {}", fontPath, size);
            return  null;
        }
    }

    public boolean drawText(MatrixStack matrixStack, String fontPath, float size, float x, float y, String text, Color color)
    {
        var font = getFont(fontPath, size);
        if(font == null) return false;

        font.drawString(matrixStack, text, x, y, color.getRGB());

        return true;
    }
}
