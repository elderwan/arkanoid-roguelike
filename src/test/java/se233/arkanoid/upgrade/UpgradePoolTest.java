// @author Wang
package se233.arkanoid.upgrade;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UpgradePoolTest {
    @Test
    void offersThreeDistinctChoices() {
        var choices = new UpgradePool(new Random(42)).getChoices();
        assertEquals(3, choices.size());
        assertEquals(3, new HashSet<>(choices.stream().map(Upgrade::getType).toList()).size());
    }
}
