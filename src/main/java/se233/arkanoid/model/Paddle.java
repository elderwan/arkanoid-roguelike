// @author Oly
package se233.arkanoid.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;

public final class Paddle {
    private static final Logger LOGGER = LoggerFactory.getLogger(Paddle.class);
    private double x;
    private double width = GameConfig.PADDLE_WIDTH;
    private double speed = GameConfig.PADDLE_SPEED;
    private double velocityX;
    private double animationTime;
    private PaddleState state = PaddleState.IDLE;

    public Paddle() { reset(); }

    public double getX() { return x; }
    public double getY() { return GameConfig.PADDLE_Y; }
    public double getWidth() { return width; }
    public double getHeight() { return GameConfig.PADDLE_HEIGHT; }
    public double getSpeed() { return speed; }
    public double getVelocityX() { return velocityX; }
    public PaddleState getState() { return state; }
    public double getAnimationTime() { return animationTime; }
    public double getCenterX() { return x + width / 2.0; }

    public void setWidth(double width) {
        if (!Double.isFinite(width) || width <= 0.0 || width > GameConfig.FIELD_WIDTH) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Paddle width must be within the field.");
        }
        this.width = width;
        x = clampX(x);
    }

    public void setSpeed(double speed) {
        if (!Double.isFinite(speed) || speed <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Paddle speed must be positive.");
        }
        this.speed = speed;
    }

    public void setX(double x) {
        if (!Double.isFinite(x)) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Paddle position must be finite.");
        }
        this.x = clampX(x);
    }

    public void moveLeft(double dt) {
        validateMovementDelta(dt);
        double oldX = x;
        x = clampX(x - speed * dt);
        // Demo: derive velocity from the clamped displacement, so a wall-blocked paddle reports zero.
        velocityX = (x - oldX) / dt;
        setMovingState(PaddleState.MOVING_LEFT);
    }

    public void moveRight(double dt) {
        validateMovementDelta(dt);
        double oldX = x;
        x = clampX(x + speed * dt);
        // Demo: actual displacement also keeps paddle influence correct at field boundaries.
        velocityX = (x - oldX) / dt;
        setMovingState(PaddleState.MOVING_RIGHT);
    }

    public void stop() {
        if (state != PaddleState.IDLE || velocityX != 0.0) {
            LOGGER.debug("Paddle stopped");
        }
        state = PaddleState.IDLE;
        velocityX = 0.0;
    }

    public void update(double dt) {
        validateDelta(dt);
        animationTime += dt;
    }

    public void reset() {
        width = GameConfig.PADDLE_WIDTH;
        speed = GameConfig.PADDLE_SPEED;
        x = (GameConfig.FIELD_WIDTH - width) / 2.0;
        velocityX = 0.0;
        animationTime = 0.0;
        state = PaddleState.IDLE;
    }

    private void setMovingState(PaddleState movingState) {
        if (state != movingState) {
            LOGGER.debug("Paddle started moving {}", movingState == PaddleState.MOVING_LEFT ? "left" : "right");
        }
        state = movingState;
    }

    private double clampX(double value) {
        return Math.max(0.0, Math.min(GameConfig.FIELD_WIDTH - width, value));
    }

    private static void validateDelta(double dt) {
        if (!Double.isFinite(dt) || dt < 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Time delta must be finite and non-negative.");
        }
    }

    private static void validateMovementDelta(double dt) {
        // Demo: movement velocity divides by dt, so zero or negative time is invalid input.
        if (!Double.isFinite(dt) || dt <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Paddle movement time delta must be positive.");
        }
    }
}
