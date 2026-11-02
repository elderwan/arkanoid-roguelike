// @author Oly
package se233.arkanoid.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;
import se233.arkanoid.engine.ScoreEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class HudView {
    private static final Logger LOGGER = LoggerFactory.getLogger(HudView.class);
    private final List<Popup> popups = new ArrayList<>();

    public void render(GraphicsContext gc, GameState state, double dt) {
        // Demo: consume new score events once, then animate their floating point labels by elapsed time.
        for (ScoreEvent event : state.drainScoreEvents()) popups.add(new Popup(event));
        Iterator<Popup> iterator = popups.iterator();
        while (iterator.hasNext()) {
            Popup popup = iterator.next();
            popup.remaining -= dt;
            if (popup.remaining <= 0.0) {
                iterator.remove();
                continue;
            }
            gc.setGlobalAlpha(Math.min(1.0, popup.remaining * 2.0));
            gc.setFill(Color.rgb(255, 224, 132));
            gc.setFont(Font.font("Monospaced", 17));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.fillText("+" + popup.event.points(), popup.event.x(), popup.event.y() - (1.0 - popup.remaining) * 38.0);
            gc.setGlobalAlpha(1.0);
        }

        gc.setFill(Color.rgb(8, 14, 26, 0.9));
        gc.fillRect(0, 0, 800, 54);
        gc.setFont(Font.font("Monospaced", 15));
        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFill(Color.WHITE);
        // Demo: the HUD keeps score, remaining lives, stage, combo, and timed laser status visible.
        gc.fillText("SCORE  " + String.format("%06d", state.getScore()), 24, 27);
        gc.setFill(Color.rgb(255, 117, 117));
        gc.fillText("LIVES  " + state.getLives(), 260, 27);
        gc.setFill(Color.rgb(94, 226, 220));
        gc.fillText("STAGE  " + state.getStageNumber() + " / 5", 390, 27);
        gc.setFill(Color.rgb(226, 183, 91));
        gc.fillText("COMBO  x" + (int) state.getComboMultiplier(), 545, 27);
        if (state.isLaserActive()) {
            gc.setTextAlign(TextAlignment.RIGHT);
            gc.setFill(Color.rgb(255, 117, 117));
            gc.fillText(String.format("LASER %.1fs", state.getLaserTimeRemaining()), 780, 27);
        }
        String upgrades = state.getActiveUpgrades().stream().map(upgrade -> upgrade.isCursed()
                ? "!" + upgrade.getName() : upgrade.getName()).distinct().limit(5)
                .reduce((first, second) -> first + "  /  " + second).orElse("No upgrades yet");
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(Font.font("Monospaced", 11));
        gc.setFill(Color.rgb(165, 181, 205));
        gc.fillText("RELICS   " + upgrades, 24, 76);
    }

    private static final class Popup {
        private final ScoreEvent event;
        private double remaining = 1.0;
        private Popup(ScoreEvent event) { this.event = event; }
    }
}
