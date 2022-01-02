package ru.vsu.сs.shemenev.server;

import ru.vsu.сs.shemenev.GameLogic;
import ru.vsu.сs.shemenev.Player;
import ru.vsu.сs.shemenev.swing.Board;
import ru.vsu.сs.shemenev.swing.Bot;
import ru.vsu.сs.shemenev.swing.Colors;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame {
    private final String host;
    private static final int size = 800;
    private final int port;
    private final Board board;
    private final GameLogic gameLogic;

    public static void main(String[] args) {
        GameLogic gameLogic = new GameLogic(new Player("Player1"), new Bot("Bot@001"));
        gameLogic.setDefaultGame();
        Board board = new Board(size, gameLogic);
        Client client = new Client("localhost", Integer.parseInt(args[0]), board, gameLogic);
        client.start();
    }

    public Client(String host, int port, Board board, GameLogic gameLogic) {
        this.host = host;
        this.port = port;
        this.board = board;
        this.gameLogic = gameLogic;
    }

    private void draw() {
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("CheckersOnline");
        pack();
        setSize(size, size);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
        getContentPane().setBackground(Colors.ORANGE_LIGHT_BACKGROUND.getColor());
        add(board);
        addMouseListener(board);
    }

    public void start() {
        draw();
        Socket socket;
        BufferedReader reader;
        PrintWriter writer;
        try {
            socket = new Socket(host, port);
            //reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //reader = new ObjectInputStream(socket.getInputStream());
            //writer = new PrintWriter(socket.getOutputStream(), true);
            //phone = new Phone(socket);
//        while (!socket.isClosed()) {
//            if (board.getFirstClick() != null && board.getSecondClick() != null) {
//                //writer.println(board.getFirstClick() + " " + board.getSecondClick());
//                Phone phone = new Phone(socket);
//                phone.writeLine(board.getFirstClick() + " " + board.getSecondClick());
//                board.resetClicks();
//                break;
//            }
//        }
//        //Packet packet = (Packet) phone.readObj();
//        //System.out.println(packet.getStart() + "->" + packet.getTarget());
//        Phone phone = new Phone(socket);
//        String command = phone.readLine();
//        //command = command.substring(4);
//        System.out.println(command);
//        while (!socket.isClosed()) {
//            if ((command == null)) {
//                //command = reader.readLine();
//                command = phone.readLine();
//            }
//            String[] parsed = command.split(" ");
//            System.out.println("From server:" + command);
//            if (parsed[0].equals("END")) {
//                socket.close();
//            }
//            gameLogic.createMove(parsed[0], parsed[1]);
//            //board.setGameLogic(gameLogic);
//            board.repaint();
//            while (!socket.isClosed()) {
//                if (board.getFirstClick() != null && board.getSecondClick() != null) {
//                    //writer.println(board.getFirstClick() + " " + board.getSecondClick());
//                    phone.writeLine(board.getFirstClick() + " " + board.getSecondClick());
//                    board.resetClicks();
//                    command = null;
//                    break;
//                }
//            }
//            //System.out.println(reader.readLine());
////            GameLogic gameUpdate = (GameLogic) reader.readObject();
////            System.out.println(10);
////            board.setGameLogic(gameUpdate);
////            board.repaint();
//        }
            //Phone phone;
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            this.dispose();
            e.printStackTrace();
            return;
        }
        while (!socket.isClosed()) {
            if (board.getFirstClick() != null && board.getSecondClick() != null) {
                String outputStr = Command.MOVE.getCommandString() + Command.SEPARATOR +
                        board.getFirstClick() + "->" + board.getSecondClick();
                System.out.println("To server:" + outputStr);
                writer.println(outputStr);
                board.resetClicks();
            } else {
                continue;
            }
            try {
                String command = reader.readLine();
                String[] parsedCommand = command.split(":");
                System.out.println("From server:" + command);
                if (parsedCommand[0].equals("WIN")) {
                    JOptionPane.showMessageDialog(null, "Вы выиграли! Бот сдался!");
                    this.dispose();
                    socket.close();
                    continue;
                }
                String[] parsedMove = parsedCommand[1].split("->");
                gameLogic.createMove(parsedMove[0], parsedMove[1]);
                board.repaint();
                if (parsedCommand[0].equals("LOSE")) {
                    JOptionPane.showMessageDialog(null, "Вы проиграли!");
                    socket.close();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                this.dispose();
                return;
            }
        }
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
