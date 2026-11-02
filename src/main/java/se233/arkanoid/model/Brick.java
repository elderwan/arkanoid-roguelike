// @author Oly
package se233.arkanoid.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;

public abstract class Brick {
    private static final Logger LOGGER = LoggerFactory.getLogger(Brick.class);
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final int maxHitPoints;
    private final int scoreValue;
    private final BrickType type;
    private int hitPoints;
    private double hitFlashTimer;

    protected Brick(double x, double y, double width, double height, int hitPoints, int scoreValue, BrickType type) {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(width)
                || !Double.isFinite(height) || width <= 0.0 || height <= 0.0
                || hitPoints <= 0 || scoreValue < 0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Invalid brick geometry or attributes.");
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hitPoints = hitPoints;
        this.maxHitPoints = hitPoints;
        this.scoreValue = scoreValue;
        this.type = type;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public int getHitPoints() { return hitPoints; }
    public int getMaxHitPoints() { return maxHitPoints; }
    public int getScoreValue() { return scoreValue; }
    public BrickType getType() { return type; }
    public boolean isDestroyed() { return hitPoints == 0; }
    public double getHitFlashTimer() { return hitFlashTimer; }
    public double boundsLeft() { return x; }
    public double boundsRight() { return x + width; }
    public double boundsTop() { return y; }
    public double boundsBottom() { return y + height; }

    public HitResult hit(int damage) {
        if (damage <= 0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Brick damage must be positive.");
        }
        if (isDestroyed()) return HitResult.DESTROYED;
        // Demo: clamp HP at zero and start a timed flash for the renderer on every valid hit.
        hitPoints = Math.max(0, hitPoints - damage);
        hitFlashTimer = 0.16;
        if (isDestroyed()) {
            LOGGER.info("{} brick destroyed at ({}, {})", type, x, y);
            return HitResult.DESTROYED;
        }
        LOGGER.debug("{} brick damaged; {} hit points remain", type, hitPoints);
        return HitResult.DAMAGED;
    }

    public void update(double dt) {
        if (!Double.isFinite(dt) || dt < 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Time delta must be finite and non-negative.");
        }
        hitFlashTimer = Math.max(0.0, hitFlashTimer - dt);
    }
}
