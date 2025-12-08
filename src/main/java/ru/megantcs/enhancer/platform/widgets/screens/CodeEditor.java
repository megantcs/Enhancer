package ru.megantcs.enhancer.platform.widgets.screens;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.text.Text;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.render.impl.Graphics;
import ru.megantcs.enhancer.platform.toolkit.Colors.ColorConvertor;
import ru.megantcs.enhancer.platform.toolkit.Colors.GradientBrush;
import ru.megantcs.enhancer.platform.toolkit.utils.WindowUtils;

public class CodeEditor extends Screen implements Minecraft
{
    private final String file;

    public CodeEditor(String file) {
        super(Text.of("codeEditor"));
        this.file = combinePath(file);


    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        width = 100;
        height = 100;

        Graphics graphics = Graphics.Instance(context);
        Window window = graphics.getWindow();

        var pos = WindowUtils.getScaledCenter(window);

        graphics.drawRect(pos.getX() - width, pos.getY() - height,   pos.getX() + width, pos.getY() + height, new GradientBrush(
                ColorConvertor.hexToColor("#141414"),
                ColorConvertor.hexToColor("#242424")));
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
