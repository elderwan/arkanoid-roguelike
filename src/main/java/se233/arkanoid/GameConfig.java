// @author Wang
package se233.arkanoid;

public final class GameConfig {
    // Demo: centralize the playfield and gameplay tuning values so engine rules stay consistent.
    public static final double FIELD_WIDTH = 800.0;
    public static final double FIELD_HEIGHT = 720.0;
    public static final double PADDLE_WIDTH = 120.0;
    public static final double PADDLE_HEIGHT = 16.0;
    public static final double PADDLE_Y = 660.0;
    public static final double PADDLE_SPEED = 520.0;
    public static final double PADDLE_STATIONARY_EPSILON = 1.0;
    public static final double PADDLE_INFLUENCE = 0.35;
    public static final double MAX_PADDLE_BIAS_SPEED = 220.0;
    public static final double MAX_BOUNCE_ANGLE_DEG = 60.0;
    public static final double BALL_RADIUS = 8.0;
    public static final double BALL_BASE_SPEED = 380.0;
    public static final double BRICK_WIDTH = 70.0;
    public static final double BRICK_HEIGHT = 28.0;
    public static final int BRICK_COLUMNS = 10;
    public static final double BRICK_LEFT_MARGIN = 50.0;
    public static final double BRICK_TOP_MARGIN = 80.0;
    public static final int STARTING_LIVES = 3;
    public static final int TOTAL_STAGES = 5;
    public static final int UPGRADE_CHOICES = 3;
    public static final double LASER_DURATION_SECONDS = 15.0;
    public static final double LASER_COOLDOWN_SECONDS = 0.35;
    public static final double LASER_SPEED = 700.0;

    // GameConfig: Initializes the object for use by the application.
    // Inputs: none.
    // Output/behavior: Initializes the object; no value is returned.
    private GameConfig() { }
}
