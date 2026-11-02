// @author Oly
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;
import se233.arkanoid.engine.GameState;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExtraLifeUpgradeTest {
    @Test
    void addsExactlyOneLife() {
        GameState state = new GameState();
        new ExtraLifeUpgrade().apply(state);
        assertEquals(4, state.getLives());
    }
}
