// @author Wang
package se233.arkanoid.exception;

public class GameException extends RuntimeException {
    // Demo: the shared exception type carries an ErrorCode so one handler can choose recovery behavior.
    private final ErrorCode errorCode;

    public GameException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GameException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
