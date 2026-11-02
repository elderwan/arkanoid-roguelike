// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class BallSpeedUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(BallSpeedUpgrade.class);
    // getName: Returns the current name.
    // Inputs: none.
    // Output/behavior: Returns the computed get name value.
    public String getName() { return "Overclocked core"; }
    // getDescription: Returns the current description.
    // Inputs: none.
    // Output/behavior: Returns the computed get description value.
    public String getDescription() { return "Increase all ball speeds by 20%."; }
    // getType: Returns the current type.
    // Inputs: none.
    // Output/behavior: Returns the computed get type value.
    public UpgradeType getType() { return UpgradeType.BALL_SPEED; }
    // apply: Performs the apply operation.
    // Inputs: state.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void apply(GameState state) {
        state.getBalls().forEach(ball -> ball.setSpeedMultiplier(ball.getSpeedMultiplier() * 1.2));
        state.getActiveUpgrades().add(this);
        LOGGER.info("Ball speed increased by 20 percent");
    }
}
