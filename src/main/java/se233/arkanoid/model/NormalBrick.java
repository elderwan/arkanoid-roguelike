// @author Oly
package se233.arkanoid.model;

import se233.arkanoid.GameConfig;

public final class NormalBrick extends Brick {
    // NormalBrick: Initializes the object for use by the application.
    // Inputs: x, y.
    // Output/behavior: Initializes the object; no value is returned.
    public NormalBrick(double x, double y) {
        super(x, y, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT, 1, 10, BrickType.NORMAL);
    }
}
