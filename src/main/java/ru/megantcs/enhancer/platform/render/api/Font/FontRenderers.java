package ru.megantcs.enhancer.platform.render.api.Font;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class FontRenderers {
    public static FontRenderer settings;
    public static FontRenderer modules;
    public static FontRenderer categories;
    public static FontRenderer icons;
    public static FontRenderer mid_icons;
    public static FontRenderer big_icons;
    public static FontRenderer thglitch;
    public static FontRenderer thglitchBig;
    public static FontRenderer monsterrat;
    public static FontRenderer sf_bold;
    public static FontRenderer sf_bold_mini;
    public static FontRenderer sf_bold_micro;
    public static FontRenderer sf_medium;
    public static FontRenderer sf_medium_mini;
    public static FontRenderer sf_medium_modules;
    public static FontRenderer minecraft;

    public static FontRenderer getSettingsRenderer() {
        return settings;
    }

    public static FontRenderer getModulesRenderer() {
        return modules;
    }
    
    public static @NotNull FontRenderer create(float size, String name) throws IOException, FontFormatException {
        return new FontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(FontRenderers.class.getClassLoader().getResourceAsStream("assets/enchancer/fonts/" + name + ".ttf"))).deriveFont(Font.PLAIN, size / 2f), size / 2f);
    }
}
