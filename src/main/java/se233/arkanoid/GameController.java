// @author Wang
package se233.arkanoid;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se233.arkanoid.engine.GameEngine;
import se233.arkanoid.engine.GameState;
import se233.arkanoid.exception.GameException;
import se233.arkanoid.exception.GameExceptionHandler;
import se233.arkanoid.render.GameRenderer;
import se233.arkanoid.ui.GameOverView;
import se233.arkanoid.ui.MainMenuView;
import se233.arkanoid.ui.StageClearBanner;
import se233.arkanoid.ui.UpgradeSelectionView;
import se233.arkanoid.ui.VictoryView;
import se233.arkanoid.upgrade.Upgrade;
import se233.arkanoid.upgrade.UpgradePool;

import java.util.EnumSet;
import java.util.Set;

public final class GameController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    // Demo: this enum is the UI state machine for menu, play, upgrade, and result screens.
    private enum Screen { MENU, PLAYING, UPGRADE_SELECTION, GAME_OVER, VICTORY }

    private final Stage stage;
    private final Canvas canvas = new Canvas(GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT);
    private final GameEngine engine = new GameEngine();
    private final GameRenderer renderer = new GameRenderer();
    private final MainMenuView menuView = new MainMenuView();
    private final GameOverView gameOverView = new GameOverView();
    private final VictoryView victoryView = new VictoryView();
    private final StageClearBanner stageClearBanner = new StageClearBanner();
    private final UpgradePool upgradePool = new UpgradePool();
    private final Set<KeyCode> heldKeys = EnumSet.noneOf(KeyCode.class);
    private final AnimationTimer timer;
    private Screen screen = Screen.MENU;
    private UpgradeSelectionView upgradeView;
    private long previousFrame;
    private boolean paused;
    private String errorMessage = "";
    private double errorRemaining;

    // GameController: Initializes the object for use by the application.
    // Inputs: stage.
    // Output/behavior: Initializes the object; no value is returned.
    public GameController(Stage stage) {
        this.stage = stage;
        canvas.setFocusTraversable(true);
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT);
        stage.setTitle("Arkanoid Roguelike");
        stage.setResizable(false);
        stage.setScene(scene);
        scene.setOnKeyPressed(event -> onKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> onKeyReleased(event.getCode()));
        canvas.setOnMouseClicked(event -> onMouseClicked(event.getX(), event.getY()));
        timer = new AnimationTimer() {
            @Override
            // handle: Processes the supplied event or input and updates the related state.
            // Inputs: now.
            // Output/behavior: Updates state or performs side effects; no value is returned.
            public void handle(long now) {
                // Demo: all per-frame GameExceptions enter the same recovery handler here.
                try {
                    tick(now);
                } catch (GameException exception) {
                    recover(exception);
                }
            }
        };
        timer.start();
        canvas.requestFocus();
    }

    // tick: Performs the related controller operation and updates the visible application state.
    // Inputs: now.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void tick(long now) {
        // Demo: cap frame delta to keep physics stable after a slow or interrupted frame.
        double dt = previousFrame == 0 ? 0.0 : Math.min(0.05, (now - previousFrame) / 1_000_000_000.0);
        previousFrame = now;
        errorRemaining = Math.max(0.0, errorRemaining - dt);
        if (errorRemaining == 0.0) errorMessage = "";

        if (screen == Screen.PLAYING && !paused) {
            updatePaddle(dt);
            engine.update(dt);
            GameState state = engine.getState();
            if (state.isGameOver()) screen = Screen.GAME_OVER;
            else if (engine.isStageComplete()) {
                if (state.getStageNumber() == GameConfig.TOTAL_STAGES) {
                    engine.advanceStage();
                    screen = Screen.VICTORY;
                } else openUpgradeSelection();
            }
        } else if (screen == Screen.PLAYING) {
            engine.getState().getPaddle().stop();
        }
        render(dt);
    }

    // render: Renders the associated visual element using the supplied graphics context.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void render(double dt) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        switch (screen) {
            case MENU -> menuView.render(gc, GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT);
            case PLAYING -> renderer.render(gc, engine.getState(), dt, paused, visibleError());
            case UPGRADE_SELECTION -> {
                renderer.render(gc, engine.getState(), dt, true, visibleError());
                stageClearBanner.render(gc, engine.getState().getStageNumber());
                upgradeView.render(gc, GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT,
                        engine.getState().getStageNumber());
                drawError(gc);
            }
            case GAME_OVER -> {
                renderer.render(gc, engine.getState(), dt, true, visibleError());
                gameOverView.render(gc, GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT, engine.getState().getScore());
                drawError(gc);
            }
            case VICTORY -> {
                renderer.render(gc, engine.getState(), dt, true, visibleError());
                victoryView.render(gc, GameConfig.FIELD_WIDTH, GameConfig.FIELD_HEIGHT, engine.getState().getScore());
                drawError(gc);
            }
        }
        if (screen == Screen.MENU) drawError(gc);
    }

    // updatePaddle: Advances the associated state using the elapsed time.
    // Inputs: dt.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void updatePaddle(double dt) {
        // Demo: update paddle velocity from actual movement, or explicitly clear it when idle.
        if (dt <= 0.0) {
            engine.getState().getPaddle().stop();
            return;
        }
        boolean left = heldKeys.contains(KeyCode.LEFT) || heldKeys.contains(KeyCode.A);
        boolean right = heldKeys.contains(KeyCode.RIGHT) || heldKeys.contains(KeyCode.D);
        if (left && !right) engine.getState().getPaddle().moveLeft(dt);
        else if (right && !left) engine.getState().getPaddle().moveRight(dt);
        else engine.getState().getPaddle().stop();
    }

    // onKeyPressed: Processes the supplied event or input and updates the related state.
    // Inputs: key.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void onKeyPressed(KeyCode key) {
        if (screen == Screen.UPGRADE_SELECTION) {
            upgradeView.handleKey(key);
            return;
        }
        if (key == KeyCode.ESCAPE) {
            screen = Screen.MENU;
            paused = false;
            heldKeys.clear();
            return;
        }
        if (key == KeyCode.P && screen == Screen.PLAYING) {
            paused = !paused;
            LOGGER.info("Player {}", paused ? "paused" : "resumed");
            return;
        }
        if (screen == Screen.PLAYING && !paused && heldKeys.add(key)) {
            if (key == KeyCode.SPACE) {
                // Demo: Space launches an attached ball first; later presses fire an active laser relic.
                if (!engine.getState().isBallLaunched()) engine.launchBall();
                else if (engine.getState().isLaserActive()) engine.fireLasers();
            }
        } else if (screen == Screen.MENU && key == KeyCode.ENTER) {
            startRun();
        } else if ((screen == Screen.GAME_OVER || screen == Screen.VICTORY) && key == KeyCode.ENTER) {
            startRun();
        }
    }

    // onKeyReleased: Processes the supplied event or input and updates the related state.
    // Inputs: key.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void onKeyReleased(KeyCode key) {
        heldKeys.remove(key);
        if (screen == Screen.PLAYING && !heldKeys.contains(KeyCode.LEFT) && !heldKeys.contains(KeyCode.RIGHT)
                && !heldKeys.contains(KeyCode.A) && !heldKeys.contains(KeyCode.D)) {
            engine.getState().getPaddle().stop();
        }
    }

    // onMouseClicked: Processes the supplied event or input and updates the related state.
    // Inputs: x, y.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void onMouseClicked(double x, double y) {
        switch (screen) {
            case MENU -> {
                if (menuView.isStartClick(x, y)) startRun();
                else if (menuView.isQuitClick(x, y)) Platform.exit();
            }
            case UPGRADE_SELECTION -> upgradeView.handleClick(x, y, GameConfig.FIELD_WIDTH);
            case GAME_OVER -> {
                if (gameOverView.isRestartClick(x, y)) startRun();
                else if (gameOverView.isMenuClick(x, y)) screen = Screen.MENU;
            }
            case VICTORY -> {
                if (victoryView.isRestartClick(x, y)) startRun();
                else if (victoryView.isMenuClick(x, y)) screen = Screen.MENU;
            }
            case PLAYING -> { }
        }
    }

    // startRun: Performs the start run operation.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void startRun() {
        try {
            engine.startRun();
            paused = false;
            heldKeys.clear();
            screen = Screen.PLAYING;
            LOGGER.info("New run started from the menu");
        } catch (GameException exception) {
            recover(exception);
        }
    }

    // openUpgradeSelection: Performs the related controller operation and updates the visible application state.
    // Inputs: none.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void openUpgradeSelection() {
        var choices = upgradePool.getChoices();
        upgradeView = new UpgradeSelectionView(choices, this::chooseUpgrade);
        screen = Screen.UPGRADE_SELECTION;
    }

    // chooseUpgrade: Performs the related controller operation and updates the visible application state.
    // Inputs: upgrade.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void chooseUpgrade(Upgrade upgrade) {
        try {
            upgrade.apply(engine.getState());
        } catch (GameException exception) {
            recover(exception);
        }
        try {
            engine.advanceStage();
            screen = engine.getState().isVictory() ? Screen.VICTORY : Screen.PLAYING;
            LOGGER.info("Advanced to stage {}", engine.getState().getStageNumber());
        } catch (GameException exception) {
            recover(exception);
        }
    }

    // recover: Performs the related controller operation and updates the visible application state.
    // Inputs: exception.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void recover(GameException exception) {
        GameExceptionHandler.RecoveryAction action = GameExceptionHandler.handle(exception);
        errorMessage = exception.getMessage();
        errorRemaining = 4.0;
        if (action == GameExceptionHandler.RecoveryAction.RETURN_TO_MENU) {
            screen = Screen.MENU;
            paused = false;
        }
        if (action == GameExceptionHandler.RecoveryAction.USE_PROCEDURAL_ASSETS) {
            errorMessage = "A visual asset could not load; procedural graphics remain active.";
        }
        LOGGER.warn("Recovery action selected: {}", action);
    }

    // visibleError: Performs the related controller operation and updates the visible application state.
    // Inputs: none.
    // Output/behavior: Returns the computed visible error value.
    private String visibleError() { return errorRemaining > 0.0 ? errorMessage : ""; }

    // drawError: Renders the associated visual element using the supplied graphics context.
    // Inputs: gc.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    private void drawError(GraphicsContext gc) {
        if (errorRemaining <= 0.0) return;
        gc.setFill(javafx.scene.paint.Color.rgb(130, 28, 38, 0.94));
        gc.fillRoundRect(175, 625, 450, 52, 8, 8);
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font("Monospaced", 12));
        gc.fillText(errorMessage, 192, 655);
    }
}
