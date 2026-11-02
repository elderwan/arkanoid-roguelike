// @author Wang
package se233.arkanoid.engine;

// ScoreEvent: Initializes the object for use by the application.
// Inputs: points, totalScore, x, y.
// Output/behavior: Initializes the object; no value is returned.
public record ScoreEvent(int points, int totalScore, double x, double y) { }
