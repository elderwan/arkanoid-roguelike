// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;
import se233.arkanoid.model.Ball;

public final class MultiBallUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiBallUpgrade.class);
    // getName: Returns the current name.
    // Inputs: none.
    // Output/behavior: Returns the computed get name value.
    public String getName() { return "Multiball"; }
    // getDescription: Returns the current description.
    // Inputs: none.
    // Output/behavior: Returns the computed get description value.
    public String getDescription() { return "Double every ball currently in play."; }
    // getType: Returns the current type.
    // Inputs: none.
    // Output/behavior: Returns the computed get type value.
    public UpgradeType getType() { return UpgradeType.MULTI_BALL; }
    // apply: Performs the apply operation.
    // Inputs: state.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void apply(GameState state) {
        // Demo: duplicate the original ball set and preserve each ball's existing relic properties.
        int existing = state.getBalls().size();
        for (int i = 0; i < existing; i++) {
            Ball source = state.getBalls().get(i);
            double direction = i % 2 == 0 ? -1.0 : 1.0;
            Ball extra = new Ball(source.getX(), source.getY(),
                    source.getVx() + direction * 105.0, source.getVy());
            extra.setSpeedMultiplier(source.getSpeedMultiplier());
            extra.setPiercing(source.isPiercing());
            state.getBalls().add(extra);
        }
        state.getActiveUpgrades().add(this);
        LOGGER.info("Multiball upgrade applied; balls={}", state.getBalls().size());
    }
}
