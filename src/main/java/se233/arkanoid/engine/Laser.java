// @author Wang
package se233.arkanoid.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;

public final class Laser {
    private static final Logger LOGGER = LoggerFactory.getLogger(Laser.class);
    private double x;
    private double y;
    private final double speed;
    private boolean active = true;

    public Laser(double x, double y, double speed) {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(speed) || speed <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Laser coordinates and speed must be valid.");
        }
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public boolean isActive() { return active; }
    public void update(double dt) {
        if (!Double.isFinite(dt) || dt < 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Time delta must be finite and non-negative.");
        }
        y -= speed * dt;
    }
    public void deactivate() {
        active = false;
        LOGGER.debug("Laser projectile consumed at ({}, {})", x, y);
    }
}
