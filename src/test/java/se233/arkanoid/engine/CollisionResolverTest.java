// @author Wang
package se233.arkanoid.engine;

import org.junit.jupiter.api.Test;
import se233.arkanoid.GameConfig;
import se233.arkanoid.model.Ball;
import se233.arkanoid.model.BallState;
import se233.arkanoid.model.NormalBrick;
import se233.arkanoid.model.Paddle;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollisionResolverTest {
    private static final double DELTA = 1.0e-6;
    private final CollisionResolver resolver = new CollisionResolver();

    @Test
    void wallCollisionInvertsOnlyTheRelevantVelocityComponent() {
        Ball left = new Ball(3, 100, -120, 80);
        assertTrue(resolver.resolveWallCollision(left));
        assertTrue(left.getVx() > 0);
        assertEquals(80.0, left.getVy(), 0.001);

        Ball top = new Ball(100, 3, 120, -80);
        assertTrue(resolver.resolveWallCollision(top));
        assertEquals(120.0, top.getVx(), 0.001);
        assertTrue(top.getVy() > 0);
    }

    @Test
    void stationaryPaddleReflectsIncomingVelocityExactly() {
        Paddle paddle = new Paddle();
        Ball downRight = new Ball(paddle.getCenterX(), 650, 150, 300);
        CollisionResolver.bounceOffPaddle(downRight, paddle);
        assertEquals(150.0, downRight.getVx(), DELTA);
        assertEquals(-300.0, downRight.getVy(), DELTA);

        Ball downLeft = new Ball(paddle.getCenterX(), 650, -150, 300);
        CollisionResolver.bounceOffPaddle(downLeft, paddle);
        assertEquals(-150.0, downLeft.getVx(), DELTA);
        assertEquals(-300.0, downLeft.getVy(), DELTA);

        Ball straightDown = new Ball(paddle.getCenterX(), 650, 0, 380);
        CollisionResolver.bounceOffPaddle(straightDown, paddle);
        assertEquals(0.0, straightDown.getVx(), DELTA);
        assertEquals(-380.0, straightDown.getVy(), DELTA);
    }

    @Test
    void stationaryBounceDoesNotDependOnPaddleHitPosition() {
        Paddle paddle = new Paddle();
        double[] expected = null;
        double[] positions = {paddle.getX(), paddle.getCenterX(), paddle.getX() + paddle.getWidth()};
        for (double x : positions) {
            Ball ball = new Ball(x, 650, 120, 310);
            CollisionResolver.bounceOffPaddle(ball, paddle);
            double[] velocity = {ball.getVx(), ball.getVy()};
            if (expected == null) expected = velocity;
            else {
                assertEquals(expected[0], velocity[0], DELTA);
                assertEquals(expected[1], velocity[1], DELTA);
            }
        }
    }

    @Test
    void identicalInputsProduceIdenticalBounceOneHundredTimes() {
        double[] expected = null;
        for (int i = 0; i < 100; i++) {
            Paddle paddle = new Paddle();
            Ball ball = new Ball(300, 650, -177.5, 321.25);
            CollisionResolver.bounceOffPaddle(ball, paddle);
            double[] velocity = {ball.getVx(), ball.getVy()};
            if (expected == null) expected = velocity;
            else {
                assertEquals(expected[0], velocity[0], DELTA);
                assertEquals(expected[1], velocity[1], DELTA);
            }
        }
    }

    @Test
    void movingPaddleBiasesStraightDownBallInItsDirectionAndPreservesSpeed() {
        Paddle right = movingPaddle(400);
        Ball rightward = new Ball(300, 650, 0, 380);
        CollisionResolver.bounceOffPaddle(rightward, right);
        assertTrue(rightward.getVx() > 0);
        assertEquals(380.0, Math.hypot(rightward.getVx(), rightward.getVy()), DELTA);

        Paddle left = movingPaddle(-400);
        Ball leftward = new Ball(300, 650, 0, 380);
        CollisionResolver.bounceOffPaddle(leftward, left);
        assertTrue(leftward.getVx() < 0);
        assertEquals(380.0, Math.hypot(leftward.getVx(), leftward.getVy()), DELTA);
    }

    @Test
    void paddleMovingRightAddsBiasToRightwardAndLeftwardIncomingBalls() {
        Paddle movingRight = movingPaddle(400);
        Ball incomingRight = new Ball(300, 650, 150, 300);
        Ball stationaryRight = new Ball(300, 650, 150, 300);
        CollisionResolver.bounceOffPaddle(incomingRight, movingRight);
        CollisionResolver.bounceOffPaddle(stationaryRight, new Paddle());
        assertTrue(incomingRight.getVx() > stationaryRight.getVx());
        assertEquals(Math.hypot(150, 300), Math.hypot(incomingRight.getVx(), incomingRight.getVy()), DELTA);

        Ball incomingLeft = new Ball(300, 650, -150, 300);
        Ball stationaryLeft = new Ball(300, 650, -150, 300);
        CollisionResolver.bounceOffPaddle(incomingLeft, movingRight);
        CollisionResolver.bounceOffPaddle(stationaryLeft, new Paddle());
        assertTrue(incomingLeft.getVx() > stationaryLeft.getVx());
        assertEquals(Math.hypot(150, 300), Math.hypot(incomingLeft.getVx(), incomingLeft.getVy()), DELTA);
    }

    @Test
    void paddleBiasIsCappedAndBounceAngleNeverExceedsMaximum() {
        assertEquals(GameConfig.MAX_PADDLE_BIAS_SPEED, CollisionResolver.paddleBias(5000), DELTA);
        assertEquals(-GameConfig.MAX_PADDLE_BIAS_SPEED, CollisionResolver.paddleBias(-5000), DELTA);
        assertTrue(Math.abs(CollisionResolver.paddleBias(5000)) <= GameConfig.MAX_PADDLE_BIAS_SPEED);

        Ball ball = new Ball(300, 650, 1000, 1);
        CollisionResolver.bounceOffPaddle(ball, movingPaddle(5000));
        double angle = Math.toDegrees(Math.atan2(Math.abs(ball.getVx()), Math.abs(ball.getVy())));
        assertTrue(angle <= GameConfig.MAX_BOUNCE_ANGLE_DEG + DELTA);
        assertTrue(ball.getVy() < 0);
        assertEquals(Math.hypot(1000, 1), Math.hypot(ball.getVx(), ball.getVy()), DELTA);
    }

    @Test
    void everyBounceLeavesBallUpwardAndAbovePaddleTop() {
        Paddle paddle = movingPaddle(400);
        Ball ball = new Ball(300, 650, -220, 310);
        CollisionResolver.bounceOffPaddle(ball, paddle);
        assertTrue(ball.getVy() < 0);
        assertEquals(paddle.getY() - ball.getRadius(), ball.getY(), DELTA);
        assertEquals(BallState.BOUNCING, ball.getState());
    }

    @Test
    void paddleVelocityBelowStationaryThresholdUsesExactMirrorReflection() {
        Paddle paddle = new Paddle();
        paddle.setSpeed(GameConfig.PADDLE_STATIONARY_EPSILON - DELTA);
        paddle.moveRight(1.0);
        Ball ball = new Ball(300, 650, 150, 300);
        CollisionResolver.bounceOffPaddle(ball, paddle);
        assertEquals(150.0, ball.getVx(), DELTA);
        assertEquals(-300.0, ball.getVy(), DELTA);
    }

    @Test
    void resolverOnlyBouncesOnPaddleTopAndDoesNotPullUpBallFromBelow() {
        Paddle paddle = new Paddle();
        Ball below = new Ball(paddle.getCenterX(), paddle.getY() + 1, 0, 100);
        assertFalse(resolver.resolvePaddleCollision(below, paddle));
        assertEquals(paddle.getY() + 1, below.getY(), DELTA);

        Ball descending = new Ball(paddle.getCenterX(), paddle.getY() - 3, 0, 100);
        assertTrue(resolver.resolvePaddleCollision(descending, paddle));
        assertEquals(paddle.getY() - descending.getRadius(), descending.getY(), DELTA);
        assertTrue(descending.getVy() < 0);
    }

    @Test
    void paddleCollisionResetsComboAndStartsBounceAnimation() {
        GameEngine engine = new GameEngine();
        engine.startRun();
        var state = engine.getState();
        state.incrementCombo();
        state.incrementCombo();
        state.setBallLaunched(true);
        Ball ball = state.getBalls().get(0);
        ball.setPosition(state.getPaddle().getCenterX(), state.getPaddle().getY() - 9);
        ball.setVelocity(0, 100);

        engine.update(0.02);

        assertEquals(0, state.getComboCount());
        assertEquals(BallState.BOUNCING, ball.getState());
        assertTrue(ball.getVy() < 0);
    }

    @Test
    void movingPaddleHelperReturnsBiasClampedBeforeNormalization() {
        assertEquals(GameConfig.MAX_PADDLE_BIAS_SPEED, CollisionResolver.paddleBias(5000), DELTA);
        assertEquals(0.35 * 400, CollisionResolver.paddleBias(400), DELTA);
    }

    @Test
    void piercingBallDestroysBrickWithoutReversingDirection() {
        Ball ball = new Ball(85, 115, 0, -180);
        ball.setPiercing(true);
        NormalBrick brick = new NormalBrick(50, 80);
        var impacts = resolver.resolveBrickCollisions(ball, List.of(brick));
        assertEquals(1, impacts.size());
        assertTrue(brick.isDestroyed());
        assertEquals(-180.0, ball.getVy(), 0.001);
    }

    private static Paddle movingPaddle(double velocityX) {
        Paddle paddle = new Paddle();
        paddle.setSpeed(Math.abs(velocityX));
        paddle.setX(velocityX > 0 ? 0 : GameConfig.FIELD_WIDTH - paddle.getWidth());
        if (velocityX > 0) paddle.moveRight(0.1);
        else paddle.moveLeft(0.1);
        assertEquals(velocityX, paddle.getVelocityX(), DELTA);
        return paddle;
    }
}
