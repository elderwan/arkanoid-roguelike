// @author Oly
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;
import se233.arkanoid.engine.GameState;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaddleSizeUpgradeTest {
    @Test
    void increasesPaddleWidthByTwentyFivePercent() {
        GameState state = new GameState();
        new PaddleSizeUpgrade().apply(state);
        assertEquals(150.0, state.getPaddle().getWidth(), 0.001);
    }
}
