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

    public GameState() { balls.add(Ball.attachedTo(paddle)); }

    public int getScore() { return score; }
    public void addScore(int points) {
        if (points < 0) throw new GameException(ErrorCode.INVALID_ARGUMENT, "Score cannot decrease.");
        score = (int) Math.min(Integer.MAX_VALUE, (long) score + points);
        LOGGER.info("Score changed by {}; new total={}", points, score);
    }
    public int getLives() { return lives; }
    public void addLife() { lives++; LOGGER.info("Extra life gained; lives={}", lives); }
    public void loseLife() {
        if (lives > 0) lives--;
        LOGGER.info("Life lost; lives remaining={}", lives);
        if (lives == 0) {
            gameOver = true;
            LOGGER.info("Game over at score {}", score);
        }
    }
    public int getStageNumber() { return stageNumber; }
    public Paddle getPaddle() { return paddle; }
    public List<Ball> getBalls() { return balls; }
    public List<Brick> getBricks() { return bricks; }
    public List<Laser> getLasers() { return lasers; }
    public List<Upgrade> getActiveUpgrades() { return activeUpgrades; }
    public double getScoreMultiplier() { return scoreMultiplier; }
    public void setScoreMultiplier(double multiplier) {
        if (!Double.isFinite(multiplier) || multiplier <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Score multiplier must be positive.");
        }
        scoreMultiplier = multiplier;
    }
    public int getComboCount() { return comboCount; }
    // Demo: combo scoring starts at x1 and is capped at x5.
    public double getComboMultiplier() { return Math.max(1, Math.min(5, comboCount)); }
    public boolean isLaserActive() { return laserTimeRemaining > 0.0; }
    public double getLaserTimeRemaining() { return laserTimeRemaining; }
    public void activateLaser(double seconds) {
        if (!Double.isFinite(seconds) || seconds <= 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Laser duration must be positive.");
        }
        laserTimeRemaining = seconds;
        // Demo: activating the special attack immediately makes its first volley available.
        laserCooldownRemaining = 0.0;
    }
    public boolean isGameOver() { return gameOver; }
    public boolean isVictory() { return victory; }
    public List<ScoreEvent> drainScoreEvents() {
        // Demo: the renderer consumes each score popup once, preventing it from reappearing every frame.
        List<ScoreEvent> drained = List.copyOf(scoreEvents);
        scoreEvents.clear();
        return drained;
    }
    public List<BrickEffect> drainBrickEffects() {
        List<BrickEffect> drained = List.copyOf(brickEffects);
        brickEffects.clear();
        return drained;
    }
    public void addScoreEvent(ScoreEvent event) { scoreEvents.add(event); }
    public void addBrickEffect(BrickEffect effect) { brickEffects.add(effect); }
    public int incrementCombo() { comboCount = Math.min(5, comboCount + 1); return comboCount; }
    public void resetCombo() { comboCount = 0; }
    public void setStageNumber(int stageNumber) {
        if (stageNumber < 1 || stageNumber > GameConfig.TOTAL_STAGES) {
            throw new GameException(ErrorCode.INVALID_STAGE, "Stage must be between 1 and 5.");
        }
        this.stageNumber = stageNumber;
    }
    public void setVictory(boolean victory) { this.victory = victory; }
    public boolean isBallLaunched() { return ballLaunched; }
    public void setBallLaunched(boolean ballLaunched) { this.ballLaunched = ballLaunched; }
    public double getLaserCooldownRemaining() { return laserCooldownRemaining; }
    public void setLaserCooldownRemaining(double seconds) {
        laserCooldownRemaining = Math.max(0.0, seconds);
    }
    public void updateTimers(double dt) {
        // Demo: both duration and cooldown use elapsed time rather than frame counts.
        laserTimeRemaining = Math.max(0.0, laserTimeRemaining - dt);
        laserCooldownRemaining = Math.max(0.0, laserCooldownRemaining - dt);
    }
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
