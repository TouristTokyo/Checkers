package ru.vsu.сs.shemenev;

import ru.vsu.сs.shemenev.swing.Bot;

public class Game {

    public static GameLogic create(String playerName1, String playerName2, boolean botGame) {
        GameLogic gameLogic;
        if (botGame) {
            gameLogic = new GameLogic(new Player(playerName1), new Bot(playerName2));
        } else {
            gameLogic = new GameLogic(new Player(playerName1), new Player(playerName2));
        }
        gameLogic.setDefaultGame();
        return gameLogic;
    }
}
