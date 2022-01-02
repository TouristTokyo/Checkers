package ru.vsu.сs.shemenev.server;

import ru.vsu.сs.shemenev.GameLogic;
import ru.vsu.сs.shemenev.Player;
import ru.vsu.сs.shemenev.swing.Move;
import ru.vsu.сs.shemenev.swing.Reader;
import ru.vsu.сs.shemenev.swing.StatusMove;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameServerLogic extends GameLogic {
    private boolean gameEnd = false;

    @Override
    public boolean isGameEnd() {
        return gameEnd;
    }

    public GameServerLogic(Player player1, Player player2) {
        super(player1, player2);
        setDefaultGame();
    }

    public String getServerMove(String positionStart, String positionEnd) {
        String submitCommand = Command.MOVE.getCommandString() + Command.SEPARATOR;
        if (getCurrMove() / 2 == botMoves.size()) {
            submitCommand = Command.WIN.getCommandString() + Command.SEPARATOR;
            gameEnd = true;
            return submitCommand;
        }
        Move currMoveBot = botMoves.get(getCurrMove() / 2);
        StatusMove status = createMove(positionStart, positionEnd);
        if (status == StatusMove.WINNER_PLAYER1) {
            submitCommand = Command.WIN.getCommandString() + Command.SEPARATOR;
            gameEnd = true;
            return submitCommand;
        }
        status = createMove(currMoveBot.getStartPosition(), currMoveBot.getTargetPosition());
        if (status == StatusMove.MOVE_IMPOSSIBLE) {
            submitCommand = Command.WIN.getCommandString() + Command.SEPARATOR;
            gameEnd = true;
            return submitCommand;
        }
        if (status == StatusMove.WINNER_PLAYER2) {
            submitCommand = Command.LOSE.getCommandString() + Command.SEPARATOR;
            gameEnd = true;
        }
        return submitCommand + currMoveBot.getStartPosition() + "->" + currMoveBot.getTargetPosition();

    }

    List<Move> botMoves;

    {
        try {
            botMoves = Reader.getStrategy(new File("resources\\bot.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Статистика игры
    public String getStatisticsForServer() {
        return "[Player: countFigures = " + getCountFiguresPlayer1() + ";" +
                "\nBot, countFigures = " + getCountFiguresPlayer2() + "]";
    }
}
