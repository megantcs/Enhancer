package ru.megantcs.enhancer.platform.mixin;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.luaj.vm2.LuaTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.megantcs.enhancer.platform.hook.ScoreboardRenderHook;
import ru.megantcs.enhancer.platform.interfaces.Minecraft;
import ru.megantcs.enhancer.platform.loader.LuaEngine;

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.megantcs.enhancer.platform.loader.modules.impl.FabricEventsModule.*;

@Mixin(InGameHud.class)
public class InGameHudMixin implements Minecraft
{
    @Shadow
    private int scaledHeight;

    @Shadow
    private int scaledWidth;

    @Inject(at = @At("HEAD"), method = "renderScoreboardSidebar", cancellable = true)
    public void renderScoreboardSidebar(DrawContext context, ScoreboardObjective objective, CallbackInfo ci)
    {
        ci.cancel();

        var textRenderer = mc.textRenderer;

        Scoreboard scoreboard = objective.getScoreboard();
        Collection<ScoreboardPlayerScore> allScores = scoreboard.getAllPlayerScores(objective);

        List<ScoreboardPlayerScore> filteredScores = (List<ScoreboardPlayerScore>) allScores.stream()
                .filter(score -> score.getPlayerName() != null && !score.getPlayerName().startsWith("#"))
                .collect(Collectors.toList());

        Collection<ScoreboardPlayerScore> displayedScores;
        if (filteredScores.size() > 15) {
            displayedScores = Lists.newArrayList(Iterables.skip(filteredScores, filteredScores.size() - 15));
        } else {
            displayedScores = filteredScores;
        }

        List<Pair<ScoreboardPlayerScore, Text>> scoreEntries = Lists.newArrayListWithCapacity(displayedScores.size());
        Text objectiveTitle = objective.getDisplayName();
        int titleWidth = textRenderer.getWidth(objectiveTitle);
        int maxWidth = titleWidth;
        int colonWidth = textRenderer.getWidth(": ");

        for (ScoreboardPlayerScore score : displayedScores) {
            Team team = scoreboard.getPlayerTeam(score.getPlayerName());
            Text formattedName = Team.decorateName(team, Text.literal(score.getPlayerName()));
            scoreEntries.add(Pair.of(score, formattedName));

            int entryWidth = textRenderer.getWidth(formattedName) +
                    colonWidth +
                    textRenderer.getWidth(Integer.toString(score.getScore()));
            maxWidth = Math.max(maxWidth, entryWidth);
        }

        int entryCount = displayedScores.size();
        int totalHeight = entryCount * 9;
        int centerY = this.scaledHeight / 2 + totalHeight / 3;
        int padding = 3;
        int scoreboardRight = this.scaledWidth - padding;
        int scoreboardLeft = scoreboardRight - maxWidth - padding * 2;
        int currentEntryIndex = 0;

        int rowBackgroundColor = Color.blue.getRGB();
        int headerBackgroundColor = Color.red.getRGB();

        for (Pair<ScoreboardPlayerScore, Text> entry : scoreEntries) {
            currentEntryIndex++;
            ScoreboardPlayerScore score = entry.getFirst();
            Text playerNameText = entry.getSecond();

            Formatting scoreColor = Formatting.RED;
            String scoreText = "" + scoreColor + score.getScore();

            int rowY = centerY - currentEntryIndex * 9;
            int rowRightEdge = this.scaledWidth - padding + 2;
            int rowLeftEdge = scoreboardLeft - 2;

            renderBackgroundScoreboard(context, rowLeftEdge, rowY, rowRightEdge, rowY + 9, rowBackgroundColor);
            context.drawText(textRenderer, playerNameText, scoreboardLeft, rowY, -1, false);

            int scoreTextWidth = textRenderer.getWidth(scoreText);
            context.drawText(textRenderer, scoreText, rowRightEdge - scoreTextWidth, rowY, -1, false);

            if (currentEntryIndex == displayedScores.size()) {
                int headerY = rowY - 9 - 1;

                renderHeaderScoreboard(context, rowLeftEdge, headerY, rowRightEdge, rowY - 1, headerBackgroundColor);
                renderSeparatorScoreboard(context, scoreboardLeft, rowY, rowRightEdge, rowBackgroundColor);

                int titleX = scoreboardLeft + maxWidth / 2 - titleWidth / 2;
                context.drawText(textRenderer, objectiveTitle, titleX, rowY - 9, -1, false);
            }
        }
    }

    private void renderSeparatorScoreboard(DrawContext context, int scoreboardLeft, int rowY, int rowRightEdge, int rowBackgroundColor) {
        var cancel = ScoreboardRenderHook.RENDER_SEPARATOR.emit(new ScoreboardRenderHook.RenderInfo(
                context,
                scoreboardLeft,
                rowY,
                rowRightEdge,
                rowY,
                rowBackgroundColor));

        if(!cancel) context.fill(scoreboardLeft - 2, rowY - 1, rowRightEdge, rowY, rowBackgroundColor);
    }

    private void renderBackgroundScoreboard(DrawContext context,
                                            int left, int top,
                                            int right, int bottom,
                                            int backgroundColor) {
        var cancel = ScoreboardRenderHook.RENDER_BACKGROUND.emit(new ScoreboardRenderHook.RenderInfo(
                context, left, top, right, bottom, backgroundColor));

        if(!cancel) context.fill(left, top, right, bottom, Color.white.getRGB());
    }

    private void renderHeaderScoreboard(DrawContext context,
                                        int left, int top,
                                        int right, int bottom,
                                        int headerColor) {
        var cancel = ScoreboardRenderHook.RENDER_HEADER.emit(new ScoreboardRenderHook.RenderInfo(
                context, left, top, right, bottom, headerColor
        ));

        if(!cancel) context.fill(left, top, right, bottom, Color.yellow.getRGB());
    }
}
