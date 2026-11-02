// @author Wang
package se233.arkanoid.upgrade;

import se233.arkanoid.engine.GameState;

import java.util.Optional;

public interface Upgrade {
    // Demo: each upgrade supplies the same contract, allowing the selection UI to treat types polymorphically.
    String getName();
    String getDescription();
    UpgradeType getType();
    default boolean isCursed() { return false; }
    void apply(GameState state);
    default Optional<Double> getDurationSeconds() { return Optional.empty(); }
}
