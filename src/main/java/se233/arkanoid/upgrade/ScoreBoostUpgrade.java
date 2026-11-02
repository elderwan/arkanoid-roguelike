// @author Oly
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class ScoreBoostUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreBoostUpgrade.class);
    // getName: Returns the current name.
    // Inputs: none.
    // Output/behavior: Returns the computed get name value.
    public String getName() { return "High stakes"; }
    // getDescription: Returns the current description.
    // Inputs: none.
    // Output/behavior: Returns the computed get description value.
    public String getDescription() { return "Increase score multiplier by 50%."; }
    // getType: Returns the current type.
    // Inputs: none.
    // Output/behavior: Returns the computed get type value.
    public UpgradeType getType() { return UpgradeType.SCORE_BOOST; }
    // apply: Performs the apply operation.
    // Inputs: state.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void apply(GameState state) {
        state.setScoreMultiplier(state.getScoreMultiplier() * 1.5);
        state.getActiveUpgrades().add(this);
        LOGGER.info("Score multiplier increased to {}", state.getScoreMultiplier());
    }
}
