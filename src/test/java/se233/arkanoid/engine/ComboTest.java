// @author Wang
package se233.arkanoid.engine;

import org.junit.jupiter.api.Test;
import se233.arkanoid.model.Ball;

import static org.junit.jupiter.api.Assertions.*;

class ComboTest {
    @Test
    void consecutiveBrickBreaksIncreaseMultiplierAndPaddleTouchResetsIt() {
        GameEngine engine = new GameEngine();
        engine.startRun();
        GameState state = engine.getState();
        assertEquals(1.0, state.getComboMultiplier());
        state.incrementCombo();
        assertEquals(1.0, state.getComboMultiplier());
        state.incrementCombo();
        assertEquals(2.0, state.getComboMultiplier());
        state.setBallLaunched(true);
        Ball ball = state.getBalls().get(0);
        ball.setPosition(state.getPaddle().getCenterX(), state.getPaddle().getY() - 7);
        ball.setVelocity(0, 100);

        engine.update(0.01);

        assertEquals(0, state.getComboCount());
        assertEquals(1.0, state.getComboMultiplier());
    }
}
