// @author Wang
package se233.arkanoid.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;
import se233.arkanoid.model.Ball;
import se233.arkanoid.model.Brick;
import se233.arkanoid.model.Paddle;
import se233.arkanoid.upgrade.Upgrade;

import java.util.ArrayList;
import java.util.List;

public final class GameState {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameState.class);
    // Demo: GameState owns the mutable run data shared by the engine, upgrades, and HUD.
    private int score;
    private int lives = GameConfig.STARTING_LIVES;
    private int stageNumber = 1;
    private final Paddle paddle = new Paddle();
    private final List<Ball> balls = new ArrayList<>();
    private final List<Brick> bricks = new ArrayList<>();
    private final List<Laser> lasers = new ArrayList<>();
    private final List<Upgrade> activeUpgrades = new ArrayList<>();
    private final List<ScoreEvent> scoreEvents = new ArrayList<>();
    private final List<BrickEffect> brickEffects = new ArrayList<>();
    private double scoreMultiplier = 1.0;
    private int comboCount;
    private double laserTimeRemaining;
    private double laserCooldownRemaining;
    private boolean gameOver;
    private boolean victory;
    private boolean ballLaunched;

    // GameState: Initializes the object for use by the application.
    // Inputs: none.
    // Output/behavior: Initializes the object; no value is returned.
    public GameState() { balls.add(Ball.attachedTo(paddle)); }

    // getScore: Returns the current score.
    // Inputs: none.
    // Output/behavior: Returns the computed get score value.
    public int getScore() { return score; }
    // addScore: Adds the supplied score to the current state.
    // Inputs: points.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void addScore(int points) {
        if (points < 0) throw new GameException(ErrorCode.INVALID_ARGUMENT, "Score cannot decrease.");
        score = (int) Math.min(Integer.MAX_VALUE, (long) score + points);
        LOGGER.info("Score changed by {}; new total={}", points, score);
    }
    // getLives: Returns the current lives.
    // Inputs: none.
    // Output/behavior: Returns the computed get lives value.
    public int getLives() { return lives; }
    // addLife: Adds the supplied life to the current state.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void addLife() { lives++; LOGGER.info("Extra life gained; lives={}", lives); }
    // loseLife: Executes the lose life operation.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void loseLife() {
        if (lives > 0) lives--;
        LOGGER.info("Life lost; lives remaining={}", lives);
        if (lives == 0) {
            gameOver = true;
            LOGGER.info("Game over at score {}", score);
        }
    }
    // getStageNumber: Returns the current stage number.
    // Inputs: none.
    // Output/behavior: Returns the computed get stage number value.
    public int getStageNumber() { return stageNumber; }
    // getPaddle: Returns the current paddle.
    // Inputs: none.
    // Output/behavior: Returns the computed get paddle value.
    public Paddle getPaddle() { return paddle; }
    // getBalls: Returns the current balls.
    // Inputs: none.
    // Output/behavior: Returns the computed get balls value.
    public List<Ball> getBalls() { return balls; }
    // getBricks: Returns the current bricks.
    // Inputs: none.
    // Output/behavior: Returns the computed get bricks value.
    public List<Brick> getBricks() { return bricks; }
    // getLasers: Returns the current lasers.
    // Inputs: none.
    // Output/behavior: Returns the computed get lasers value.
    public List<Laser> getLasers() { return lasers; }
    // getActiveUpgrades: Returns the current active upgrades.
    // Inputs: none.
    // Output/behavior: Returns the computed get active upgrades value.
    public List<Upgrade> getActiveUpgrades() { return activeUpgrades; }
    // getScoreMultiplier: Returns the current score multiplier.
    // Inputs: none.
    // Output/behavior: Returns the computed get score multiplier value.
    public double getScoreMultiplier() { return scoreMultiplier; }
    // setScoreMultiplier: Updates the score multiplier value.
    // Inputs: multiplier.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setScoreMultiplier(double multiplier) {
        if (!Double.isFinite(multiplier) || multiplier <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Score multiplier must be positive.");
        }
        scoreMultiplier = multiplier;
    }
    // getComboCount: Returns the current combo count.
    // Inputs: none.
    // Output/behavior: Returns the computed get combo count value.
    public int getComboCount() { return comboCount; }
    // Demo: combo scoring starts at x1 and is capped at x5.
    // getComboMultiplier: Returns the current combo multiplier.
    // Inputs: none.
    // Output/behavior: Returns the computed get combo multiplier value.
    public double getComboMultiplier() { return Math.max(1, Math.min(5, comboCount)); }
    // isLaserActive: Reports whether laser active is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isLaserActive() { return laserTimeRemaining > 0.0; }
    // getLaserTimeRemaining: Returns the current laser time remaining.
    // Inputs: none.
    // Output/behavior: Returns the computed get laser time remaining value.
    public double getLaserTimeRemaining() { return laserTimeRemaining; }
    // activateLaser: Executes the activate laser operation.
    // Inputs: seconds.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void activateLaser(double seconds) {
        if (!Double.isFinite(seconds) || seconds <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Laser duration must be positive.");
        }
        laserTimeRemaining = seconds;
        // Demo: activating the special attack immediately makes its first volley available.
        laserCooldownRemaining = 0.0;
    }
    // isGameOver: Reports whether game over is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isGameOver() { return gameOver; }
    // isVictory: Reports whether victory is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isVictory() { return victory; }
    // drainScoreEvents: Executes the drain score events operation.
    // Inputs: none.
    // Output/behavior: Returns the computed drain score events value.
    public List<ScoreEvent> drainScoreEvents() {
        // Demo: the renderer consumes each score popup once, preventing it from reappearing every frame.
        List<ScoreEvent> drained = List.copyOf(scoreEvents);
        scoreEvents.clear();
        return drained;
    }
    // drainBrickEffects: Executes the drain brick effects operation.
    // Inputs: none.
    // Output/behavior: Returns the computed drain brick effects value.
    public List<BrickEffect> drainBrickEffects() {
        List<BrickEffect> drained = List.copyOf(brickEffects);
        brickEffects.clear();
        return drained;
    }
    // addScoreEvent: Adds the supplied score event to the current state.
    // Inputs: event.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void addScoreEvent(ScoreEvent event) { scoreEvents.add(event); }
    // addBrickEffect: Adds the supplied brick effect to the current state.
    // Inputs: effect.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void addBrickEffect(BrickEffect effect) { brickEffects.add(effect); }
    // incrementCombo: Executes the increment combo operation.
    // Inputs: none.
    // Output/behavior: Returns the computed increment combo value.
    public int incrementCombo() { comboCount = Math.min(5, comboCount + 1); return comboCount; }
    // resetCombo: Restores the associated state to its initial values.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void resetCombo() { comboCount = 0; }
    // setStageNumber: Updates the stage number value.
    // Inputs: stageNumber.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setStageNumber(int stageNumber) {
        if (stageNumber < 1 || stageNumber > GameConfig.TOTAL_STAGES) {
            throw new GameException(ErrorCode.INVALID_STAGE, "Stage must be between 1 and 5.");
        }
        this.stageNumber = stageNumber;
    }
    // setVictory: Updates the victory value.
    // Inputs: victory.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setVictory(boolean victory) { this.victory = victory; }
    // isBallLaunched: Reports whether ball launched is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isBallLaunched() { return ballLaunched; }
    // setBallLaunched: Updates the ball launched value.
    // Inputs: ballLaunched.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setBallLaunched(boolean ballLaunched) { this.ballLaunched = ballLaunched; }
    // getLaserCooldownRemaining: Returns the current laser cooldown remaining.
    // Inputs: none.
    // Output/behavior: Returns the computed get laser cooldown remaining value.
    public double getLaserCooldownRemaining() { return laserCooldownRemaining; }
    // setLaserCooldownRemaining: Updates the laser cooldown remaining value.
    // Inputs: seconds.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void setLaserCooldownRemaining(double seconds) {
        laserCooldownRemaining = Math.max(0.0, seconds);
    }
    // updateTimers: Advances the associated state using the elapsed time.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void updateTimers(double dt) {
        // Demo: both duration and cooldown use elapsed time rather than frame counts.
        laserTimeRemaining = Math.max(0.0, laserTimeRemaining - dt);
        laserCooldownRemaining = Math.max(0.0, laserCooldownRemaining - dt);
    }
    // resetRun: Restores the associated state to its initial values.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void resetRun() {
        // Demo: restart restores base stats and removes every run-specific entity and relic.
        score = 0;
        lives = GameConfig.STARTING_LIVES;
        stageNumber = 1;
        scoreMultiplier = 1.0;
        comboCount = 0;
        laserTimeRemaining = 0.0;
        laserCooldownRemaining = 0.0;
        gameOver = false;
        victory = false;
        ballLaunched = false;
        paddle.reset();
        balls.clear();
        balls.add(Ball.attachedTo(paddle));
        bricks.clear();
        lasers.clear();
        activeUpgrades.clear();
        scoreEvents.clear();
        brickEffects.clear();
    }
}
