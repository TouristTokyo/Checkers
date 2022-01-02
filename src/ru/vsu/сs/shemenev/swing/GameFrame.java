package ru.vsu.сs.shemenev.swing;


import ru.vsu.сs.shemenev.GameLogic;
import ru.vsu.сs.shemenev.Player;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class GameFrame extends JFrame {
    private static final int size = 800;
    private GameLogic gameLogic;
    private final Board board;

    public GameFrame() {
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //???
        setTitle("Checkers");
        pack();
        setSize(size, size);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        getContentPane().setBackground(Colors.ORANGE_LIGHT_BACKGROUND.getColor());

        gameLogic = new GameLogic(new Player("Player1"),new Player( "Player2"));
        gameLogic.setDefaultGame();
        board = new Board(size, gameLogic);
        add(board);
        addMouseListener(board);


        JButton nextMoveButton = new JButton("--Next-->");
        customizeJButton(nextMoveButton);
        nextMoveButton.setBounds(size - 200, size - 90, size / 8, size / 25);
        add(nextMoveButton);

        JButton uploadButton = new JButton("Load");
        customizeJButton(uploadButton);
        uploadButton.setBounds(size / 8, size - 90, size / 8, size / 25);
        add(uploadButton);

        JButton createGameForPlayers = new JButton("2 Players");
        customizeJButton(createGameForPlayers);
        createGameForPlayers.setBounds(size - 440, size - 80, size / 7, size / 25);
        add(createGameForPlayers);

        JLabel labelNameForPlayer1 = new JLabel("Player1");
        labelNameForPlayer1.setBounds(384, size - 110, size / 10, size / 32);
        labelNameForPlayer1.setFont(new Font("TimesRoman", Font.BOLD, 15));
        add(labelNameForPlayer1);

        JLabel labelNameForPlayer2 = new JLabel("Player2");
        labelNameForPlayer2.setBounds(384, 25, size / 10, size / 32);
        labelNameForPlayer2.setFont(new Font("TimesRoman", Font.BOLD, 15));
        add(labelNameForPlayer2);

        JLabel labelMode = new JLabel("Mode: Players");
        labelMode.setBounds(10, 15, size / 5, size / 32);
        labelMode.setFont(new Font("TimesRoman", Font.BOLD, 15));
        add(labelMode);

        nextMoveButton.addActionListener(event -> {
            if (gameLogic.isGameEnd()) {
                JOptionPane.showMessageDialog(null, "Партия уже сыграна." +
                        "Загрузите новую стратегию", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (gameLogic.isNotStrategyBot()) {
                JOptionPane.showMessageDialog(null, "Стратегия не найдена (или найдена не для всех ботов).\n" +
                        "Загрузите стратегию (или загрузите новую стратегию)", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (gameLogic.isNotNativeBot() && !gameLogic.isGameEnd()) {
                JOptionPane.showMessageDialog(null, "Стратегия игры не полная.\n" +
                        "Загрузите полную стратегию игры", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (gameLogic.isGameEnd()) {
                JOptionPane.showMessageDialog(null, "Партия сыграна!\n" +
                        "Загрузите новую стратегию", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StatusMove status = gameLogic.nextMoveForBots();
            board.repaint();
            if (status == StatusMove.MOVE_IMPOSSIBLE) {
                JOptionPane.showMessageDialog(null, "Данный ход не возможен.\n" +
                        "Загрузите другую стратегию", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (status == StatusMove.SELECT_IMPOSSIBLE) {
                JOptionPane.showMessageDialog(null, "Ошибка при выборе фигуры.\n" +
                        "Загрузите другую стратегию", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (status == StatusMove.WINNER_PLAYER1) {
                JOptionPane.showMessageDialog(null, "Победил Bot@001", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else if (status == StatusMove.WINNER_PLAYER2) {
                JOptionPane.showMessageDialog(null, "Победил Bot@002", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        uploadButton.addActionListener(event -> {
            File file;
            JFileChooser fileChooser = new JFileChooser();
            int ret = fileChooser.showDialog(null, "Select");
            if (ret == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
            } else {
                return;
            }
            try {
                List<Move> listStrategy = Reader.getStrategy(file);
                List<Move> listForBot1 = new ArrayList<>();
                List<Move> listForBot2 = new ArrayList<>();
                for (Move move : listStrategy) {
                    if (move.isFirstPlayer()) {
                        listForBot1.add(move);
                        continue;
                    }
                    listForBot2.add(move);
                }
                removeMouseListener(board);
                gameLogic = new GameLogic(new Bot("Bot@001"), new Bot("Bor@002"));
                gameLogic.setDefaultGameForBots(listForBot1, listForBot2);
                board.setGameLogic(gameLogic);
                board.repaint();
                labelMode.setText("Mode: Bots");
                JOptionPane.showMessageDialog(null, "Стратегия загружена успешно", "Info", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        createGameForPlayers.addActionListener(event -> {
            removeMouseListener(board);
            gameLogic = new GameLogic(new Player("Player1"), new Player("Player2"));
            gameLogic.setDefaultGame();
            board.setGameLogic(gameLogic);
            addMouseListener(board);
            board.repaint();
            labelMode.setText("Mode: Players");
        });
    }

    private void customizeJButton(JButton button) {
        button.setForeground(Color.WHITE);
        button.setFont(new Font("TimesRoman", Font.BOLD, 15));
        button.setHorizontalTextPosition(JButton.LEFT);
        button.setBackground(Colors.GREEN_BUTTON.getColor());
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

}
