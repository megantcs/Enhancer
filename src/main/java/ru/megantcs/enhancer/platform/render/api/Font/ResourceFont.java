package ru.megantcs.enhancer.platform.render.api.Font;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class ResourceFont implements FontRender
{
    private final String path;
    private final String namespace;
    private final int size;

    private FontRenderer fontRenderer;

    public ResourceFont(String namespace, String path, int size) {
        this.path = path;
        this.namespace = namespace;
        this.size = size;

        create();
    }

    private void create() {
        try {
           fontRenderer = new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(FontRenderers.class.getClassLoader().getResourceAsStream("assets/" + namespace  +"/" + path))).deriveFont(Font.PLAIN, size / 2f), size / 2f);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }
}
