package ru.vsu.сs.shemenev.swing;

import ru.vsu.сs.shemenev.Figure;
import ru.vsu.сs.shemenev.GameLogic;
import ru.vsu.сs.shemenev.KingFigure;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board extends JPanel implements MouseListener {
    private static final int cellSize = 80;
    private static final int panelSize = 640;
    private GameLogic gameLogic;
    private int clickedX = -1;
    private int clickedY = -1;
    private int countClick = 0;
    private String firstClick = null;
    private String secondClick = null;

    public Board(int size, GameLogic gameLogic) {
        setBounds(size / 10, size / 16, panelSize, panelSize);
        this.gameLogic = gameLogic;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public String getFirstClick() {
        return firstClick;
    }

    public String getSecondClick() {
        return secondClick;
    }

    public void resetClicks() {
        firstClick = null;
        secondClick = null;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        drawBoard((Graphics2D) g);
    }

    private void drawBoard(Graphics2D g2D) {
        Figure[][] figures = gameLogic.getFigures();

        int yCurr = 0;
        for (int i = 0; i < figures.length; i++) {
            int xCurr = 0;
            if (i % 2 == 0) {
                for (int j = 0; j < figures[0].length; j++) {
                    if (clickedX != -1 && clickedY != -1 && j == clickedX && i == clickedY) {
                        g2D.setColor(Colors.LIGHT_GREEN_SELECTED.getColor());
                    } else if (j % 2 == 0) {
                        g2D.setColor(Colors.WHITE_CELL.getColor());
                    } else {
                        g2D.setColor(Colors.BLACK_CELL.getColor());
                    }
                    g2D.fillRect(xCurr, yCurr, cellSize, cellSize);
                    drawCellBorders(g2D, xCurr, yCurr);
                    if (j % 2 != 0 && figures[i][j] != null) {
                        boolean isKing = figures[i][j] instanceof KingFigure;
                        Color color = figures[i][j].isBlack() ? Colors.DARK_FIGURE.getColor() : Colors.WHITE_FIGURE.getColor();
                        drawFigure(g2D, xCurr, yCurr, color, isKing);
                    }
                    xCurr += cellSize;
                }
            } else {
                for (int j = 0; j < figures[0].length; j++) {
                    if (clickedX != -1 && clickedY != -1 && j == clickedX && i == clickedY) {
                        g2D.setColor(Colors.LIGHT_GREEN_SELECTED.getColor());
                    } else if (j % 2 == 0) {
                        g2D.setColor(Colors.BLACK_CELL.getColor());
                    } else {
                        g2D.setColor(Colors.WHITE_CELL.getColor());
                    }
                    g2D.fillRect(xCurr, yCurr, cellSize, cellSize);
                    drawCellBorders(g2D, xCurr, yCurr);
                    if (j % 2 == 0 && figures[i][j] != null) {
                        boolean isKing = figures[i][j] instanceof KingFigure;
                        Color color = figures[i][j].isBlack() ? Colors.DARK_FIGURE.getColor() : Colors.WHITE_FIGURE.getColor();
                        drawFigure(g2D, xCurr, yCurr, color, isKing);
                    }
                    xCurr += cellSize;
                }
            }
            yCurr += cellSize;
        }
    }

    private void drawCellBorders(Graphics2D g2D, int xCurr, int yCurr) {
        g2D.setStroke(new BasicStroke(2));
        g2D.setColor(Color.black);
        g2D.drawRect(xCurr, yCurr, cellSize, cellSize);
    }

    private void drawFigure(Graphics2D g2D, int x, int y, Color color, boolean isKing) {
        g2D.setColor(color);
        g2D.fillOval(x + cellSize / 16, y + cellSize / 16, cellSize - cellSize / 8, cellSize - cellSize / 8);
        if (isKing) {
            g2D.setColor(Colors.GOLD_CROWN.getColor());
            g2D.fillOval(x + cellSize / 4, y + cellSize / 4, cellSize / 2, cellSize / 2);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        countClick++;
        if (e.getX() >= 85 && e.getX() <= 725 && e.getY() >= 80 && e.getY() <= 720) {
            int col = (e.getX()) / (panelSize / 8) - 1;
            int row = (e.getY()) / (panelSize / 8) - 1;
            //System.out.println(((char) ('A' + col)) + ((row + 1) + "")); //TODO Убрать
            String position = createPosition(row, col);
            if (countClick == 1) {
                if (gameLogic.checkSelectedFigure(position)) {
                    clickedX = col;
                    clickedY = row;
                    firstClick = position;
                } else {
                    countClick = 0;
                    JOptionPane.showMessageDialog(null, "Ошибка при выборе фигуры");
                }
            } else {
                String positionFrom = createPosition(clickedY, clickedX); //TODO Заменить на start
                StatusMove statusMove = gameLogic.createMove(positionFrom, position);
                if (statusMove.equals(StatusMove.KILL_YET)) {
                    clickedX = col;
                    clickedY = row;
                    countClick = 1;
                    firstClick = positionFrom;
                    secondClick = position;
                } else if (statusMove.equals(StatusMove.MOVE_IMPOSSIBLE)) {
                    countClick = 0;
                    clickedX = -1;
                    clickedY = -1;
                    JOptionPane.showMessageDialog(null, "Неверный ход");
                } else if (statusMove.equals(StatusMove.WINNER_PLAYER1)) {
                    countClick = 0;
                    clickedX = -1;
                    clickedY = -1;
                    repaint();
                    JOptionPane.showMessageDialog(null, "Победил Player1");
                } else if (statusMove.equals(StatusMove.WINNER_PLAYER2)) {
                    countClick = 0;
                    clickedX = -1;
                    clickedY = -1;
                    repaint();
                    JOptionPane.showMessageDialog(null, "Победил Player2");
                } else {
                    clickedX = -1;
                    clickedY = -1;
                    countClick = 0;
                    secondClick = position;
                }
            }
        } else {
            clickedX = -1;
            clickedY = -1;
            countClick = 0;
        }
        if (gameLogic.isGameEnd()) {
            gameLogic.setDefaultGame();
        }
        repaint();

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private String createPosition(int row, int col) {
        return ((char) ('A' + col)) + ((row + 1) + "");
    }
}
