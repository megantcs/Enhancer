package ru.megantcs.enhancer.platform.toolkit.Colors;

import java.awt.*;
import java.util.Objects;

public class GradientBrush extends Brush
{
    private int angle = 0;

    public GradientBrush() {
        super();
    }

    public GradientBrush(Color mainColor, int count) {
        super(mainColor, count);
    }

    public GradientBrush(Color... colors) {
        super(colors);
    }

    public GradientBrush(Brush brush) {
        super(brush.getColors());
    }

    public GradientBrush setAngle(int angle) {
        if(angle < 0 || angle > 360)
            throw new IllegalArgumentException("angle < 0 || angle > 360: " + angle);

        this.angle = angle;
        return this;
    }

    public boolean isHorizontal() {
        return angle == 0;
    }

    public boolean isVertical() {
        return angle == 90;
    }

    public int getAngle() {
        return angle;
    }

    public TypeGradient getType()
    {
        if(isHorizontal()) return TypeGradient.horizontal;
        if(isVertical()) return TypeGradient.vertical;

        return TypeGradient.none;
    }
}
