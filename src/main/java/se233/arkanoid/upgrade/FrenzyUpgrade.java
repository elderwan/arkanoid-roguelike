// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class FrenzyUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrenzyUpgrade.class);
    // getName: Returns the current name.
    // Inputs: none.
    // Output/behavior: Returns the computed get name value.
    public String getName() { return "Frenzy"; }
    // getDescription: Returns the current description.
    // Inputs: none.
    // Output/behavior: Returns the computed get description value.
    public String getDescription() { return "Ball speed +40%, and gain one life."; }
    // getType: Returns the current type.
    // Inputs: none.
    // Output/behavior: Returns the computed get type value.
    public UpgradeType getType() { return UpgradeType.FRENZY; }
    // isCursed: Reports whether cursed is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    public boolean isCursed() { return true; }
    // apply: Performs the apply operation.
    // Inputs: state.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void apply(GameState state) {
        // Demo: this cursed choice combines faster balls with a compensating extra life.
        state.getBalls().forEach(ball -> ball.setSpeedMultiplier(ball.getSpeedMultiplier() * 1.4));
        state.addLife();
        state.getActiveUpgrades().add(this);
        LOGGER.info("Cursed Frenzy applied: speed +40 percent, one life gained");
    }
}
