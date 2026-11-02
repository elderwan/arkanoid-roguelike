// @author Wang
package se233.arkanoid.render;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.engine.BrickEffect;
import se233.arkanoid.engine.GameState;
import se233.arkanoid.engine.Laser;
import se233.arkanoid.model.Ball;
import se233.arkanoid.model.Brick;
import se233.arkanoid.model.BrickType;
import se233.arkanoid.ui.HudView;

public final class GameRenderer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRenderer.class);
    private final SpriteSheet spriteSheet = new SpriteSheet();
    private final ParticleEffects particles = new ParticleEffects();
    private final HudView hud = new HudView();

    // render: Renders the associated visual element using the supplied graphics context.
    // Inputs: gc, state, dt, paused, errorMessage.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void render(GraphicsContext gc, GameState state, double dt, boolean paused, String errorMessage) {
        drawBackground(gc);
        for (BrickEffect effect : state.drainBrickEffects()) {
            if (effect.destroyed()) particles.spawnBrickDestroyed(effect.x(), effect.y());
            else particles.spawnBrickHit(effect.x(), effect.y());
        }
        particles.update(dt);
        for (Brick brick : state.getBricks()) drawBrick(gc, brick);
        state.getLasers().forEach(laser -> drawLaser(gc, laser));
        var paddle = state.getPaddle();
        spriteSheet.drawPaddle(gc, paddle.getX(), paddle.getY(), paddle.getWidth(),
                paddle.getHeight(), paddle.getState(), paddle.getAnimationTime());
        for (Ball ball : state.getBalls()) {
            spriteSheet.drawBall(gc, ball.getX(), ball.getY(), ball.getRadius(),
                    ball.getState(), ball.getBounceAnimationTime());
        }
        particles.render(gc);
        hud.render(gc, state, dt);
        if (!state.isBallLaunched() && !paused) {
            gc.setFill(Color.rgb(190, 202, 220));
            gc.fillText("Press SPACE to launch", 332, 625);
        }
        if (paused) overlay(gc, "PAUSED", "Press P to resume");
        if (errorMessage != null && !errorMessage.isBlank()) {
            gc.setFill(Color.rgb(130, 28, 38, 0.94));
            gc.fillRoundRect(175, 580, 450, 58, 8, 8);
            gc.setFill(Color.WHITE);
            gc.fillText(errorMessage, 195, 614);
        }
    }

    // drawBackground: Renders the associated visual element using the supplied graphics context.
    // Inputs: gc.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void drawBackground(GraphicsContext gc) {
        gc.setFill(Color.rgb(12, 19, 34));
        gc.fillRect(0, 0, GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT);
        gc.setStroke(Color.rgb(29, 43, 66));
        gc.setLineWidth(1);
        for (int y = 110; y < 650; y += 54) gc.strokeLine(0, y, 800, y);
        gc.setStroke(Color.rgb(43, 63, 91));
        gc.strokeLine(0, GameConfig.FIELD_HEIGHT - 2, 800, GameConfig.FIELD_HEIGHT - 2);
    }

    // drawBrick: Renders the associated visual element using the supplied graphics context.
    // Inputs: gc, brick.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void drawBrick(GraphicsContext gc, Brick brick) {
        if (brick.isDestroyed()) return;
        Color base = switch (brick.getType()) {
            case NORMAL -> Color.rgb(85, 124, 177);
            case STRONG -> Color.rgb(159, 97, 161);
            case BONUS -> Color.rgb(226, 183, 91);
        };
        gc.setFill(brick.getHitFlashTimer() > 0.0 ? Color.WHITE : base);
        gc.fillRoundRect(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight(), 5, 5);
        gc.setStroke(Color.rgb(225, 238, 255, 0.62));
        gc.strokeRoundRect(brick.getX() + 1, brick.getY() + 1,
                brick.getWidth() - 2, brick.getHeight() - 2, 4, 4);
        if (brick.getMaxHitPoints() > 1) {
            gc.setFill(Color.rgb(13, 20, 33, 0.58));
            gc.fillOval(brick.getX() + brick.getWidth() - 16, brick.getY() + 5, 9, 9);
            if (brick.getHitPoints() >= 3) gc.fillOval(brick.getX() + brick.getWidth() - 29,
                    brick.getY() + 5, 9, 9);
        }
    }

    // drawLaser: Renders the associated visual element using the supplied graphics context.
    // Inputs: gc, laser.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void drawLaser(GraphicsContext gc, Laser laser) {
        gc.setStroke(Color.rgb(255, 117, 117));
        gc.setLineWidth(4);
        gc.strokeLine(laser.getX(), laser.getY() - 12, laser.getX(), laser.getY() + 2);
        gc.setStroke(Color.rgb(255, 221, 213));
        gc.setLineWidth(1);
        gc.strokeLine(laser.getX(), laser.getY() - 12, laser.getX(), laser.getY() + 2);
    }

    // overlay: Executes the overlay operation.
    // Inputs: gc, title, subtitle.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void overlay(GraphicsContext gc, String title, String subtitle) {
        gc.setFill(Color.rgb(5, 8, 15, 0.72));
        gc.fillRect(0, 54, 800, 666);
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Monospaced", 32));
        gc.fillText(title, 335, 330);
        gc.setFont(javafx.scene.text.Font.font("Monospaced", 14));
        gc.fillText(subtitle, 316, 365);
    }
}
