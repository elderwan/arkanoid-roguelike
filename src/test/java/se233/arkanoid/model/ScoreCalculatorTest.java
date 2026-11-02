// @author Oly
package se233.arkanoid.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreCalculatorTest {
    @Test
    void appliesScoreAndComboMultipliersAndRounds() {
        assertEquals(75, ScoreCalculator.calculate(50, 1.5, 1.0));
        assertEquals(113, ScoreCalculator.calculate(50, 1.5, 1.5));
    }

    @Test
    void zeroBrickValueAwardsNoPoints() {
        assertEquals(0, ScoreCalculator.calculate(0, 3.0, 5.0));
    }
}
