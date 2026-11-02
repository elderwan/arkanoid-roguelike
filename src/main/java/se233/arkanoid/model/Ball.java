// @author Wang
package se233.arkanoid.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;

public final class Ball {
    private static final Logger LOGGER = LoggerFactory.getLogger(Ball.class);
    private double x;
    private double y;
    private double vx;
    private double vy;
    private double speedMultiplier = 1.0;
    private boolean piercing;
    private BallState state = BallState.FLYING;
    private double bounceAnimationTime;

    // Ball: Initializes the object for use by the application.
    // Inputs: x, y, vx, vy.
    // Output/behavior: Initializes the object; no value is returned.
    public Ball(double x, double y, double vx, double vy) {
        setPosition(x, y);
        setVelocity(vx, vy);
    }

    // attachedTo: Creates or calculates the requested value from the supplied inputs.
    // Inputs: paddle.
    // Output/behavior: Returns the computed attached to value.
    public static Ball attachedTo(Paddle paddle) {
        return new Ball(paddle.getCenterX(), paddle.getY() - GameConfig.BALL_RADIUS, 0.0, 0.0);
    }

    // getX: Returns the current x.
    // Inputs: none.
    // Output/behavior: Returns the computed get x value.
    public double getX() { return x; }
    // getY: Returns the current y.
    // Inputs: none.
    // Output/behavior: Returns the computed get y value.
    public double getY() { return y; }
    // getRadius: Returns the current radius.
    // Inputs: none.
    // Output/behavior: Returns the computed get radius value.
    public double getRadius() { return GameConfig.BALL_RADIUS; }
    // getVx: Returns the current vx.
    // Inputs: none.
    // Output/behavior: Returns the computed get vx value.
    public double getVx() { return vx; }
    // getVy: Returns the current vy.
    // Inputs: none.
    // Output/behavior: Returns the computed get vy value.
    public double getVy() { return vy; }
    // getSpeedMultiplier: Returns the current speed multiplier.
    // Inputs: none.
    // Output/behavior: Returns the computed get speed multiplier value.
    public double getSpeedMultiplier() { return speedMultiplier; }
    // isPiercing: Reports whether piercing is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isPiercing() { return piercing; }
    // getState: Returns the current state.
    // Inputs: none.
    // Output/behavior: Returns the computed get state value.
    public BallState getState() { return state; }
    // getBounceAnimationTime: Returns the current bounce animation time.
    // Inputs: none.
    // Output/behavior: Returns the computed get bounce animation time value.
    public double getBounceAnimationTime() { return bounceAnimationTime; }

    // setPosition: Updates the position value.
    // Inputs: x, y.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setPosition(double x, double y) {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Ball position must be finite.");
        }
        this.x = x;
        this.y = y;
    }

    // setVelocity: Updates the velocity value.
    // Inputs: vx, vy.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setVelocity(double vx, double vy) {
        if (!Double.isFinite(vx) || !Double.isFinite(vy)) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Ball velocity must be finite.");
        }
        this.vx = vx;
        this.vy = vy;
    }

    // setSpeedMultiplier: Updates the speed multiplier value.
    // Inputs: multiplier.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setSpeedMultiplier(double multiplier) {
        if (!Double.isFinite(multiplier) || multiplier <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Ball speed multiplier must be positive.");
        }
        double ratio = multiplier / speedMultiplier;
        vx *= ratio;
        vy *= ratio;
        speedMultiplier = multiplier;
    }

    // setPiercing: Updates the piercing value.
    // Inputs: piercing.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setPiercing(boolean piercing) { this.piercing = piercing; }

    // update: Advances the associated state using the elapsed time.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void update(double dt) {
        if (!Double.isFinite(dt) || dt < 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Time delta must be finite and non-negative.");
        }
        x += vx * dt;
        y += vy * dt;
        if (state == BallState.BOUNCING) {
            bounceAnimationTime = Math.max(0.0, bounceAnimationTime - dt);
            if (bounceAnimationTime == 0.0) state = BallState.FLYING;
        }
    }

    // onBounce: Executes the on bounce operation.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void onBounce() {
        state = BallState.BOUNCING;
        bounceAnimationTime = 0.18;
        LOGGER.debug("Ball bounced at ({}, {})", x, y);
    }
}
