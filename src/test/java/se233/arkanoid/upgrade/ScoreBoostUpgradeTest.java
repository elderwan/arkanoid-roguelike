// @author Oly
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;
import se233.arkanoid.engine.GameState;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreBoostUpgradeTest {
    @Test
    void increasesScoreMultiplierByFiftyPercent() {
        GameState state = new GameState();
        new ScoreBoostUpgrade().apply(state);
        assertEquals(1.5, state.getScoreMultiplier(), 0.0001);
    }
}
