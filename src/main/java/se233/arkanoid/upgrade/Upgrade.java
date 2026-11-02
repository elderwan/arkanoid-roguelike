// @author Wang
package se233.arkanoid.upgrade;

import se233.arkanoid.engine.GameState;

import java.util.Optional;

public interface Upgrade {
    // Demo: each upgrade supplies the same contract, allowing the selection UI to treat types polymorphically.
    // getName: Returns the display name used for this upgrade.
    // Inputs: none.
    // Output/behavior: Returns the upgrade's display name.
    String getName();
    // getDescription: Returns the gameplay description shown to the player.
    // Inputs: none.
    // Output/behavior: Returns the upgrade's description.
    String getDescription();
    // getType: Returns the category used to apply and persist this upgrade.
    // Inputs: none.
    // Output/behavior: Returns the upgrade type.
    UpgradeType getType();
    // isCursed: Reports whether cursed is true.
    // Inputs: none.
    // Output/behavior: Returns a boolean result.
    default boolean isCursed() { return false; }
    // apply: Applies this upgrade's effects to the supplied game state.
    // Inputs: state.
    // Output/behavior: Mutates the game state and records the applied upgrade.
    void apply(GameState state);
    // getDurationSeconds: Returns the current duration seconds.
    // Inputs: none.
    // Output/behavior: Returns the computed get duration seconds value.
    default Optional<Double> getDurationSeconds() { return Optional.empty(); }
}
