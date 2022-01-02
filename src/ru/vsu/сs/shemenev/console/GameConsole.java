package ru.vsu.сs.shemenev.console;

import ru.vsu.сs.shemenev.Game;
import ru.vsu.сs.shemenev.GameLogic;
import ru.vsu.сs.shemenev.Player;

import java.util.Scanner;

public class GameConsole {
    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.print("Введите имя 1-го игрока: ");
        String namePlayer1 = scanner.nextLine();
        System.out.print("Введите имя 2-го игрока: ");
        String namePlayer2 = scanner.nextLine();

        GameLogic gameLogic = new GameLogic(new Player(namePlayer1), new Player(namePlayer2));
        gameLogic.setDefaultGame();

        String positionTo, currPlayer, positionFrom;
        boolean killing = false;

        System.out.println("----Игра началась----");
        while (!gameLogic.isGameEnd()) {
            System.out.println("Убито белых: " + (12 - gameLogic.getCountFiguresPlayer1()) + "   Убито черных: " +
                    (12 - gameLogic.getCountFiguresPlayer2()));
            ConsolePainter.drawBoardWithFigures(gameLogic.getFigures());
            if (gameLogic.getCurrMove()%2==0) {
                currPlayer = namePlayer1;
            } else {
                currPlayer = namePlayer2;
            }

            System.out.print(currPlayer + ", выберите шашку: ");
            positionFrom = scanner.nextLine();

            if (gameLogic.checkSelectedFigure(positionFrom.toUpperCase())) {

                while (gameLogic.checkKillNow(positionFrom.toUpperCase(), null)) {
                    killing = true;
                    System.out.print(currPlayer + ", вам надо бить! Выберите куда хотите сходить: ");
                    positionTo = scanner.nextLine();
                    if (gameLogic.checkKillNow(positionFrom.toUpperCase(), positionTo.toUpperCase())) {
                        gameLogic.makeKill(positionFrom.toUpperCase(), positionTo.toUpperCase());
                        positionFrom = positionTo;
                        System.out.println("!Вы съели шашку противника!");
                    } else {
                        System.out.println("ход неверный");
                    }
                }

                if (killing) {
                    killing = false;
                    continue;
                }

                System.out.print(currPlayer + ", выберите куда хотите сходить: ");
                positionTo = scanner.nextLine();

                if (gameLogic.checkMove(positionFrom.toUpperCase(), positionTo.toUpperCase())) {
                    gameLogic.makeMove(positionFrom.toUpperCase(), positionTo.toUpperCase());
                } else {
                    System.out.println("Данный ход невозможен");
                }
            } else {
                System.out.println("Ошибка при выборе фигуры");
            }
        }

        System.out.println("----Игра завершилась----");
        System.out.println("Победил " + (gameLogic.getCountFiguresPlayer1() > gameLogic.getCountFiguresPlayer2() ?
                namePlayer1 : namePlayer2));
    }
}