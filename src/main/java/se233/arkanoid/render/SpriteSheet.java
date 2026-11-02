// @author Oly
package se233.arkanoid.render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.model.BallState;
import se233.arkanoid.model.PaddleState;

import java.io.InputStream;

public final class SpriteSheet {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpriteSheet.class);
    private final Image image;

    // SpriteSheet: Initializes the object for use by the application.
    // Inputs: none.
    // Output/behavior: Initializes the object; no value is returned.
    public SpriteSheet() {
        InputStream resource = SpriteSheet.class.getResourceAsStream("/sprites/arkanoid.png");
        // Demo: missing or invalid artwork falls back to the same procedural draw methods below.
        if (resource == null) {
            image = null;
            LOGGER.warn("Sprite sheet /sprites/arkanoid.png is missing; procedural sprites will be used");
        } else {
            Image loaded = new Image(resource);
            if (loaded.isError() || loaded.getWidth() < 4 || loaded.getHeight() < 2) {
                image = null;
                LOGGER.warn("Sprite sheet could not be decoded or has invalid dimensions; using procedural sprites");
            } else {
                image = loaded;
                LOGGER.info("Loaded sprite sheet with dimensions {}x{}", loaded.getWidth(), loaded.getHeight());
            }
        }
    }

    // hasSpriteSheet: Reports whether sprite sheet is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean hasSpriteSheet() { return image != null; }

    // drawBall: Renders the associated visual element using the supplied graphics context.
    // Inputs: gc, x, y, radius, state, animationTime.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void drawBall(GraphicsContext gc, double x, double y, double radius, BallState state, double animationTime) {
        if (image != null) {
            double cellW = image.getWidth() / 4.0;
            double cellH = image.getHeight() / 2.0;
            // Demo: select a different sheet frame for ball bounce versus normal flight.
            int frame = state == BallState.BOUNCING ? 1 : 0;
            gc.drawImage(image, frame * cellW, 0, cellW, cellH, x - radius, y - radius,
                    radius * 2, radius * 2);
            return;
        }
        double pulse = state == BallState.BOUNCING ? 1.0 + 0.18 * Math.sin(animationTime * 35.0) : 1.0;
        double size = radius * 2.0 * pulse;
        gc.setFill(Color.rgb(255, 242, 205));
        gc.fillOval(x - size / 2.0, y - size / 2.0, size, size);
        gc.setFill(Color.rgb(94, 226, 220, 0.38));
        gc.fillOval(x - radius * 1.8, y - radius * 1.8, radius * 3.6, radius * 3.6);
    }

    // drawPaddle: Renders the associated visual element using the supplied graphics context.
    // Inputs: gc, x, y, width, height, state, animationTime.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void drawPaddle(GraphicsContext gc, double x, double y, double width, double height,
                           PaddleState state, double animationTime) {
        if (image != null) {
            double cellW = image.getWidth() / 4.0;
            double cellH = image.getHeight() / 2.0;
            // Demo: paddle idle, left, and right movement map to separate sprite frames.
            int frame = switch (state) {
                case IDLE -> 0;
                case MOVING_LEFT -> 2;
                case MOVING_RIGHT -> 3;
            };
            gc.drawImage(image, frame * cellW, cellH, cellW, cellH, x, y, width, height);
            return;
        }
        double glow = state == PaddleState.IDLE ? 0.0 : 0.12 + 0.08 * Math.sin(animationTime * 20.0);
        gc.setFill(Color.rgb(94, 226, 220, 0.2 + glow));
        gc.fillRoundRect(x - 5, y - 5, width + 10, height + 10, 10, 10);
        gc.setFill(Color.rgb(94, 226, 220));
        gc.fillRoundRect(x, y, width, height, 7, 7);
        gc.setFill(Color.rgb(226, 255, 250));
        gc.fillRoundRect(x + width * 0.12, y + 3, width * 0.76, 3, 3, 3);
    }
}
