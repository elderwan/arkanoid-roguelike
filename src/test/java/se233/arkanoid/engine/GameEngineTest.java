// @author Wang
package se233.arkanoid.engine;

import org.junit.jupiter.api.Test;
import se233.arkanoid.GameConfig;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;
import se233.arkanoid.model.Ball;
import se233.arkanoid.model.NormalBrick;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {
    @Test
    void destroyingLastBrickAwardsScoreAndCompletesStage() {
        GameEngine engine = new GameEngine();
        engine.startRun();
        GameState state = engine.getState();
        state.getBricks().clear();
        state.getBricks().add(new NormalBrick(50, 80));
        Ball ball = state.getBalls().get(0);
        ball.setPosition(85, 115);
        ball.setVelocity(0, -100);
        state.setBallLaunched(true);

        engine.update(0.01);

        assertTrue(engine.isStageComplete());
        assertEquals(10, state.getScore());
        assertEquals(1, state.getComboCount());
        assertEquals(1, state.drainScoreEvents().size());
    }

    @Test
    void losingLastBallCostsOneLifeAndZeroLivesEndsRun() {
        GameEngine engine = new GameEngine();
        engine.startRun();
        GameState state = engine.getState();
        for (int loss = 1; loss <= GameConfig.STARTING_LIVES; loss++) {
            state.setBallLaunched(true);
            state.getBalls().get(0).setPosition(400, GameConfig.FIELD_HEIGHT + 20);
            engine.update(0.0);
            assertEquals(GameConfig.STARTING_LIVES - loss, state.getLives());
        }
        assertTrue(state.isGameOver());
    }

    @Test
    void ballLaunchUsesPaddleVelocityAndIsStraightUpWhenStationary() {
        GameEngine stationaryEngine = new GameEngine();
        stationaryEngine.startRun();
        Ball stationaryBall = stationaryEngine.getState().getBalls().get(0);
        stationaryEngine.launchBall();
        assertEquals(0.0, stationaryBall.getVx(), 1.0e-6);
        assertTrue(stationaryBall.getVy() < 0.0);

        GameEngine movingEngine = new GameEngine();
        movingEngine.startRun();
        var movingState = movingEngine.getState();
        movingState.getPaddle().setSpeed(400.0);
        movingState.getPaddle().setX(200.0);
        movingState.getPaddle().moveRight(1.0);
        Ball movingBall = movingState.getBalls().get(0);
        movingEngine.launchBall();
        assertTrue(movingBall.getVx() > 0.0);
        assertTrue(movingBall.getVy() < 0.0);
        assertEquals(GameConfig.BALL_BASE_SPEED,
                Math.hypot(movingBall.getVx(), movingBall.getVy()), 1.0e-6);
    }

    @Test
    void clearingFifthStageSetsVictory() {
        GameEngine engine = new GameEngine();
        engine.startRun();
        GameState state = engine.getState();
        state.setStageNumber(GameConfig.TOTAL_STAGES);
        state.getBricks().clear();
        engine.update(0.0);
        assertTrue(engine.isStageComplete());
        engine.advanceStage();
        assertTrue(state.isVictory());
    }

    @Test
    void scoreCannotDecrease() {
        GameState state = new GameState();
        state.addScore(40);
        assertThrows(GameException.class, () -> state.addScore(-1));
        assertEquals(40, state.getScore());
        GameException exception = assertThrows(GameException.class, () -> state.setStageNumber(0));
        assertEquals(ErrorCode.INVALID_STAGE, exception.getErrorCode());
    }
}
