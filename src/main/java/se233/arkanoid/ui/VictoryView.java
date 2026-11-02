// @author Oly
package se233.arkanoid.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class VictoryView {
    private static final Logger LOGGER = LoggerFactory.getLogger(VictoryView.class);
    public void render(GraphicsContext gc, double width, double height, int score) {
        gc.setFill(Color.rgb(9, 14, 25, 0.96));
        gc.fillRect(0, 0, width, height);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.rgb(94, 226, 220));
        gc.setFont(Font.font("Monospaced", 38));
        gc.fillText("GRID CONQUERED", width / 2, 270);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Monospaced", 18));
        gc.fillText("Five stages. One final score: " + score, width / 2, 330);
        gc.setFill(Color.rgb(94, 226, 220));
        gc.fillRoundRect(width / 2 - 165, 400, 145, 55, 5, 5);
        gc.setFill(Color.rgb(10, 18, 30));
        gc.setFont(Font.font("Monospaced", 15));
        gc.fillText("PLAY AGAIN", width / 2 - 92, 427);
        gc.setFill(Color.rgb(28, 40, 62));
        gc.fillRoundRect(width / 2 + 20, 400, 145, 55, 5, 5);
        gc.setStroke(Color.rgb(92, 113, 144));
        gc.strokeRoundRect(width / 2 + 20, 400, 145, 55, 5, 5);
        gc.setFill(Color.WHITE);
        gc.fillText("MENU", width / 2 + 92, 427);
    }
    public boolean isRestartClick(double x, double y) { return x >= 235 && x <= 380 && y >= 400 && y <= 455; }
    public boolean isMenuClick(double x, double y) { return x >= 420 && x <= 565 && y >= 400 && y <= 455; }
}
