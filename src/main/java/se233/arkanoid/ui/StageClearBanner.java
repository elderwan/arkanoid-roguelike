// @author Oly
package se233.arkanoid.ui;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StageClearBanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(StageClearBanner.class);
    // render: Renders the associated visual element using the supplied graphics context.
    // Inputs: gc, stageNumber.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void render(GraphicsContext gc, int stageNumber) {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.rgb(226, 183, 91));
        gc.setFont(Font.font("Monospaced", 15));
        gc.fillText("STAGE " + stageNumber + " CLEAR!", 400, 150);
    }
}
