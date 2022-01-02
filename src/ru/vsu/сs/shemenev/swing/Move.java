package ru.vsu.сs.shemenev.swing;

public class Move {
    private final String startPosition;
    private final String targetPosition;
    private final boolean isFirstPlayer;

    public Move(String startPosition, String targetPosition, boolean isFirstPlayer) {
        this.startPosition = startPosition;
        this.targetPosition = targetPosition;
        this.isFirstPlayer = isFirstPlayer;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public String getTargetPosition() {
        return targetPosition;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }
}
