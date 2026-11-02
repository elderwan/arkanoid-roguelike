// @author Oly
package se233.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrickTest {
    @Test
    void normalBrickIsDestroyedByOneHitAndStartsFlash() {
        NormalBrick brick = new NormalBrick(10, 20);
        assertEquals(HitResult.DESTROYED, brick.hit(1));
        assertTrue(brick.isDestroyed());
        assertEquals(0.16, brick.getHitFlashTimer(), 0.0001);
        brick.update(0.06);
        assertEquals(0.10, brick.getHitFlashTimer(), 0.0001);
    }

    @Test
    void strongBrickRequiresThreeHits() {
        StrongBrick brick = new StrongBrick(0, 0);
        assertEquals(HitResult.DAMAGED, brick.hit(1));
        assertEquals(HitResult.DAMAGED, brick.hit(1));
        assertEquals(HitResult.DESTROYED, brick.hit(1));
        assertEquals(0, brick.getHitPoints());
    }

    @Test
    void bonusBrickHasFiftyBasePoints() {
        assertEquals(50, new BonusBrick(0, 0).getScoreValue());
    }
}
