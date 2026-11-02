// @author Oly
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameState;

public final class PaddleSizeUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaddleSizeUpgrade.class);
    public String getName() { return "Wide catch"; }
    public String getDescription() { return "Increase paddle width by 25%."; }
    public UpgradeType getType() { return UpgradeType.PADDLE_SIZE; }
    public void apply(GameState state) {
        state.getPaddle().setWidth(Math.min(800.0, state.getPaddle().getWidth() * 1.25));
        state.getActiveUpgrades().add(this);
        LOGGER.info("Paddle width increased to {}", state.getPaddle().getWidth());
    }
}
