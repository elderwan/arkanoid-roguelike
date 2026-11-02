// @author Wang
package se233.arkanoid;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.exception.GameException;
import se233.arkanoid.exception.GameExceptionHandler;

public final class ArkanoidApp extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArkanoidApp.class);

    @Override
    // start: Executes the start operation.
    // Inputs: primaryStage.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public void start(Stage primaryStage) {
        // Demo: uncaught GameExceptions on any thread are routed to the centralized handler.
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            if (throwable instanceof GameException gameException) {
                GameExceptionHandler.handle(gameException);
            } else {
                LOGGER.error("Uncaught application error on thread {}", thread.getName(), throwable);
            }
        });
        new GameController(primaryStage);
        primaryStage.show();
        LOGGER.info("Arkanoid Roguelike application started");
    }
}
