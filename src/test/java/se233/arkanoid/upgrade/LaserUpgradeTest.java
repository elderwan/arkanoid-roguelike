// @author Wang
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;
import se233.arkanoid.engine.GameEngine;
import se233.arkanoid.engine.GameState;
import se233.arkanoid.model.NormalBrick;

import static org.junit.jupiter.api.Assertions.*;

class LaserUpgradeTest {
    @Test
    void activatesTimedLaserAndFiresTwoProjectilesWithCooldown() {
        GameEngine engine = new GameEngine();
        GameState state = engine.getState();
        LaserUpgrade upgrade = new LaserUpgrade();
        upgrade.apply(state);
        assertTrue(state.isLaserActive());
        assertEquals(15.0, state.getLaserTimeRemaining(), 0.001);
        engine.fireLasers();
        assertEquals(2, state.getLasers().size());
        assertTrue(state.getLaserCooldownRemaining() > 0.0);
        engine.fireLasers();
        assertEquals(2, state.getLasers().size());
    }

    @Test
    void laserDamagesOneBrickAndIsRemovedImmediately() {
        GameEngine engine = new GameEngine();
        engine.startRun();
        GameState state = engine.getState();
        state.getBricks().clear();
        NormalBrick brick = new NormalBrick(330, 640);
        state.getBricks().add(brick);
        new LaserUpgrade().apply(state);
        engine.fireLasers();

        engine.update(0.02);

        assertTrue(brick.isDestroyed());
        assertFalse(state.getLasers().stream().anyMatch(laser -> laser.getX() == 348.0));
    }
}
