// @author Wang
package se233.arkanoid.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameExceptionHandler.class);

    public enum RecoveryAction { SHOW_ERROR, RETURN_TO_MENU, USE_PROCEDURAL_ASSETS, SKIP_UPGRADE }

    private GameExceptionHandler() { }

    public static RecoveryAction handle(GameException exception) {
        // Demo: this is the single policy point mapping each game error code to recovery.
        LOGGER.error("Handled game error [{}]: {}", exception.getErrorCode(), exception.getMessage(), exception);
        return switch (exception.getErrorCode()) {
            case RESOURCE_LOAD_FAILED -> RecoveryAction.USE_PROCEDURAL_ASSETS;
            case UPGRADE_FAILED -> RecoveryAction.SKIP_UPGRADE;
            case INVALID_STAGE, INVALID_STATE -> RecoveryAction.RETURN_TO_MENU;
            case INVALID_ARGUMENT -> RecoveryAction.SHOW_ERROR;
        };
    }
}
