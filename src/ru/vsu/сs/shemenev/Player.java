package ru.vsu.сs.shemenev;

public class Player {
    private final String name;
    private int countFigures = 12;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCountFigures() {
        return countFigures;
    }

    public void setCountFigures(int countFigures) {
        this.countFigures = countFigures;
    }
}
