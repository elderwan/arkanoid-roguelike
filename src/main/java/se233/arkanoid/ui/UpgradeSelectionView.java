// @author Oly
package se233.arkanoid.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.upgrade.Upgrade;

import java.util.List;
import java.util.function.Consumer;

public final class UpgradeSelectionView {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradeSelectionView.class);
    private final List<Upgrade> choices;
    private final Consumer<Upgrade> onChoose;
    private boolean chosen;

    public UpgradeSelectionView(List<Upgrade> choices, Consumer<Upgrade> onChoose) {
        this.choices = List.copyOf(choices);
        this.onChoose = onChoose;
    }

    public void render(GraphicsContext gc, double width, double height, int clearedStage) {
        gc.setFill(Color.rgb(8, 13, 24, 0.96));
        gc.fillRect(0, 0, width, height);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.rgb(226, 183, 91));
        gc.setFont(Font.font("Monospaced", 15));
        gc.fillText("STAGE " + clearedStage + " CLEAR", width / 2, 150);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Monospaced", 32));
        gc.fillText("Choose one relic", width / 2, 200);
        gc.setFill(Color.rgb(165, 181, 205));
        gc.setFont(Font.font("Monospaced", 13));
        gc.fillText("Your choice lasts for the rest of this run.", width / 2, 235);

        double cardWidth = 218;
        double gap = 18;
        double startX = (width - choices.size() * cardWidth - (choices.size() - 1) * gap) / 2.0;
        for (int i = 0; i < choices.size(); i++) {
            Upgrade upgrade = choices.get(i);
            double x = startX + i * (cardWidth + gap);
            double y = 290;
            Color accent = upgrade.isCursed() ? Color.rgb(255, 95, 107) : Color.rgb(94, 226, 220);
            gc.setFill(Color.rgb(19, 29, 47));
            gc.fillRoundRect(x, y, cardWidth, 220, 8, 8);
            gc.setStroke(accent);
            gc.setLineWidth(2);
            gc.strokeRoundRect(x, y, cardWidth, 220, 8, 8);
            gc.setLineWidth(1);
            gc.setFill(accent);
            gc.setFont(Font.font("Monospaced", 13));
            gc.fillText("[" + (i + 1) + "]" + (upgrade.isCursed() ? " CURSED" : " RELIC"),
                    x + cardWidth / 2, y + 34);
            gc.setFill(Color.WHITE);
            gc.setFont(Font.font("Monospaced", 18));
            gc.fillText(upgrade.getName(), x + cardWidth / 2, y + 83);
            gc.setFill(Color.rgb(190, 202, 220));
            gc.setFont(Font.font("Monospaced", 12));
            wrap(gc, upgrade.getDescription(), x + cardWidth / 2, y + 126, 28);
            gc.setFill(accent);
            gc.setFont(Font.font("Monospaced", 12));
            gc.fillText("CHOOSE", x + cardWidth / 2, y + 192);
        }
        gc.setFill(Color.rgb(165, 181, 205));
        gc.setFont(Font.font("Monospaced", 12));
        gc.fillText("Press 1, 2, or 3 (or click a relic)", width / 2, 570);
    }

    public void handleKey(KeyCode key) {
        // Demo: number keys map directly to the three displayed relic choices.
        if (key == KeyCode.DIGIT1 || key == KeyCode.NUMPAD1) choose(0);
        else if (key == KeyCode.DIGIT2 || key == KeyCode.NUMPAD2) choose(1);
        else if (key == KeyCode.DIGIT3 || key == KeyCode.NUMPAD3) choose(2);
    }

    public void handleClick(double x, double y, double width) {
        if (y < 290 || y > 510) return;
        double cardWidth = 218;
        double gap = 18;
        double startX = (width - choices.size() * cardWidth - (choices.size() - 1) * gap) / 2.0;
        int index = (int) ((x - startX) / (cardWidth + gap));
        if (index >= 0 && index < choices.size() && x <= startX + (index + 1) * cardWidth + index * gap) {
            choose(index);
        }
    }

    private void choose(int index) {
        if (chosen || index < 0 || index >= choices.size()) return;
        // Demo: guard the callback so a double click or repeated key cannot apply two upgrades.
        chosen = true;
        Upgrade upgrade = choices.get(index);
        LOGGER.info("Player chose upgrade {}", upgrade.getName());
        onChoose.accept(upgrade);
    }

    private void wrap(GraphicsContext gc, String text, double centerX, double y, int maxCharacters) {
        String[] words = text.split(" ");
        String line = "";
        int lineNumber = 0;
        for (String word : words) {
            String next = line.isEmpty() ? word : line + " " + word;
            if (next.length() > maxCharacters && !line.isEmpty()) {
                gc.fillText(line, centerX, y + lineNumber++ * 20);
                line = word;
            } else line = next;
        }
        if (!line.isEmpty()) gc.fillText(line, centerX, y + lineNumber * 20);
    }
}
