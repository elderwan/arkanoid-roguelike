// @author Wang
package se233.arkanoid.upgrade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class UpgradePool {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpgradePool.class);
    private final Random random;
    private final List<Upgrade> upgrades;

    // UpgradePool: Initializes the object for use by the application.
    // Inputs: none.
    // Output/behavior: Initializes the object; no value is returned.
    public UpgradePool() { this(new Random()); }

    // UpgradePool: Initializes the object for use by the application.
    // Inputs: random.
    // Output/behavior: Initializes the object; no value is returned.
    public UpgradePool(Random random) {
        this.random = random;
        this.upgrades = List.of(new MultiBallUpgrade(), new PiercingBallUpgrade(), new LaserUpgrade(),
                new BallSpeedUpgrade(), new PaddleSizeUpgrade(), new PaddleSpeedUpgrade(),
                new ExtraLifeUpgrade(), new ScoreBoostUpgrade(), new GlassCannonUpgrade(), new FrenzyUpgrade());
    }

    // getChoices: Returns the current choices.
    // Inputs: none.
    // Output/behavior: Returns the computed get choices value.
    public List<Upgrade> getChoices() {
        List<Upgrade> shuffled = new ArrayList<>(upgrades);
        // Demo: shuffle a copy so the master pool is unchanged; taking three gives distinct choices.
        Collections.shuffle(shuffled, random);
        List<Upgrade> choices = List.copyOf(shuffled.subList(0, GameConfig.UPGRADE_CHOICES));
        LOGGER.info("Offered upgrades: {}", choices.stream().map(Upgrade::getName).toList());
        return choices;
    }

    // getChoices: Returns the current choices.
    // Inputs: count.
    // Output/behavior: Returns the computed get choices value.
    public List<Upgrade> getChoices(int count) {
        if (count < 1 || count > upgrades.size()) {
            throw new GameException(ErrorCode.INVALID_ARGUMENT, "Upgrade choice count is out of range.");
        }
        List<Upgrade> shuffled = new ArrayList<>(upgrades);
        Collections.shuffle(shuffled, random);
        return List.copyOf(shuffled.subList(0, count));
    }
}
