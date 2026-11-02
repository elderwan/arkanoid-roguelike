// @author Wang
package se233.arkanoid.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;
import se233.arkanoid.model.Ball;
import se233.arkanoid.model.Brick;
import se233.arkanoid.model.HitResult;
import se233.arkanoid.model.Paddle;

import java.util.ArrayList;
import java.util.List;

public final class CollisionResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollisionResolver.class);

    public record BrickImpact(Brick brick, HitResult result) { }

    // resolveWallCollision: Resolves the requested collision or interaction and reports whether it was handled.
    // Inputs: ball.
    // Output/behavior: Returns a boolean result.
    public boolean resolveWallCollision(Ball ball) {
        boolean bounced = false;
        if (ball.getX() - ball.getRadius() < 0.0) {
            ball.setPosition(ball.getRadius(), ball.getY());
            ball.setVelocity(Math.abs(ball.getVx()), ball.getVy());
            bounced = true;
        } else if (ball.getX() + ball.getRadius() > GameConfig.FIELD_WIDTH) {
            ball.setPosition(GameConfig.FIELD_WIDTH - ball.getRadius(), ball.getY());
            ball.setVelocity(-Math.abs(ball.getVx()), ball.getVy());
            bounced = true;
        }
        if (ball.getY() - ball.getRadius() < 0.0) {
            ball.setPosition(ball.getX(), ball.getRadius());
            ball.setVelocity(ball.getVx(), Math.abs(ball.getVy()));
            bounced = true;
        }
        if (bounced) {
            ball.onBounce();
            LOGGER.debug("Ball bounced from field wall");
        }
        return bounced;
    }

    // resolvePaddleCollision: Resolves the requested collision or interaction and reports whether it was handled.
    // Inputs: ball, paddle.
    // Output/behavior: Returns a boolean result.
    public boolean resolvePaddleCollision(Ball ball, Paddle paddle) {
        return resolvePaddleCollision(ball, paddle, ball.getY());
    }

    // resolvePaddleCollision: Resolves the requested collision or interaction and reports whether it was handled.
    // Inputs: ball, paddle, previousY.
    // Output/behavior: Returns a boolean result.
    public boolean resolvePaddleCollision(Ball ball, Paddle paddle, double previousY) {
        // Demo: only accept a downward ball that reaches the paddle's top from above.
        boolean sweptAcrossTop = previousY <= paddle.getY()
                && ball.getY() + ball.getRadius() >= paddle.getY();
        boolean overlappingTop = ball.getY() <= paddle.getY()
                && ball.getY() + ball.getRadius() >= paddle.getY();
        if (ball.getVy() <= 0.0 || (!sweptAcrossTop && !overlappingTop)
                || ball.getX() + ball.getRadius() < paddle.getX()
                || ball.getX() - ball.getRadius() > paddle.getX() + paddle.getWidth()) {
            return false;
        }
        bounceOffPaddle(ball, paddle);
        return true;
    }

    // bounceOffPaddle: Executes the bounce off paddle operation.
    // Inputs: ball, paddle.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public static void bounceOffPaddle(Ball ball, Paddle paddle) {
        double incomingVx = ball.getVx();
        double incomingVy = ball.getVy();
        double paddleVx = paddle.getVelocityX();
        double outgoingVx = incomingVx;
        double outgoingVy = -Math.abs(incomingVy);

        if (Math.abs(paddleVx) >= GameConfig.PADDLE_STATIONARY_EPSILON) {
            // Demo: stationary paddles mirror-reflect; moving paddles add capped horizontal bias.
            outgoingVx += paddleBias(paddleVx);
            // Demo: clamp the direction, then normalize to preserve the incoming speed.
            outgoingVx = clampBounceAngle(outgoingVx, outgoingVy);

            double incomingSpeed = Math.hypot(incomingVx, incomingVy);
            double outgoingSpeed = Math.hypot(outgoingVx, outgoingVy);
            if (outgoingSpeed > 0.0) {
                double scale = incomingSpeed / outgoingSpeed;
                outgoingVx *= scale;
                outgoingVy *= scale;
            }
        }

        ball.setPosition(ball.getX(), paddle.getY() - ball.getRadius());
        ball.setVelocity(outgoingVx, outgoingVy);
        ball.onBounce();
        double angleDegrees = Math.toDegrees(Math.atan2(Math.abs(outgoingVx), Math.abs(outgoingVy)));
        LOGGER.debug("Paddle bounce paddleVx={} incoming=({}, {}) outgoing=({}, {}) angle={}deg",
                paddleVx, incomingVx, incomingVy, outgoingVx, outgoingVy, angleDegrees);
    }

    // launchFromPaddle: Executes the launch from paddle operation.
    // Inputs: ball, paddle, launchSpeed.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public static void launchFromPaddle(Ball ball, Paddle paddle, double launchSpeed) {
        if (!Double.isFinite(launchSpeed) || launchSpeed <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Ball launch speed must be positive.");
        }
        double paddleVx = paddle.getVelocityX();
        double outgoingVx = 0.0;
        double outgoingVy = -launchSpeed;
        if (Math.abs(paddleVx) >= GameConfig.PADDLE_STATIONARY_EPSILON) {
            // Demo: a stationary launch is vertical; paddle movement deterministically biases the launch.
            outgoingVx = paddleBias(paddleVx);
            outgoingVx = clampBounceAngle(outgoingVx, outgoingVy);
            double outgoingSpeed = Math.hypot(outgoingVx, outgoingVy);
            double scale = launchSpeed / outgoingSpeed;
            outgoingVx *= scale;
            outgoingVy *= scale;
        }
        ball.setVelocity(outgoingVx, outgoingVy);
        LOGGER.debug("Paddle launch paddleVx={} outgoing=({}, {})", paddleVx, outgoingVx, outgoingVy);
    }

    // paddleBias: Executes the paddle bias operation.
    // Inputs: paddleVelocityX.
    // Output/behavior: Returns the computed paddle bias value.
    static double paddleBias(double paddleVelocityX) {
        if (!Double.isFinite(paddleVelocityX)) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Paddle velocity must be finite.");
        }
        // Demo: the bias helper is shared by paddle bounces and launches.
        double bias = GameConfig.PADDLE_INFLUENCE * paddleVelocityX;
        return Math.max(-GameConfig.MAX_PADDLE_BIAS_SPEED,
                Math.min(GameConfig.MAX_PADDLE_BIAS_SPEED, bias));
    }

    // clampBounceAngle: Executes the clamp bounce angle operation.
    // Inputs: vx, vy.
    // Output/behavior: Returns the computed clamp bounce angle value.
    private static double clampBounceAngle(double vx, double vy) {
        double maxHorizontal = Math.abs(vy) * Math.tan(Math.toRadians(GameConfig.MAX_BOUNCE_ANGLE_DEG));
        if (Math.abs(vx) > maxHorizontal) {
            return Math.copySign(maxHorizontal, vx);
        }
        return vx;
    }

    // resolveBrickCollisions: Resolves the requested collision or interaction and reports whether it was handled.
    // Inputs: ball, bricks.
    // Output/behavior: Returns the computed resolve brick collisions value.
    public List<BrickImpact> resolveBrickCollisions(Ball ball, List<Brick> bricks) {
        List<BrickImpact> impacts = new ArrayList<>();
        for (Brick brick : bricks) {
            if (brick.isDestroyed() || !circleIntersectsBrick(ball, brick)) continue;
            HitResult result = brick.hit(1);
            impacts.add(new BrickImpact(brick, result));
            if (!ball.isPiercing()) {
                double overlapX = Math.min(ball.getX() + ball.getRadius() - brick.boundsLeft(),
                        brick.boundsRight() - (ball.getX() - ball.getRadius()));
                double overlapY = Math.min(ball.getY() + ball.getRadius() - brick.boundsTop(),
                        brick.boundsBottom() - (ball.getY() - ball.getRadius()));
                if (overlapX < overlapY) ball.setVelocity(-ball.getVx(), ball.getVy());
                else ball.setVelocity(ball.getVx(), -ball.getVy());
                ball.onBounce();
                break;
            }
        }
        return impacts;
    }

    // circleIntersectsBrick: Executes the circle intersects brick operation.
    // Inputs: ball, brick.
    // Output/behavior: Returns a boolean result.
    private boolean circleIntersectsBrick(Ball ball, Brick brick) {
        double nearestX = Math.max(brick.boundsLeft(), Math.min(ball.getX(), brick.boundsRight()));
        double nearestY = Math.max(brick.boundsTop(), Math.min(ball.getY(), brick.boundsBottom()));
        double dx = ball.getX() - nearestX;
        double dy = ball.getY() - nearestY;
        return dx * dx + dy * dy <= ball.getRadius() * ball.getRadius();
    }
}
