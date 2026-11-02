// @author Wang
package se233.arkanoid.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameExceptionHandlerTest {
    @Test
    void handlesEveryErrorCodeAndReturnsRecoveryAction() {
        for (ErrorCode code : ErrorCode.values()) {
            var action = assertDoesNotThrow(() ->
                    GameExceptionHandler.handle(new GameException(code, "test " + code)));
            assertNotNull(action);
        }
    }
}
