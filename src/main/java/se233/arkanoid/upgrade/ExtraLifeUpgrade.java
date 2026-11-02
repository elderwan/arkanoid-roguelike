// @author Oly
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class ExtraLifeUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtraLifeUpgrade.class);
    public String getName() { return "Spare heart"; }
    public String getDescription() { return "Gain one extra life."; }
    public UpgradeType getType() { return UpgradeType.EXTRA_LIFE; }
    public void apply(GameState state) {
        state.addLife();
        state.getActiveUpgrades().add(this);
        LOGGER.info("Extra life upgrade applied");
    }
}
