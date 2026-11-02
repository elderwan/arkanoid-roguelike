// @author Wang
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;
import se233.arkanoid.engine.GameEngine;
import se233.arkanoid.engine.GameState;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PiercingBallUpgradeTest {
    @Test
    void makesCurrentAndFutureStageBallsPiercing() {
        GameEngine engine = new GameEngine();
        engine.startRun();
        GameState state = engine.getState();
        new PiercingBallUpgrade().apply(state);
        assertTrue(state.getBalls().get(0).isPiercing());
        state.getBricks().clear();
        engine.update(0.0);
        engine.advanceStage();
        assertTrue(state.getBalls().get(0).isPiercing());
    }
}
