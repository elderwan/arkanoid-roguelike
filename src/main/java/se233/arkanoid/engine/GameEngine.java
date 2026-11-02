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
import se233.arkanoid.model.ScoreCalculator;

import java.util.Iterator;

public final class GameEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameEngine.class);
    private final GameState state;
    private final CollisionResolver collisionResolver;
    private final StageManager stageManager;
    private boolean stageComplete;

    // GameEngine: Initializes the object for use by the application.
    // Inputs: none.
    // Output/behavior: Initializes the object; no value is returned.
    public GameEngine() { this(new GameState(), new CollisionResolver(), new StageManager()); }

    // GameEngine: Initializes the object for use by the application.
    // Inputs: state, collisionResolver, stageManager.
    // Output/behavior: Initializes the object; no value is returned.
    public GameEngine(GameState state, CollisionResolver collisionResolver, StageManager stageManager) {
        this.state = state;
        this.collisionResolver = collisionResolver;
        this.stageManager = stageManager;
    }

    // getState: Returns the current state.
    // Inputs: none.
    // Output/behavior: Returns the computed get state value.
    public GameState getState() { return state; }
    // isStageComplete: Reports whether stage complete is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isStageComplete() { return stageComplete; }

    // startRun: Performs the start run operation.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void startRun() {
        // Demo: reset the run, then let StageManager create the first playable stage.
        state.resetRun();
        stageComplete = false;
        stageManager.loadStage(state, 1);
        LOGGER.info("New game started");
    }

    // launchBall: Performs the launch ball operation.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void launchBall() {
        if (state.isGameOver() || state.isVictory() || state.isBallLaunched()) return;
        for (Ball ball : state.getBalls()) {
            double speed = GameConfig.BALL_BASE_SPEED * stageManager.speedMultiplier(state.getStageNumber())
                    * ball.getSpeedMultiplier();
            // Demo: launch direction is deterministic and uses the paddle's current horizontal velocity.
            CollisionResolver.launchFromPaddle(ball, state.getPaddle(), speed);
        }
        state.setBallLaunched(true);
        LOGGER.info("Player launched the ball");
    }

    // fireLasers: Performs the fire lasers operation.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void fireLasers() {
        if (!state.isLaserActive() || state.getLaserCooldownRemaining() > 0.0) return;
        double y = state.getPaddle().getY();
        state.getLasers().add(new Laser(state.getPaddle().getX() + 8.0, y, GameConfig.LASER_SPEED));
        state.getLasers().add(new Laser(state.getPaddle().getX() + state.getPaddle().getWidth() - 8.0, y,
                GameConfig.LASER_SPEED));
        state.setLaserCooldownRemaining(GameConfig.LASER_COOLDOWN_SECONDS);
        LOGGER.debug("Player fired a dual-laser volley");
    }

    // update: Advances the associated state using the elapsed time.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void update(double dt) {
        if (!Double.isFinite(dt) || dt < 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Time delta must be finite and non-negative.");
        }
        if (state.isGameOver() || state.isVictory() || stageComplete) return;
        state.updateTimers(dt);
        state.getPaddle().update(dt);
        for (Brick brick : state.getBricks()) brick.update(dt);
        if (!state.isBallLaunched()) {
            for (Ball ball : state.getBalls()) {
                ball.setPosition(state.getPaddle().getCenterX(), state.getPaddle().getY() - ball.getRadius());
            }
        } else {
            updateBalls(dt);
        }
        updateLasers(dt);
        if (state.getBricks().stream().allMatch(Brick::isDestroyed)) {
            // Demo: the controller observes this flag and opens the upgrade-selection screen.
            stageComplete = true;
            LOGGER.info("Stage {} cleared", state.getStageNumber());
        }
    }

    // updateBalls: Advances the associated state using the elapsed time.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void updateBalls(double dt) {
        Iterator<Ball> iterator = state.getBalls().iterator();
        while (iterator.hasNext()) {
            Ball ball = iterator.next();
            // Demo: preserve the prior position to detect a top-surface crossing, not a side/bottom collision.
            double previousY = ball.getY();
            ball.update(dt);
            collisionResolver.resolveWallCollision(ball);
            if (collisionResolver.resolvePaddleCollision(ball, state.getPaddle(), previousY)) {
                // Demo: a successful paddle return breaks the consecutive-hit combo.
                state.resetCombo();
            }
            for (CollisionResolver.BrickImpact impact : collisionResolver.resolveBrickCollisions(ball, state.getBricks())) {
                Brick brick = impact.brick();
                boolean destroyed = impact.result() == HitResult.DESTROYED;
                state.addBrickEffect(new BrickEffect(brick.getX() + brick.getWidth() / 2.0,
                        brick.getY() + brick.getHeight() / 2.0, destroyed));
                if (destroyed) {
                    // Demo: points use the current combo before the successful brick break increments it.
                    int points = ScoreCalculator.calculate(brick.getScoreValue(), state.getScoreMultiplier(),
                            state.getComboMultiplier());
                    state.addScore(points);
                    state.addScoreEvent(new ScoreEvent(points, state.getScore(),
                            brick.getX() + brick.getWidth() / 2.0, brick.getY()));
                    // Demo: increase the combo only after scoring so the first break is x1.
                    state.incrementCombo();
                    LOGGER.info("Brick destroyed; score gained={}, combo={}", points, state.getComboCount());
                }
            }
            if (ball.getY() - ball.getRadius() > GameConfig.FIELD_HEIGHT) iterator.remove();
        }
        if (state.getBalls().isEmpty()) {
            // Demo: a life is lost only after every ball has left the field.
            state.loseLife();
            state.resetCombo();
            if (!state.isGameOver()) {
                state.getBalls().add(Ball.attachedTo(state.getPaddle()));
                state.setBallLaunched(false);
            }
        }
    }

    // updateLasers: Advances the associated state using the elapsed time.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void updateLasers(double dt) {
        for (Laser laser : state.getLasers()) laser.update(dt);
        Iterator<Laser> iterator = state.getLasers().iterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            if (laser.getY() < 0.0 || !laser.isActive()) {
                iterator.remove();
                continue;
            }
            for (Brick brick : state.getBricks()) {
                // Demo: each projectile damages the first intersected live brick and is then consumed.
                if (!brick.isDestroyed() && laser.getX() >= brick.boundsLeft()
                        && laser.getX() <= brick.boundsRight()
                        && laser.getY() >= brick.boundsTop() && laser.getY() <= brick.boundsBottom()) {
                    HitResult result = brick.hit(1);
                    boolean destroyed = result == HitResult.DESTROYED;
                    state.addBrickEffect(new BrickEffect(brick.getX() + brick.getWidth() / 2.0,
                            brick.getY() + brick.getHeight() / 2.0, destroyed));
                    if (destroyed) {
                        int points = ScoreCalculator.calculate(brick.getScoreValue(), state.getScoreMultiplier(),
                                state.getComboMultiplier());
                        state.addScore(points);
                        state.addScoreEvent(new ScoreEvent(points, state.getScore(), brick.getX(), brick.getY()));
                        state.incrementCombo();
                    }
                    laser.deactivate();
                    break;
                }
            }
            if (!laser.isActive()) iterator.remove();
        }
    }

    // advanceStage: Performs the advance stage operation.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void advanceStage() {
        if (!stageComplete) return;
        stageComplete = false;
        stageManager.advance(state);
    }
}
