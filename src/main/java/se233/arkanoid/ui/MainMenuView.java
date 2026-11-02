// @author Oly
package se233.arkanoid.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MainMenuView {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuView.class);
    public void render(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.rgb(12, 19, 34));
        gc.fillRect(0, 0, width, height);
        gc.setFill(Color.rgb(20, 32, 54));
        for (int i = 0; i < 10; i++) gc.fillRect(0, 70 + i * 58, width, 1);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.rgb(94, 226, 220));
        gc.setFont(Font.font("Monospaced", 16));
        gc.fillText("A SURVIVAL ARCADE", width / 2, 170);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Monospaced", 48));
        gc.fillText("ARKANOID", width / 2, 230);
        gc.setFill(Color.rgb(226, 183, 91));
        gc.setFont(Font.font("Monospaced", 18));
        gc.fillText("ROGUELIKE", width / 2, 275);
        button(gc, width / 2 - 115, 370, 230, 54, "START RUN", true);
        button(gc, width / 2 - 115, 444, 230, 48, "QUIT", false);
        gc.setFill(Color.rgb(157, 170, 192));
        gc.setFont(Font.font("Monospaced", 13));
        gc.fillText("Break the grid. Choose your risk. Reach the final stage.", width / 2, 555);
        gc.fillText("Move: ← → or A / D     Launch / fire: SPACE     Pause: P", width / 2, 590);
    }

    public boolean isStartClick(double x, double y) { return x >= 285 && x <= 515 && y >= 370 && y <= 424; }
    public boolean isQuitClick(double x, double y) { return x >= 285 && x <= 515 && y >= 444 && y <= 492; }

    private void button(GraphicsContext gc, double x, double y, double w, double h, String text, boolean primary) {
        gc.setFill(primary ? Color.rgb(94, 226, 220) : Color.rgb(28, 40, 62));
        gc.fillRoundRect(x, y, w, h, 5, 5);
        gc.setStroke(primary ? Color.rgb(94, 226, 220) : Color.rgb(92, 113, 144));
        gc.strokeRoundRect(x, y, w, h, 5, 5);
        gc.setFill(primary ? Color.rgb(12, 19, 34) : Color.WHITE);
        gc.setFont(Font.font("Monospaced", 16));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText(text, x + w / 2, y + h / 2);
    }
}
