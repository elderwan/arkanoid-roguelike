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

    // Brick: Initializes the object for use by the application.
    // Inputs: x, y, width, height, hitPoints, scoreValue, type.
    // Output/behavior: Initializes the object; no value is returned.
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

    // getX: Returns the current x.
    // Inputs: none.
    // Output/behavior: Returns the computed get x value.
    public double getX() { return x; }
    // getY: Returns the current y.
    // Inputs: none.
    // Output/behavior: Returns the computed get y value.
    public double getY() { return y; }
    // getWidth: Returns the current width.
    // Inputs: none.
    // Output/behavior: Returns the computed get width value.
    public double getWidth() { return width; }
    // getHeight: Returns the current height.
    // Inputs: none.
    // Output/behavior: Returns the computed get height value.
    public double getHeight() { return height; }
    // getHitPoints: Returns the current hit points.
    // Inputs: none.
    // Output/behavior: Returns the computed get hit points value.
    public int getHitPoints() { return hitPoints; }
    // getMaxHitPoints: Returns the current max hit points.
    // Inputs: none.
    // Output/behavior: Returns the computed get max hit points value.
    public int getMaxHitPoints() { return maxHitPoints; }
    // getScoreValue: Returns the current score value.
    // Inputs: none.
    // Output/behavior: Returns the computed get score value value.
    public int getScoreValue() { return scoreValue; }
    // getType: Returns the current type.
    // Inputs: none.
    // Output/behavior: Returns the computed get type value.
    public BrickType getType() { return type; }
    // isDestroyed: Reports whether destroyed is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isDestroyed() { return hitPoints == 0; }
    // getHitFlashTimer: Returns the current hit flash timer.
    // Inputs: none.
    // Output/behavior: Returns the computed get hit flash timer value.
    public double getHitFlashTimer() { return hitFlashTimer; }
    // boundsLeft: Executes the bounds left operation.
    // Inputs: none.
    // Output/behavior: Returns the computed bounds left value.
    public double boundsLeft() { return x; }
    // boundsRight: Executes the bounds right operation.
    // Inputs: none.
    // Output/behavior: Returns the computed bounds right value.
    public double boundsRight() { return x + width; }
    // boundsTop: Executes the bounds top operation.
    // Inputs: none.
    // Output/behavior: Returns the computed bounds top value.
    public double boundsTop() { return y; }
    // boundsBottom: Executes the bounds bottom operation.
    // Inputs: none.
    // Output/behavior: Returns the computed bounds bottom value.
    public double boundsBottom() { return y + height; }

    // hit: Executes the hit operation.
    // Inputs: damage.
    // Output/behavior: Returns the computed hit value.
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

    // update: Advances the associated state using the elapsed time.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void update(double dt) {
        if (!Double.isFinite(dt) || dt < 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Time delta must be finite and non-negative.");
        }
        hitFlashTimer = Math.max(0.0, hitFlashTimer - dt);
    }
}
