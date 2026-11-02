// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class BallSpeedUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(BallSpeedUpgrade.class);
    public String getName() { return "Overclocked core"; }
    public String getDescription() { return "Increase all ball speeds by 20%."; }
    public UpgradeType getType() { return UpgradeType.BALL_SPEED; }
    public void apply(GameState state) {
        state.getBalls().forEach(ball -> ball.setSpeedMultiplier(ball.getSpeedMultiplier() * 1.2));
        state.getActiveUpgrades().add(this);
        LOGGER.info("Ball speed increased by 20 percent");
    }
}
