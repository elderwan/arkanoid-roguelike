// @author Oly
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class PaddleSizeUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaddleSizeUpgrade.class);
    // getName: Returns the current name.
    // Inputs: none.
    // Output/behavior: Returns the computed get name value.
    public String getName() { return "Wide catch"; }
    // getDescription: Returns the current description.
    // Inputs: none.
    // Output/behavior: Returns the computed get description value.
    public String getDescription() { return "Increase paddle width by 25%."; }
    // getType: Returns the current type.
    // Inputs: none.
    // Output/behavior: Returns the computed get type value.
    public UpgradeType getType() { return UpgradeType.PADDLE_SIZE; }
    // apply: Performs the apply operation.
    // Inputs: state.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void apply(GameState state) {
        state.getPaddle().setWidth(Math.min(800.0, state.getPaddle().getWidth() * 1.25));
        state.getActiveUpgrades().add(this);
        LOGGER.info("Paddle width increased to {}", state.getPaddle().getWidth());
    }
}
