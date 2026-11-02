// @author Oly
package se233.arkanoid.model;

import org.junit.jupiter.api.Test;
import se233.arkanoid.GameConfig;

import static org.junit.jupiter.api.Assertions.*;

class PaddleTest {
    @Test
    void movementClampsAtFieldEdgesAndUpdatesState() {
        Paddle paddle = new Paddle();
        paddle.setX(0);
        paddle.moveLeft(1.0);
        assertEquals(0.0, paddle.getX());
        assertEquals(PaddleState.MOVING_LEFT, paddle.getState());
        paddle.moveRight(0.5);
        assertEquals(PaddleState.MOVING_RIGHT, paddle.getState());
        paddle.setX(GameConfig.FIELD_WIDTH);
        assertEquals(GameConfig.FIELD_WIDTH - paddle.getWidth(), paddle.getX());
        paddle.stop();
        assertEquals(PaddleState.IDLE, paddle.getState());
    }

    @Test
    void resetRestoresDefaultDimensionsSpeedAndPosition() {
        Paddle paddle = new Paddle();
        paddle.setWidth(80);
        paddle.setSpeed(900);
        paddle.setX(0);
        paddle.reset();
        assertEquals(GameConfig.PADDLE_WIDTH, paddle.getWidth());
        assertEquals(GameConfig.PADDLE_SPEED, paddle.getSpeed());
        assertEquals((GameConfig.FIELD_WIDTH - GameConfig.PADDLE_WIDTH) / 2.0, paddle.getX());
    }

    @Test
    void velocityTracksActualMovementAndStopsOnWallsStopAndReset() {
        Paddle paddle = new Paddle();
        paddle.moveRight(0.5);
        assertTrue(paddle.getVelocityX() > 0.0);
        assertEquals(paddle.getSpeed(), paddle.getVelocityX(), 0.001);

        paddle.moveLeft(0.5);
        assertTrue(paddle.getVelocityX() < 0.0);
        assertEquals(-paddle.getSpeed(), paddle.getVelocityX(), 0.001);

        paddle.stop();
        assertEquals(0.0, paddle.getVelocityX(), 0.0);

        paddle.setX(0.0);
        paddle.moveLeft(0.5);
        assertEquals(0.0, paddle.getVelocityX(), 0.0);
        paddle.setX(GameConfig.FIELD_WIDTH - paddle.getWidth());
        paddle.moveRight(0.5);
        assertEquals(0.0, paddle.getVelocityX(), 0.0);

        paddle.moveLeft(0.5);
        assertTrue(paddle.getVelocityX() < 0.0);
        paddle.reset();
        assertEquals(0.0, paddle.getVelocityX(), 0.0);
    }

    @Test
    void nonPositiveMovementDeltaThrowsGameException() {
        Paddle paddle = new Paddle();
        assertEquals(se233.arkanoid.exception.ErrorCode.INVALID_ARGUMENT,
                assertThrows(se233.arkanoid.exception.GameException.class, () -> paddle.moveLeft(0.0))
                        .getErrorCode());
        assertEquals(se233.arkanoid.exception.ErrorCode.INVALID_ARGUMENT,
                assertThrows(se233.arkanoid.exception.GameException.class, () -> paddle.moveRight(-0.1))
                        .getErrorCode());
    }
}
