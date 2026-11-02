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

    // Paddle: Initializes the object for use by the application.
    // Inputs: none.
    // Output/behavior: Initializes the object; no value is returned.
    public Paddle() { reset(); }

    // getX: Returns the current x.
    // Inputs: none.
    // Output/behavior: Returns the computed get x value.
    public double getX() { return x; }
    // getY: Returns the current y.
    // Inputs: none.
    // Output/behavior: Returns the computed get y value.
    public double getY() { return GameConfig.PADDLE_Y; }
    // getWidth: Returns the current width.
    // Inputs: none.
    // Output/behavior: Returns the computed get width value.
    public double getWidth() { return width; }
    // getHeight: Returns the current height.
    // Inputs: none.
    // Output/behavior: Returns the computed get height value.
    public double getHeight() { return GameConfig.PADDLE_HEIGHT; }
    // getSpeed: Returns the current speed.
    // Inputs: none.
    // Output/behavior: Returns the computed get speed value.
    public double getSpeed() { return speed; }
    // getVelocityX: Returns the current velocity x.
    // Inputs: none.
    // Output/behavior: Returns the computed get velocity x value.
    public double getVelocityX() { return velocityX; }
    // getState: Returns the current state.
    // Inputs: none.
    // Output/behavior: Returns the computed get state value.
    public PaddleState getState() { return state; }
    // getAnimationTime: Returns the current animation time.
    // Inputs: none.
    // Output/behavior: Returns the computed get animation time value.
    public double getAnimationTime() { return animationTime; }
    // getCenterX: Returns the current center x.
    // Inputs: none.
    // Output/behavior: Returns the computed get center x value.
    public double getCenterX() { return x + width / 2.0; }

    // setWidth: Updates the width value.
    // Inputs: width.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setWidth(double width) {
        if (!Double.isFinite(width) || width <= 0.0 || width > GameConfig.FIELD_WIDTH) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Paddle width must be within the field.");
        }
        this.width = width;
        x = clampX(x);
    }

    // setSpeed: Updates the speed value.
    // Inputs: speed.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setSpeed(double speed) {
        if (!Double.isFinite(speed) || speed <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Paddle speed must be positive.");
        }
        this.speed = speed;
    }

    // setX: Updates the x value.
    // Inputs: x.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setX(double x) {
        if (!Double.isFinite(x)) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Paddle position must be finite.");
        }
        this.x = clampX(x);
    }

    // moveLeft: Changes the associated object by performing the moveLeft operation.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void moveLeft(double dt) {
        validateMovementDelta(dt);
        double oldX = x;
        x = clampX(x - speed * dt);
        // Demo: derive velocity from the clamped displacement, so a wall-blocked paddle reports zero.
        velocityX = (x - oldX) / dt;
        setMovingState(PaddleState.MOVING_LEFT);
    }

    // moveRight: Changes the associated object by performing the moveRight operation.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void moveRight(double dt) {
        validateMovementDelta(dt);
        double oldX = x;
        x = clampX(x + speed * dt);
        // Demo: actual displacement also keeps paddle influence correct at field boundaries.
        velocityX = (x - oldX) / dt;
        setMovingState(PaddleState.MOVING_RIGHT);
    }

    // stop: Changes the associated object by performing the stop operation.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void stop() {
        if (state != PaddleState.IDLE || velocityX != 0.0) {
            LOGGER.debug("Paddle stopped");
        }
        state = PaddleState.IDLE;
        velocityX = 0.0;
    }

    // update: Advances the associated state using the elapsed time.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void update(double dt) {
        validateDelta(dt);
        animationTime += dt;
    }

    // reset: Restores the associated state to its initial values.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void reset() {
        width = GameConfig.PADDLE_WIDTH;
        speed = GameConfig.PADDLE_SPEED;
        x = (GameConfig.FIELD_WIDTH - width) / 2.0;
        velocityX = 0.0;
        animationTime = 0.0;
        state = PaddleState.IDLE;
    }

    // setMovingState: Updates the moving state value.
    // Inputs: movingState.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void setMovingState(PaddleState movingState) {
        if (state != movingState) {
            LOGGER.debug("Paddle started moving {}", movingState == PaddleState.MOVING_LEFT ? "left" : "right");
        }
        state = movingState;
    }

    // clampX: Executes the clamp x operation.
    // Inputs: value.
    // Output/behavior: Returns the computed clamp x value.
    private double clampX(double value) {
        return Math.max(0.0, Math.min(GameConfig.FIELD_WIDTH - width, value));
    }

    // validateDelta: Executes the validate delta operation.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private static void validateDelta(double dt) {
        if (!Double.isFinite(dt) || dt < 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Time delta must be finite and non-negative.");
        }
    }

    // validateMovementDelta: Executes the validate movement delta operation.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private static void validateMovementDelta(double dt) {
        // Demo: movement velocity divides by dt, so zero or negative time is invalid input.
        if (!Double.isFinite(dt) || dt <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Paddle movement time delta must be positive.");
        }
    }
}
