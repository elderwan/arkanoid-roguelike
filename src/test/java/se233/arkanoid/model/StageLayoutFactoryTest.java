// @author Oly
package se233.arkanoid.model;

import org.junit.jupiter.api.Test;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;

import static org.junit.jupiter.api.Assertions.*;

class StageLayoutFactoryTest {
    @Test
    void createsNonEmptyLayoutsWithinFieldForAllFiveStages() {
        for (int stage = 1; stage <= 5; stage++) {
            var bricks = StageLayoutFactory.create(stage);
            assertFalse(bricks.isEmpty());
            assertTrue(bricks.stream().allMatch(brick -> brick.boundsRight() <= 800.0));
        }
    }

    @Test
    void rejectsStagesOutsideSupportedRange() {
        GameException low = assertThrows(GameException.class, () -> StageLayoutFactory.create(0));
        GameException high = assertThrows(GameException.class, () -> StageLayoutFactory.create(6));
        assertEquals(ErrorCode.INVALID_STAGE, low.getErrorCode());
        assertEquals(ErrorCode.INVALID_STAGE, high.getErrorCode());
    }
}
