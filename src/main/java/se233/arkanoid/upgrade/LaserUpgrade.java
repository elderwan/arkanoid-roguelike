// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.engine.GameState;

import java.util.Optional;

public final class LaserUpgrade implements Upgrade {
    private static final Logger LOGGER = LoggerFactory.getLogger(LaserUpgrade.class);
    // getName: Returns the current name.
    // Inputs: none.
    // Output/behavior: Returns the computed get name value.
    public String getName() { return "Twin lasers"; }
    // getDescription: Returns the current description.
    // Inputs: none.
    // Output/behavior: Returns the computed get description value.
    public String getDescription() { return "Fire dual lasers with SPACE for 15 seconds."; }
    // getType: Returns the current type.
    // Inputs: none.
    // Output/behavior: Returns the computed get type value.
    public UpgradeType getType() { return UpgradeType.LASER; }
    // getDurationSeconds: Returns the current duration seconds.
    // Inputs: none.
    // Output/behavior: Returns the computed get duration seconds value.
    public Optional<Double> getDurationSeconds() { return Optional.of(GameConfig.LASER_DURATION_SECONDS); }
    // apply: Performs the apply operation.
    // Inputs: state.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void apply(GameState state) {
        // Demo: unlike permanent upgrades, this activates a timed special attack.
        state.activateLaser(GameConfig.LASER_DURATION_SECONDS);
        state.getActiveUpgrades().add(this);
        LOGGER.info("Twin laser upgrade activated for {} seconds", GameConfig.LASER_DURATION_SECONDS);
    }
}
