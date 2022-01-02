package ru.vsu.сs.shemenev.swing;

import ru.vsu.сs.shemenev.Player;

import java.util.List;

public class Bot extends Player {
    private List<Move> strategies;
    private int currMove = 0;

    public Bot(String name) {
        super(name);
    }

    public String getStartPosition() {
        return strategies.get(currMove).getStartPosition();
    }

    public String getTargetPosition() {
        return strategies.get(currMove++).getTargetPosition();
    }

    public void setStrategies(List<Move> strategies) {
        this.strategies = strategies;
    }

    public void setCurrMove(int currMove) {
        this.currMove = currMove;
    }

    public boolean isNotStrategy() {
        return strategies == null;
    }

    public boolean isNotMove() {
        return currMove >= strategies.size();
    }
}
