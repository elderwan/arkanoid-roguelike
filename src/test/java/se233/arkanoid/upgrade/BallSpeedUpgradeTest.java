// @author Wang
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;
import se233.arkanoid.engine.GameState;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BallSpeedUpgradeTest {
    @Test
    void increasesBallSpeedMultiplierByTwentyPercent() {
        GameState state = new GameState();
        new BallSpeedUpgrade().apply(state);
        assertEquals(1.2, state.getBalls().get(0).getSpeedMultiplier(), 0.0001);
    }
}
