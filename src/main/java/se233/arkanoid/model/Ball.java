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

    public Ball(double x, double y, double vx, double vy) {
        setPosition(x, y);
        setVelocity(vx, vy);
    }

    public static Ball attachedTo(Paddle paddle) {
        return new Ball(paddle.getCenterX(), paddle.getY() - GameConfig.BALL_RADIUS, 0.0, 0.0);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getRadius() { return GameConfig.BALL_RADIUS; }
    public double getVx() { return vx; }
    public double getVy() { return vy; }
    public double getSpeedMultiplier() { return speedMultiplier; }
    public boolean isPiercing() { return piercing; }
    public BallState getState() { return state; }
    public double getBounceAnimationTime() { return bounceAnimationTime; }

    public void setPosition(double x, double y) {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Ball position must be finite.");
        }
        this.x = x;
        this.y = y;
    }

    public void setVelocity(double vx, double vy) {
        if (!Double.isFinite(vx) || !Double.isFinite(vy)) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Ball velocity must be finite.");
        }
        this.vx = vx;
        this.vy = vy;
    }

    public void setSpeedMultiplier(double multiplier) {
        if (!Double.isFinite(multiplier) || multiplier <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Ball speed multiplier must be positive.");
        }
        double ratio = multiplier / speedMultiplier;
        vx *= ratio;
        vy *= ratio;
        speedMultiplier = multiplier;
    }

    public void setPiercing(boolean piercing) { this.piercing = piercing; }

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

    public void onBounce() {
        state = BallState.BOUNCING;
        bounceAnimationTime = 0.18;
        LOGGER.debug("Ball bounced at ({}, {})", x, y);
    }
}
