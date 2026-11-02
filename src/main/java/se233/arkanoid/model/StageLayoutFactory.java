// @author Oly
package se233.arkanoid.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.GameConfig;
import se233.arkanoid.exception.ErrorCode;
import se233.arkanoid.exception.GameException;

import java.util.ArrayList;
import java.util.List;

public final class StageLayoutFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(StageLayoutFactory.class);
    private static final double GAP_X = 0.0;
    private static final double GAP_Y = 9.0;

    private StageLayoutFactory() { }

    public static List<Brick> create(int stageNumber) {
        if (stageNumber < 1 || stageNumber > GameConfig.TOTAL_STAGES) {
            throw new GameException(ErrorCode.INVALID_STAGE, "Stage must be between 1 and 5.");
        }
        List<Brick> bricks = new ArrayList<>();
        // Demo: each case defines one stage pattern using polymorphic brick subclasses.
        switch (stageNumber) {
            // Demo: stage one is a four-row baseline layout of normal bricks.
            case 1 -> fillRectangle(bricks, 4, (row, col) -> new NormalBrick(x(col), y(row)));
            // Demo: stage two alternates durable strong bricks with normal bricks.
            case 2 -> fillRectangle(bricks, 5, (row, col) -> (row + col) % 2 == 0
                    ? new StrongBrick(x(col), y(row)) : new NormalBrick(x(col), y(row)));
            case 3 -> {
                // Demo: each row narrows to form a pyramid, with strong bricks on its border.
                for (int row = 0; row < 5; row++) {
                    for (int col = row; col < GameConfig.BRICK_COLUMNS - row; col++) {
                        boolean border = row == 0 || col == row || col == GameConfig.BRICK_COLUMNS - row - 1;
                        bricks.add(border ? new StrongBrick(x(col), y(row)) : new NormalBrick(x(col), y(row)));
                    }
                }
            }
            case 4 -> {
                // Demo: a checkerboard mixes brick durability and places bonus bricks near the center.
                for (int row = 0; row < 5; row++) {
                    for (int col = 0; col < GameConfig.BRICK_COLUMNS; col++) {
                        if ((row + col) % 2 == 0) {
                            bricks.add((row == 2 && (col == 3 || col == 6))
                                    ? new BonusBrick(x(col), y(row))
                                    : ((row + col) % 4 == 0 ? new StrongBrick(x(col), y(row))
                                    : new NormalBrick(x(col), y(row))));
                        }
                    }
                }
            }
            case 5 -> {
                // Demo: the final dense grid places high-value bonus bricks in its central block.
                for (int row = 0; row < 6; row++) {
                    for (int col = 0; col < GameConfig.BRICK_COLUMNS; col++) {
                        boolean center = row >= 2 && row <= 3 && col >= 3 && col <= 6;
                        bricks.add(center ? new BonusBrick(x(col), y(row))
                                : ((row + col) % 3 == 0 ? new StrongBrick(x(col), y(row))
                                : new NormalBrick(x(col), y(row))));
                    }
                }
            }
            default -> throw new GameException(ErrorCode.INVALID_STAGE, "Unsupported stage.");
        }
        LOGGER.info("Created stage {} with {} bricks", stageNumber, bricks.size());
        return bricks;
    }

    private static void fillRectangle(List<Brick> bricks, int rows, BrickCreator creator) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < GameConfig.BRICK_COLUMNS; col++) {
                bricks.add(creator.create(row, col));
            }
        }
    }

    private static double x(int col) { return GameConfig.BRICK_LEFT_MARGIN + col * (GameConfig.BRICK_WIDTH + GAP_X); }
    private static double y(int row) { return GameConfig.BRICK_TOP_MARGIN + row * (GameConfig.BRICK_HEIGHT + GAP_Y); }
    @FunctionalInterface private interface BrickCreator { Brick create(int row, int col); }
}
