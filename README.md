<!-- @author Oly -->
# Arkanoid Roguelike

A Java 17 / JavaFX 21 single-module project for SE233. Wang and Oly built a
Canvas-rendered brick-breaker with stage-to-stage relic choices, combo scoring,
laser volleys, and cursed relics. The model and engine are plain Java and can
be tested without starting the JavaFX toolkit.

## Build, test, and launch

```shell
mvn clean compile
mvn test
mvn javafx:run
mvn clean package
java -jar target/arkanoid-roguelike-1.0.0-shaded.jar
```

The Maven OS profiles select JavaFX native artifacts for Windows, Linux,
Intel macOS, or Apple Silicon. The shaded jar includes its dependencies and
uses `se233.arkanoid.Launcher` as its entry point.

## Controls

- **Left / Right** or **A / D**: move the paddle.
- **Space**: launch the ball; while the twin-laser relic is active, fire the
  two laser bolts instead.
- **P**: pause or resume.
- **Esc**: return to the menu.
- **1 / 2 / 3** or click a card: choose a relic after a stage.
- **Enter**: start or restart from a result screen.

Every run starts with three lives. Clear all bricks to choose one of three
distinct relics. Normal, strong, and bonus bricks award 10, 30, and 50 base
points. Consecutive destroyed bricks raise the combo from x1 to x5; a paddle
touch resets it. The five stages introduce tougher patterns and increase ball
speed by six percent per stage. Cursed cards are red and pair a substantial
bonus with a cost. Brick flashes, hit sparks, destruction bursts, floating
score popups, a moving paddle animation, and a distinct bounce animation make
collisions visible.

## Demo script for the lecturer

1. Run `mvn javafx:run`; start a run from the title screen and launch with
   **Space**. Paddle motion and ball-bounce visuals are separate states.
2. Break a brick. Point totals update immediately and a floating score popup
   appears; hit and destruction particles differ. Continue without touching
   the paddle to demonstrate combo x1 through x5, then touch it to reset.
3. Clear stage 1 and select a relic with a click or number key. Demonstrate
   ordinary upgrades, or select a red cursed card to show its paired effect.
4. Choose Twin lasers, fire with **Space**, and note the dual projectiles and
   remaining active time. The later stages demonstrate stage progression,
   changing brick layouts, and rising speed.
5. Lose all three lives to show the game-over and restart flow, or clear stage
   5 to show victory and the final score. Pause with **P** and return to menu
   with **Esc**.
6. Run `mvn test` to demonstrate model, collision, stage, scoring, life,
   upgrade, and exception-handler coverage. Logs show player actions, score
   totals, stage events, and handled game errors in the console and
   `logs/arkanoid.log`.

## Rubric map

| Rubric area | Where to demonstrate / inspect | Owner |
|---|---|---|
| Playable game and five stages | `GameController`, `GameEngine`, `StageManager`, `StageLayoutFactory` | Wang / Oly |
| Roguelike choices and cursed relics | `UpgradeSelectionView`, `UpgradePool`, `*Upgrade` classes | Oly / Wang |
| Separate movement and bounce animation states | `PaddleState`, `BallState`, `SpriteSheet`, `GameRenderer` | Oly / Wang |
| Score display and score-change feedback | `GameState`, `ScoreCalculator`, `HudView` | Wang / Oly |
| Brick hit/destruction animation | `Brick`, `ParticleEffects`, `GameRenderer` | Oly / Wang |
| Custom exception and centralized handling | `GameException`, `GameExceptionHandler`, `GameController`, `ArkanoidApp` | Wang |
| Logging | `src/main/resources/logback.xml` and application/model/upgrade classes | Wang / Oly |
| Unit tests | `src/test/java/se233/arkanoid` | Wang / Oly |
| Maven run/test/compile/executable jar | `pom.xml`, `Launcher.java` | Wang |
| Additional feature beyond classic Arkanoid | Combo multiplier and paired cursed relics | Wang |
