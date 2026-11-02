// @author Oly
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class ScoreBoostUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreBoostUpgrade.class);
    public String getName() { return "High stakes"; }
    public String getDescription() { return "Increase score multiplier by 50%."; }
    public UpgradeType getType() { return UpgradeType.SCORE_BOOST; }
    public void apply(GameState state) {
        state.setScoreMultiplier(state.getScoreMultiplier() * 1.5);
        state.getActiveUpgrades().add(this);
        LOGGER.info("Score multiplier increased to {}", state.getScoreMultiplier());
    }
}
