// @author Wang
package se233.arkanoid.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;
import se233.arkanoid.model.Ball;
import se233.arkanoid.model.StageLayoutFactory;
import se233.arkanoid.upgrade.UpgradeType;

public final class StageManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(StageManager.class);

    // loadStage: Executes the load stage operation.
    // Inputs: state, stageNumber.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void loadStage(GameState state, int stageNumber) {
        if (stageNumber < 1 || stageNumber > GameConfig.TOTAL_STAGES) {
            throw new GameException(ErrorCode.INVALID_STAGE, "Cannot load stage " + stageNumber);
        }
        state.setStageNumber(stageNumber);
        state.getBricks().clear();
        state.getBricks().addAll(StageLayoutFactory.create(stageNumber));
        // Demo: rebuild stage entities but reapply persistent upgrades to new balls.
        state.getLasers().clear();
        state.getBalls().clear();
        long multiBallUpgrades = state.getActiveUpgrades().stream()
                .filter(upgrade -> upgrade.getType() == UpgradeType.MULTI_BALL).count();
        int ballCount = (int) Math.min(8, 1L << Math.min(3, multiBallUpgrades));
        double speedMultiplier = 1.0;
        boolean piercing = false;
        for (var upgrade : state.getActiveUpgrades()) {
            if (upgrade.getType() == UpgradeType.BALL_SPEED) speedMultiplier *= 1.2;
            if (upgrade.getType() == UpgradeType.FRENZY) speedMultiplier *= 1.4;
            if (upgrade.getType() == UpgradeType.PIERCING_BALL) piercing = true;
        }
        for (int i = 0; i < ballCount; i++) {
            Ball ball = Ball.attachedTo(state.getPaddle());
            ball.setSpeedMultiplier(speedMultiplier);
            ball.setPiercing(piercing);
            state.getBalls().add(ball);
        }
        state.setBallLaunched(false);
        LOGGER.info("Stage {} started; ball speed multiplier is {}", stageNumber, speedMultiplier(stageNumber));
    }

    // advance: Executes the advance operation.
    // Inputs: state.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void advance(GameState state) {
        int nextStage = state.getStageNumber() + 1;
        if (nextStage > GameConfig.TOTAL_STAGES) {
            // Demo: clearing the final stage ends the run with victory instead of loading another layout.
            state.setVictory(true);
            LOGGER.info("Victory! Final score={}", state.getScore());
            return;
        }
        loadStage(state, nextStage);
    }

    // speedMultiplier: Executes the speed multiplier operation.
    // Inputs: stageNumber.
    // Output/behavior: Returns the computed speed multiplier value.
    public double speedMultiplier(int stageNumber) {
        if (stageNumber < 1 || stageNumber > GameConfig.TOTAL_STAGES) {
            throw new GameException(ErrorCode.INVALID_STAGE, "Cannot calculate speed for stage " + stageNumber);
        }
        // Demo: each stage increases base ball speed by six percent relative to stage one.
        return Math.pow(1.06, stageNumber - 1);
    }
}
