// @author Wang
package se233.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BallTest {
    @Test
    void updatesPositionAndBounceAnimationExpires() {
        Ball ball = new Ball(10, 20, 100, -50);
        ball.update(0.2);
        assertEquals(30.0, ball.getX(), 0.001);
        assertEquals(10.0, ball.getY(), 0.001);
        ball.onBounce();
        assertEquals(BallState.BOUNCING, ball.getState());
        ball.update(0.2);
        assertEquals(BallState.FLYING, ball.getState());
        assertEquals(0.0, ball.getBounceAnimationTime(), 0.001);
    }

    @Test
    void speedMultiplierScalesVelocityAndPiercingFlagCanBeSet() {
        Ball ball = new Ball(0, 0, 100, -100);
        ball.setSpeedMultiplier(1.5);
        assertEquals(150.0, ball.getVx(), 0.001);
        assertEquals(-150.0, ball.getVy(), 0.001);
        ball.setPiercing(true);
        assertTrue(ball.isPiercing());
    }
}
