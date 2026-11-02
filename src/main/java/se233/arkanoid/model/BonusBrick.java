// @author Oly
package se233.arkanoid.model;

import se233.arkanoid.GameConfig;

public final class BonusBrick extends Brick {
    // BonusBrick: Initializes the object for use by the application.
    // Inputs: x, y.
    // Output/behavior: Initializes the object; no value is returned.
    public BonusBrick(double x, double y) {
        super(x, y, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT, 2, 50, BrickType.BONUS);
    }
}
