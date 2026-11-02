// @author Wang
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;
import se233.arkanoid.GameConfig;
import se233.arkanoid.engine.GameState;

import static org.junit.jupiter.api.Assertions.*;

class CursedUpgradeTest {
    @Test
    void glassCannonPairsTripleScoreWithSmallerPaddle() {
        GameState state = new GameState();
        GlassCannonUpgrade upgrade = new GlassCannonUpgrade();
        upgrade.apply(state);
        assertTrue(upgrade.isCursed());
        assertEquals(3.0, state.getScoreMultiplier(), 0.001);
        assertEquals(GameConfig.PADDLE_WIDTH * 0.7, state.getPaddle().getWidth(), 0.001);
    }

    @Test
    void frenzyPairsSpeedWithAnExtraLife() {
        GameState state = new GameState();
        FrenzyUpgrade upgrade = new FrenzyUpgrade();
        upgrade.apply(state);
        assertTrue(upgrade.isCursed());
        assertEquals(1.4, state.getBalls().get(0).getSpeedMultiplier(), 0.001);
        assertEquals(GameConfig.STARTING_LIVES + 1, state.getLives());
    }
}
