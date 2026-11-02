// @author Oly
package se233.arkanoid.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameOverView {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameOverView.class);
    // render: Renders the associated visual element using the supplied graphics context.
    // Inputs: gc, width, height, score.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void render(GraphicsContext gc, double width, double height, int score) {
        gc.setFill(Color.rgb(9, 14, 25, 0.94));
        gc.fillRect(0, 0, width, height);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.rgb(255, 117, 117));
        gc.setFont(Font.font("Monospaced", 38));
        gc.fillText("RUN ENDED", width / 2, 275);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Monospaced", 18));
        gc.fillText("Final score  " + score, width / 2, 335);
        buttons(gc, width, "RESTART", "MENU");
    }
    // isRestartClick: Reports whether restart click is true.
    // Inputs: x, y.
    // Output/behavior: Returns a boolean result.
    public boolean isRestartClick(double x, double y) { return inside(x, y, 275, 420); }
    // isMenuClick: Reports whether menu click is true.
    // Inputs: x, y.
    // Output/behavior: Returns a boolean result.
    public boolean isMenuClick(double x, double y) { return inside(x, y, 435, 580); }
    // inside: Executes the inside operation.
    // Inputs: x, y, left, right.
    // Output/behavior: Returns a boolean result.
    private static boolean inside(double x, double y, double left, double right) {
        return x >= left && x <= right && y >= 400 && y <= 455;
    }
    // buttons: Executes the buttons operation.
    // Inputs: gc, width, first, second.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void buttons(GraphicsContext gc, double width, String first, String second) {
        gc.setFont(Font.font("Monospaced", 15));
        gc.setTextBaseline(VPos.CENTER);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFill(Color.rgb(94, 226, 220));
        gc.fillRoundRect(width / 2 - 165, 400, 145, 55, 5, 5);
        gc.setFill(Color.rgb(10, 18, 30));
        gc.fillText(first, width / 2 - 92, 427);
        gc.setFill(Color.rgb(28, 40, 62));
        gc.fillRoundRect(width / 2 + 20, 400, 145, 55, 5, 5);
        gc.setStroke(Color.rgb(92, 113, 144));
        gc.strokeRoundRect(width / 2 + 20, 400, 145, 55, 5, 5);
        gc.setFill(Color.WHITE);
        gc.fillText(second, width / 2 + 92, 427);
    }
}
