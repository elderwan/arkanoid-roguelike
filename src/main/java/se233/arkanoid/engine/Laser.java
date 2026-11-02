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

    // Laser: Initializes the object for use by the application.
    // Inputs: x, y, speed.
    // Output/behavior: Initializes the object; no value is returned.
    public Laser(double x, double y, double speed) {
        if (!Double.isFinite(x) || !Double.isFinite(y) || !Double.isFinite(speed) || speed <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Laser coordinates and speed must be valid.");
        }
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    // getX: Returns the current x.
    // Inputs: none.
    // Output/behavior: Returns the computed get x value.
    public double getX() { return x; }
    // getY: Returns the current y.
    // Inputs: none.
    // Output/behavior: Returns the computed get y value.
    public double getY() { return y; }
    // isActive: Reports whether active is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isActive() { return active; }
    // update: Advances the associated state using the elapsed time.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void update(double dt) {
        if (!Double.isFinite(dt) || dt < 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Time delta must be finite and non-negative.");
        }
        y -= speed * dt;
    }
    // deactivate: Performs the deactivate operation.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void deactivate() {
        active = false;
        LOGGER.debug("Laser projectile consumed at ({}, {})", x, y);
    }
}
