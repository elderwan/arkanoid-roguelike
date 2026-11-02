// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;
import se233.arkanoid.model.Ball;

public final class MultiBallUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiBallUpgrade.class);
    public String getName() { return "Multiball"; }
    public String getDescription() { return "Double every ball currently in play."; }
    public UpgradeType getType() { return UpgradeType.MULTI_BALL; }
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
