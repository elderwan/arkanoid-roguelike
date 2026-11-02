// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class GlassCannonUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlassCannonUpgrade.class);
    public String getName() { return "Glass cannon"; }
    public String getDescription() { return "Triple score, but shrink the paddle by 30%."; }
    public UpgradeType getType() { return UpgradeType.GLASS_CANNON; }
    public boolean isCursed() { return true; }
    public void apply(GameState state) {
        // Demo: this cursed choice trades paddle width for a threefold score multiplier.
        state.setScoreMultiplier(state.getScoreMultiplier() * 3.0);
        state.getPaddle().setWidth(state.getPaddle().getWidth() * 0.7);
        state.getActiveUpgrades().add(this);
        LOGGER.info("Cursed Glass cannon applied: score x3, paddle width -30 percent");
    }
}
