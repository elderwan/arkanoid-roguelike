// @author Wang
package se233.arkanoid;

import javafx.application.Application;

public final class Launcher {
    // Launcher: Initializes the object for use by the application.
    // Inputs: none.
    // Output/behavior: Initializes the object; no value is returned.
    private Launcher() { }
    // main: Executes the main operation.
    // Inputs: args.
    // Output/behavior: Updates state or performs side effects; no value is returned.
    public static void main(String[] args) {
        Application.launch(ArkanoidApp.class, args);
    }
}
