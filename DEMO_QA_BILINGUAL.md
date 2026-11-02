# SE233 Arkanoid Roguelike — Simple Demo Q&A
# SE233 Arkanoid Roguelike — 演示简单问答

## 1. What is your project?
## 1. 你们的项目是什么？

**Answer (English):**
It is a single-player Arkanoid game with five stages and upgrade choices.

**回答（中文）：**
这是一个单人打砖块游戏，有五个关卡和升级选择。

**Code to search / 代码搜索：** `GameConfig.TOTAL_STAGES`, `StageManager`, `UpgradePool`

## 2. How do I start the game?
## 2. 怎么开始游戏？

**Answer (English):**  
Click **Start Run**, or press **Enter** on the menu.

**回答（中文）：**  
点击 **Start Run**，或者在菜单中按 **Enter**。

**Code to search / 代码搜索：** `isStartClick`, `startRun()`, `KeyCode.ENTER` in `GameController.java`

## 3. How do I control the paddle?
## 3. 怎么控制挡板？

**Answer (English):**  
Use the **Left** and **Right** arrow keys, or **A** and **D**.

**回答（中文）：**  
使用键盘的**左、右方向键**，或者 **A、D** 键。

**Code to search / 代码搜索：** `updatePaddle`, `KeyCode.LEFT`, `KeyCode.A`, `moveLeft`, `moveRight`

## 4. How does the ball bounce off the paddle?
## 4. 球碰到挡板时是怎么反弹的？

**Answer (English):**
The bounce is deterministic. If the paddle is still, the ball keeps its horizontal speed and reverses its vertical direction. If the paddle is moving, its motion adds a limited sideways bias. The game caps the bounce angle and preserves the ball's speed.

**回答（中文）：**
反弹方向是确定的。挡板静止时，球的水平速度保持不变，垂直方向反转；挡板移动时，会给球增加有限的横向速度偏移。反弹角度有上限，并且会保持球的速度大小。

**Code to search / 代码搜索：** `bounceOffPaddle`, `paddleBias`, `clampBounceAngle`, `resolvePaddleCollision` in `engine/CollisionResolver.java`; `getVelocityX` in `model/Paddle.java`; `stationaryPaddleReflectsIncomingVelocityExactly` in `CollisionResolverTest.java`

## 4. How do I launch the ball?
## 4. 怎么发射球？

**Answer (English):**  
Press **Space**. If the laser upgrade is active, Space fires the lasers instead.

**回答（中文）：**  
按 **Space** 发射球。如果激光升级还有效，按 Space 会发射激光。

**Code to search / 代码搜索：** `KeyCode.SPACE`, `launchBall()`, `fireLasers()`, `launchFromPaddle` in `GameController.java`, `GameEngine.java`, and `CollisionResolver.java`

## 5. How many lives does the player start with?
## 5. 玩家开始时有几条生命？

**Answer (English):**  
The player starts with three lives. A life is lost when all balls are lost.

**回答（中文）：**  
玩家开始时有三条生命。所有球都丢失后会失去一条生命。

**Code to search / 代码搜索：** `STARTING_LIVES`, `state.getBalls().isEmpty()`, `loseLife()`

## 6. What happens when I clear a stage?
## 6. 清空一个关卡后会发生什么？

**Answer (English):**  
The game pauses and offers three upgrades. Choose one to continue to the next stage.

**回答（中文）：**  
游戏会暂停并提供三个升级选项。选择一个后继续下一关。

**Code to search / 代码搜索：** `isStageComplete()`, `openUpgradeSelection()`, `chooseUpgrade()`, `advanceStage()`

## 7. How do I choose an upgrade?
## 7. 怎么选择升级？

**Answer (English):**  
Click an upgrade card, or press **1**, **2**, or **3**.

**回答（中文）：**  
点击升级卡片，或者按 **1、2、3** 选择。

**Code to search / 代码搜索：** `handleKey`, `KeyCode.DIGIT1`, `handleClick`, `onChoose.accept`

## 8. What kinds of bricks are in the game?
## 8. 游戏里有哪些砖块？

**Answer (English):**  
There are normal, strong, and bonus bricks. They have different durability and score values.

**回答（中文）：**  
游戏里有普通砖、坚固砖和奖励砖。它们的耐久度和分数不同。

**Code to search / 代码搜索：** `NormalBrick`, `StrongBrick`, `BonusBrick`, `enum BrickType`

## 9. How do you get points?
## 9. 怎么获得分数？

**Answer (English):**  
Destroying a brick gives points. The score is shown at the top of the screen.

**回答（中文）：**  
摧毁砖块可以获得分数。分数显示在屏幕上方。

**Code to search / 代码搜索：** `ScoreCalculator.calculate`, `state.addScore(points)`, `SCORE`, `ScoreEvent`

## 10. What is a cursed upgrade?
## 10. 什么是诅咒升级？

**Answer (English):**  
It gives a strong benefit but also has a drawback. For example, Glass Cannon increases score but makes the paddle smaller.

**回答（中文）：**  
它会带来较强的好处，但同时有副作用。例如，Glass Cannon 提高分数倍率，但会缩小挡板。

**Code to search / 代码搜索：** `isCursed()`, `GlassCannonUpgrade.apply`, `FrenzyUpgrade.apply`

## 11. What is one extra feature in your game?
## 11. 你们的游戏有什么额外特色？

**Answer (English):**  
The combo system increases the score multiplier when bricks are destroyed in a row. Hitting the paddle resets the combo.

**回答（中文）：**  
连击系统会在连续摧毁砖块时提高分数倍率。球碰到挡板后连击会重置。

**Code to search / 代码搜索：** `incrementCombo`, `resetCombo`, `getComboMultiplier`

## 12. How do you test the game?
## 12. 你们怎么测试游戏？

**Answer (English):**  
We use JUnit tests for bricks, paddle movement, collisions, scoring, lives, stages, and upgrades.

**回答（中文）：**  
我们使用 JUnit 测试砖块、挡板移动、碰撞、计分、生命、关卡和升级。

**Code to search / 代码搜索：** `src/test/java/se233/arkanoid`, `@Test`, `CollisionResolverTest`, `GameEngineTest`

## 13. How do you run the project?
## 13. 怎么运行项目？

**Answer (English):**  
Run `mvn javafx:run` to start the game. Run `mvn test` to run the tests.

**回答（中文）：**  
运行 `mvn javafx:run` 启动游戏，运行 `mvn test` 执行测试。

**Code to search / 代码搜索：** `javafx-maven-plugin`, `maven-surefire-plugin`, `Launcher`

## 14. What happens if a game error occurs?
## 14. 游戏发生错误时会怎样？

**Answer (English):**  
The game uses a custom `GameException` and a central handler to log the error and choose a recovery action.

**回答（中文）：**  
游戏使用自定义的 `GameException` 和统一处理器记录错误，并选择相应的恢复方式。

**Code to search / 代码搜索：** `class GameException`, `GameExceptionHandler.handle`, `catch (GameException`
