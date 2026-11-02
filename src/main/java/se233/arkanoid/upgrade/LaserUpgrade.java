// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.engine.GameState;

import java.util.Optional;

public final class LaserUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaserUpgrade.class);
    public String getName() { return "Twin lasers"; }
    public String getDescription() { return "Fire dual lasers with SPACE for 15 seconds."; }
    public UpgradeType getType() { return UpgradeType.LASER; }
    public Optional<Double> getDurationSeconds() { return Optional.of(GameConfig.LASER_DURATION_SECONDS); }
    public void apply(GameState state) {
        // Demo: unlike permanent upgrades, this activates a timed special attack.
        state.activateLaser(GameConfig.LASER_DURATION_SECONDS);
        state.getActiveUpgrades().add(this);
        LOGGER.info("Twin laser upgrade activated for {} seconds", GameConfig.LASER_DURATION_SECONDS);
    }
}
