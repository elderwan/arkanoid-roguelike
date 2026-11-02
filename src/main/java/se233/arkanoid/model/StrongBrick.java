// @author Oly
package se233.arkanoid.model;

import se233.arkanoid.GameConfig;

public final class StrongBrick extends Brick {
    public StrongBrick(double x, double y) {
        super(x, y, GameConfig.BRICK_WIDTH, GameConfig.BRICK_HEIGHT, 3, 30, BrickType.STRONG);
    }
}
