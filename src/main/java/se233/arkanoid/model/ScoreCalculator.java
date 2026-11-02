// @author Oly
package se233.arkanoid.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;

public final class ScoreCalculator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreCalculator.class);

    private ScoreCalculator() { }

    public static int calculate(int brickScore, double scoreMultiplier, double comboMultiplier) {
        if (brickScore < 0 || !Double.isFinite(scoreMultiplier) || scoreMultiplier < 0.0
                || !Double.isFinite(comboMultiplier) || comboMultiplier < 0.0) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Scoring values must be non-negative.");
        }
        // Demo: one pure formula combines brick value, run upgrade multiplier, and current combo.
        int result = (int) Math.min(Integer.MAX_VALUE,
                Math.round(brickScore * scoreMultiplier * comboMultiplier));
        LOGGER.debug("Calculated score {} from brick={} scoreMultiplier={} combo={}",
                result, brickScore, scoreMultiplier, comboMultiplier);
        return result;
    }
}
