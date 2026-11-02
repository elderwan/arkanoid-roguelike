// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class PiercingBallUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(PiercingBallUpgrade.class);
    public String getName() { return "Phase shot"; }
    public String getDescription() { return "Balls pass through bricks instead of bouncing."; }
    public UpgradeType getType() { return UpgradeType.PIERCING_BALL; }
    public void apply(GameState state) {
        state.getBalls().forEach(ball -> ball.setPiercing(true));
        state.getActiveUpgrades().add(this);
        LOGGER.info("Piercing-ball upgrade applied");
    }
}
