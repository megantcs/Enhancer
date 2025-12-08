package ru.megantcs.enhancer.platform.toolkit.Colors;

import java.awt.*;

public class ColorConvertor
{
    public static Color hexToColor(String hex) {
        if (hex == null || hex.isEmpty()) {
            return Color.BLACK;
        }

        String cleanHex = hex.startsWith("#") ? hex.substring(1) : hex;

        try {
            switch (cleanHex.length()) {
                case 3:
                    cleanHex = "" +
                            cleanHex.charAt(0) + cleanHex.charAt(0) +
                            cleanHex.charAt(1) + cleanHex.charAt(1) +
                            cleanHex.charAt(2) + cleanHex.charAt(2);
                case 6:
                    int rgb = Integer.parseInt(cleanHex, 16);
                    return new Color(
                            (rgb >> 16) & 0xFF,
                            (rgb >> 8) & 0xFF,
                            rgb & 0xFF
                    );

                case 8:
                    long rgba = Long.parseLong(cleanHex, 16);
                    return new Color(
                            (int) ((rgba >> 24) & 0xFF),
                            (int) ((rgba >> 16) & 0xFF),
                            (int) ((rgba >> 8) & 0xFF),
                            (int) (rgba & 0xFF)
                    );

                case 4:
                    cleanHex = "" +
                            cleanHex.charAt(0) + cleanHex.charAt(0) +
                            cleanHex.charAt(1) + cleanHex.charAt(1) +
                            cleanHex.charAt(2) + cleanHex.charAt(2) +
                            cleanHex.charAt(3) + cleanHex.charAt(3);
                    rgba = Long.parseLong(cleanHex, 16);
                    return new Color(
                            (int) ((rgba >> 24) & 0xFF),
                            (int) ((rgba >> 16) & 0xFF),
                            (int) ((rgba >> 8) & 0xFF),
                            (int) (rgba & 0xFF)
                    );

                default:
                    throw new IllegalArgumentException("Invalid HEX color format: " + hex);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid HEX color: " + hex, e);
        }
    }
}
