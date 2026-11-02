// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class FrenzyUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(FrenzyUpgrade.class);
    public String getName() { return "Frenzy"; }
    public String getDescription() { return "Ball speed +40%, and gain one life."; }
    public UpgradeType getType() { return UpgradeType.FRENZY; }
    public boolean isCursed() { return true; }
    public void apply(GameState state) {
        // Demo: this cursed choice combines faster balls with a compensating extra life.
        state.getBalls().forEach(ball -> ball.setSpeedMultiplier(ball.getSpeedMultiplier() * 1.4));
        state.addLife();
        state.getActiveUpgrades().add(this);
        LOGGER.info("Cursed Frenzy applied: speed +40 percent, one life gained");
    }
}
