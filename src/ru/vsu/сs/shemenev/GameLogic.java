package ru.vsu.сs.shemenev;

import ru.vsu.сs.shemenev.swing.Bot;
import ru.vsu.сs.shemenev.swing.Move;
import ru.vsu.сs.shemenev.swing.StatusMove;

import java.util.List;

public class GameLogic {
    private Figure[][] figures;
    private boolean gameEnd = false;
    private int currMove = 0;
    private final Player player1;
    private final Player player2;
    private boolean killingYet = false;


    public GameLogic(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }


    public boolean isGameEnd() {
        return gameEnd;
    }

    public int getCurrMove() {
        return currMove;
    }

    public int getCountFiguresPlayer1() {
        return player1.getCountFigures();
    }

    public int getCountFiguresPlayer2() {
        return player2.getCountFigures();
    }

    public void setCountFiguresForPlayers() {
        player1.setCountFigures(12);
        player2.setCountFigures(12);
    }


    public Figure[][] getFigures() {
        return figures;
    }

    public void createBoardWithFigures() {
        figures = new Figure[8][8];
        boolean color = true;
        for (int i = 0; i < 8; i++) {
            if (i > 4) {
                color = false;
            } else if (i == 3 || i == 4) {
                continue;
            }
            if (i % 2 == 0) {
                for (int j = 0; j < 8; j++) {
                    if (j % 2 == 0) {
                        figures[i][j] = null;
                    } else {
                        figures[i][j] = new SimpleFigure(color, i, j);
                    }
                }
            } else {
                for (int j = 0; j < 8; j++) {
                    if (j % 2 == 0) {
                        figures[i][j] = new SimpleFigure(color, i, j);
                    } else {
                        figures[i][j] = null;
                    }
                }
            }
        }
    }


    public boolean checkSelectedFigure(String position) {
        int indexCol = position.charAt(0) - 'A';
        int indexRow = Integer.parseInt(position.substring(1, 2)) - 1;
        if (indexCol < 0 || indexCol > 7 || indexRow < 0 || indexRow > 7 || figures[indexRow][indexCol] == null
                || !((currMove % 2 == 0 && !figures[indexRow][indexCol].isBlack()) || (currMove % 2 != 0 &&
                figures[indexRow][indexCol].isBlack()))) {
            return false;
        }
        return true;
    }

    public boolean checkMove(String positionFrom, String positionTo) {
        int indexRow = Integer.parseInt(positionFrom.substring(1, 2)) - 1;
        int indexCol = positionFrom.toUpperCase().charAt(0) - 'A';
        return figures[indexRow][indexCol].checkMove(figures, figures[indexRow][indexCol], positionTo);
    }

    public void makeMove(String positionFrom, String positionTo) {
        int indexHor1 = Integer.parseInt(positionFrom.substring(1, 2)) - 1;
        int indexVer1 = positionFrom.toUpperCase().charAt(0) - 'A';
        int indexHor2 = Integer.parseInt(positionTo.substring(1, 2)) - 1;
        int indexVer2 = positionTo.toUpperCase().charAt(0) - 'A';
        Figure temp = figures[indexHor1][indexVer1];
        figures[indexHor1][indexVer1] = figures[indexHor2][indexVer2];
        figures[indexHor2][indexVer2] = temp;
        temp.setColIndex(indexVer2);
        temp.setRowIndex(indexHor2);
        if (temp instanceof SimpleFigure && (indexHor2 == 0 || indexHor2 == 7)) {
            figures[indexHor2][indexVer2] = new KingFigure(temp.isBlack(), temp.getRowIndex(), temp.getColIndex());
        }
    }

    public boolean checkKillNow(String positionFrom, String positionTo) {
        int indexRow = Integer.parseInt(positionFrom.substring(1, 2)) - 1;
        int indexCol = positionFrom.toUpperCase().charAt(0) - 'A';
        if (positionTo != null) {
            int indexColEnd = positionTo.charAt(0) - 'A';
            int indexRowEnd = Integer.parseInt(positionTo.substring(1, 2)) - 1;
            if (indexColEnd < 0 || indexColEnd > 7 || indexRowEnd < 0 || indexRowEnd > 7 || figures[indexRowEnd][indexColEnd] != null) {
                return false;
            }
        }
        return figures[indexRow][indexCol].checkKill(figures, figures[indexRow][indexCol], positionTo);
    }

    public void makeKill(String positionFrom, String positionTo) {
        Player player = currMove % 2 == 0 ? player2 : player1;
        int indexHor1 = Integer.parseInt(positionFrom.substring(1, 2)) - 1;
        int indexVer1 = positionFrom.toUpperCase().charAt(0) - 'A';
        int indexHor2 = Integer.parseInt(positionTo.substring(1, 2)) - 1;
        int indexVer2 = positionTo.toUpperCase().charAt(0) - 'A';
        figures[indexHor1][indexVer1].makeKill(figures, indexHor1, indexVer1, indexHor2, indexVer2, player);
        if (player1.getCountFigures() == 0 || player2.getCountFigures() == 0) {
            gameEnd = true;
        }
    }

    public StatusMove createMove(String positionStart, String positionTarget) {
        if (checkKillNow(positionStart, positionTarget)) {
            makeKill(positionStart, positionTarget);
            if (checkKillNow(positionTarget, null)) {
                //currMove--;
                killingYet = true;
                return StatusMove.KILL_YET;
            }
            killingYet = false;
            currMove++;
        } else if (checkMove(positionStart, positionTarget) && !killingYet) {
            makeMove(positionStart, positionTarget);
            currMove++;
        } else if (killingYet) {
            return StatusMove.KILL_YET;
        } else {
            return StatusMove.MOVE_IMPOSSIBLE;
        }
        if (player1.getCountFigures() == 0) {
            gameEnd = true;
            return StatusMove.WINNER_PLAYER2;
        } else if (player2.getCountFigures() == 0) {
            gameEnd = true;
            return StatusMove.WINNER_PLAYER1;
        } else {
            return StatusMove.ALL_GOOD;
        }
    }

    public void setDefaultGame() {
        createBoardWithFigures();
        currMove = 0;
        setCountFiguresForPlayers();
        if (gameEnd) {
            gameEnd = false;
        }
    }

    //Для ботов

    public StatusMove nextMoveForBots() {
        if (player1 instanceof Bot && player2 instanceof Bot) {
            String positionStart = currMove % 2 == 0 ? ((Bot) player1).getStartPosition() : ((Bot) player2).getStartPosition();
            if (checkSelectedFigure(positionStart)) {
                String positionTarget = currMove % 2 == 0 ? ((Bot) player1).getTargetPosition() : ((Bot) player2).getTargetPosition();
                if (checkKillNow(positionStart, positionTarget)) {
                    makeKill(positionStart, positionTarget);
                    if (checkKillNow(positionTarget, null)) {
                        currMove--;
                    }
                    currMove++;
                } else if (checkMove(positionStart, positionTarget)) {
                    makeMove(positionStart, positionTarget);
                    currMove++;
                } else {
                    return StatusMove.MOVE_IMPOSSIBLE;
                }
                if (player1.getCountFigures() == 0) {
                    gameEnd = true;
                    return StatusMove.WINNER_PLAYER2;
                } else if (player2.getCountFigures() == 0) {
                    gameEnd = true;
                    return StatusMove.WINNER_PLAYER1;
                }
            } else {
                return StatusMove.SELECT_IMPOSSIBLE;
            }
        }
        return StatusMove.ALL_GOOD;
    }

    public void setDefaultGameForBots(List<Move> listForBot1, List<Move> listForBot2) {
        if (player1 instanceof Bot && player2 instanceof Bot) {
            ((Bot) player1).setStrategies(listForBot1);
            ((Bot) player2).setStrategies(listForBot2);
            ((Bot) player1).setCurrMove(0);
            ((Bot) player2).setCurrMove(0);
        }
        setDefaultGame();
    }

    public boolean isNotNativeBot() {
        if (player1 instanceof Bot && player2 instanceof Bot) {
            return (currMove % 2 == 0 && ((Bot) player1).isNotMove()) ||
                    (currMove % 2 != 0 && ((Bot) player2).isNotMove());
        }
        return true;
    }

    public boolean isNotStrategyBot() {
        if (player1 instanceof Bot && player2 instanceof Bot) {
            return ((Bot) player1).isNotStrategy() || ((Bot) player2).isNotStrategy();
        }
        return true;
    }

    /*public String getServerMove(String a, String b) {
//        try {
        String submitCommand = Command.MOVE.getCommandString() + Command.SEPARATOR;
        if (currMove / 2 == botMoves.size()) {
            submitCommand = Command.WIN.getCommandString() + Command.SEPARATOR;
            gameEnd = true;
            return submitCommand;
        }
        Move currMoveBot = botMoves.get(currMove / 2);
        //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        //ObjectOutputStream writer = new ObjectOutputStream(socket.getOutputStream());
        //Phone phone = new Phone(socket);
        //String input = phone.readLine();
        //System.out.println(input);
//            String input = reader.readLine();
//            String[] parsed = input.split(" ");
        createMove(a, b);
        createMove(currMoveBot.getStartPosition(), currMoveBot.getTargetPosition());
        //System.out.println(currMoveBot);
        //System.out.println(submitStr);
        return submitCommand + currMoveBot.getStartPosition() + "->" + currMoveBot.getTargetPosition();
        //writer.println(submitStr);
        //phone.writeObj(new Packet("D3", "C4"));
        //phone.writeLine(submitStr);
//            //phone.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

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
        return "[Player: countFigures = " + player1.getCountFigures() + ";" +
                "\nBot, countFigures = " + player2.getCountFigures() + "]";
    }
     */
}
