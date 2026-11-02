// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class GlassCannonUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlassCannonUpgrade.class);
    // getName: Returns the current name.
    // Inputs: none.
    // Output/behavior: Returns the computed get name value.
    public String getName() { return "Glass cannon"; }
    // getDescription: Returns the current description.
    // Inputs: none.
    // Output/behavior: Returns the computed get description value.
    public String getDescription() { return "Triple score, but shrink the paddle by 30%."; }
    // getType: Returns the current type.
    // Inputs: none.
    // Output/behavior: Returns the computed get type value.
    public UpgradeType getType() { return UpgradeType.GLASS_CANNON; }
    // isCursed: Reports whether cursed is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isCursed() { return true; }
    // apply: Performs the apply operation.
    // Inputs: state.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void apply(GameState state) {
        // Demo: this cursed choice trades paddle width for a threefold score multiplier.
        state.setScoreMultiplier(state.getScoreMultiplier() * 3.0);
        state.getPaddle().setWidth(state.getPaddle().getWidth() * 0.7);
        state.getActiveUpgrades().add(this);
        LOGGER.info("Cursed Glass cannon applied: score x3, paddle width -30 percent");
    }
}
