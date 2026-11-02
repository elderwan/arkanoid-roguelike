// @author Wang
package se233.arkanoid.exception;

public class GameException extends RuntimeException {
    // Demo: the shared exception type carries an ErrorCode so one handler can choose recovery behavior.
    private final ErrorCode errorCode;

    // GameException: Initializes the object for use by the application.
    // Inputs: errorCode, message.
    // Output/behavior: Initializes the object; no value is returned.
    public GameException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    // GameException: Initializes the object for use by the application.
    // Inputs: errorCode, message, cause.
    // Output/behavior: Initializes the object; no value is returned.
    public GameException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    // getErrorCode: Returns the current error code.
    // Inputs: none.
    // Output/behavior: Returns the computed get error code value.
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
