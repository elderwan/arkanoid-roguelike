// @author Oly
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;
import se233.arkanoid.engine.GameState;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaddleSpeedUpgradeTest {
    @Test
    void increasesPaddleSpeedByTwentyFivePercent() {
        GameState state = new GameState();
        new PaddleSpeedUpgrade().apply(state);
        assertEquals(650.0, state.getPaddle().getSpeed(), 0.001);
    }
}
