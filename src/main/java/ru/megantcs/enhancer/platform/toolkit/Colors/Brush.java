package ru.megantcs.enhancer.platform.toolkit.Colors;

import ru.megantcs.enhancer.platform.interfaces.Func;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Brush
{
    private final List<Color> colors
            = new ArrayList<>();

    public Brush() {}
    public Brush(Color mainColor, int count) {
        if(count <= 0) return;

        for(int i = 0; i < count; i++)
            colors.add(mainColor);
    }
    public Brush(Color... colors) {
        this.colors.addAll(List.of(colors));
    }

    public Brush(List<Color> colors) {
        this.colors.addAll(colors);
    }

    public Color get(int index) {
        if (index < 0 || index >= colors.size()) {
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for Brush with " + colors.size() + " colors");
        }
        return colors.get(index);
    }

    public Color getOr(int index, Color defaultColor) {
        if (index >= 0 && index < colors.size()) {
            return colors.get(index);
        }
        return defaultColor;
    }

    public void removeColor(int index) {
        if (index >= 0 && index < colors.size()) {
            colors.remove(index);
        }
    }

    public boolean isCount(int i) {
        return isCountIf((size)-> size == i);
    }

    public boolean isCountIf(Func<Integer, Boolean> check) {
        return isIf((list) -> check.get(list.size()));
    }

    public boolean isIf(Func<List<Color>,Boolean> check) {
        return check.get(Collections.unmodifiableList(colors));
    }

    public boolean isAvailable(int i) {
        return isCountIf((count)->count >= i);
    }
    public void throwIsNotAvailable(int i, String message) {
        if(!isAvailable(i)) throw new IndexOutOfBoundsException(message);
    }

    public void throwIsNotAvailable(int i) {
        if(!isAvailable(i)) throw new IndexOutOfBoundsException("!isAvailable(" + i + ")");
    }


    public Color first() {
        throwIsEmpty();
        return colors.get(0);
    }

    public Color last() {
        throwIsEmpty();
        return colors.get(colors.size() - 1);
    }

    public int size() {
        return colors.size();
    }

    private void throwIsEmpty() {
        if(colors.isEmpty()) throw new RuntimeException("colors is empty");
    }

    public Color color1() {
        throwIsEmpty();
        return get(0);
    }

    public Color color2() {
        throwIsEmpty();
        return get(1);
    }

    public Color color3() {
        throwIsEmpty();
        return get(2);
    }

    public Color color4() {
        throwIsEmpty();
        return get(3);
    }

    public List<Color> getColors() {
        return Collections.unmodifiableList(colors);
    }

    public static Brush of(String hex) {
        return new Brush(ColorConvertor.hexToColor(hex));
    }

    public static Brush of(Color color) {
        return new Brush(color);
    }

    public static Brush of(int r, int g, int b, int a) {
        return new Brush(new Color(r, g, b, a));
    }
}
