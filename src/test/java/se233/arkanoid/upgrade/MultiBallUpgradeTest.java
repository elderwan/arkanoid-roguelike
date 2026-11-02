// @author Wang
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;
import se233.arkanoid.engine.GameEngine;
import se233.arkanoid.engine.GameState;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiBallUpgradeTest {
    @Test
    void doublesBallsAndPersistsIntoNextStage() {
        GameEngine engine = new GameEngine();
        engine.startRun();
        GameState state = engine.getState();
        new MultiBallUpgrade().apply(state);
        assertEquals(2, state.getBalls().size());
        engine.getState().getBricks().clear();
        engine.update(0.0);
        engine.advanceStage();
        assertEquals(2, state.getBalls().size());
    }
}
