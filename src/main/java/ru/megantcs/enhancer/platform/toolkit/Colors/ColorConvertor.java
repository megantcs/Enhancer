package ru.megantcs.enhancer.platform.toolkit.Colors;

import java.awt.*;
import java.util.Objects;

public class ColorConvertor
{
    public static String withAlpha(String hexColor, float alpha) {
        if (hexColor == null || hexColor.isEmpty()) {
            return hexColor;
        }

        String color = hexColor.startsWith("#") ? hexColor : "#" + hexColor;

        int alphaValue = Math.max(0, Math.min(255, (int)(alpha * 255)));
        String alphaHex = String.format("%02X", alphaValue);

        if (color.length() == 9) {
            return "#" + alphaHex + color.substring(3);
        }
        else if (color.length() == 7) {
            return "#" + alphaHex + color.substring(1);
        }
        else if (color.length() == 4) {
            String r = color.substring(1, 2);
            String g = color.substring(2, 3);
            String b = color.substring(3, 4);
            return "#" + alphaHex + r + r + g + g + b + b;
        }
        else if (color.length() == 5) {
            String a = color.substring(1, 2);
            String r = color.substring(2, 3);
            String g = color.substring(3, 4);
            String b = color.substring(4, 5);
            return "#" + alphaHex + r + r + g + g + b + b;
        }

        return color;
    }

    public static Color hexToColor(String hex)
    {
        if(Objects.equals(hex, "white")) return Color.white;
        if(Objects.equals(hex, "red")) return Color.red;

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
