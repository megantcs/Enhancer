package ru.megantcs.enhancer.platform.toolkit.Colors;

import java.awt.*;

public class ColorConvertor
{
    public static String withAlpha(String hexColor, float alpha) {
        if (hexColor == null || hexColor.isEmpty()) {
            return hexColor;
        }

        // Обеспечиваем корректный формат
        String color = hexColor.startsWith("#") ? hexColor : "#" + hexColor;

        // Преобразуем alpha от 0.0-1.0 в 0-255
        int alphaValue = Math.max(0, Math.min(255, (int)(alpha * 255)));
        String alphaHex = String.format("%02X", alphaValue);

        // Если цвет уже имеет альфа-канал (#AARRGGBB)
        if (color.length() == 9) { // # + 8 символов
            return "#" + alphaHex + color.substring(3); // Заменяем первые 2 символа после #
        }
        // Если цвет без альфа-канала (#RRGGBB)
        else if (color.length() == 7) { // # + 6 символов
            return "#" + alphaHex + color.substring(1);
        }
        // Если короткий формат (#RGB)
        else if (color.length() == 4) { // # + 3 символа
            // Расширяем до полного формата
            String r = color.substring(1, 2);
            String g = color.substring(2, 3);
            String b = color.substring(3, 4);
            return "#" + alphaHex + r + r + g + g + b + b;
        }
        // Если короткий с альфа (#ARGB)
        else if (color.length() == 5) { // # + 4 символа
            String a = color.substring(1, 2);
            String r = color.substring(2, 3);
            String g = color.substring(3, 4);
            String b = color.substring(4, 5);
            return "#" + alphaHex + r + r + g + g + b + b;
        }

        // Если неизвестный формат, возвращаем как есть
        return color;
    }

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
