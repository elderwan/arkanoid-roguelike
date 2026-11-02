// @author Oly
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class PaddleSpeedUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaddleSpeedUpgrade.class);
    public String getName() { return "Quick hands"; }
    public String getDescription() { return "Increase paddle speed by 25%."; }
    public UpgradeType getType() { return UpgradeType.PADDLE_SPEED; }
    public void apply(GameState state) {
        state.getPaddle().setSpeed(state.getPaddle().getSpeed() * 1.25);
        state.getActiveUpgrades().add(this);
        LOGGER.info("Paddle speed increased to {}", state.getPaddle().getSpeed());
    }
}
