package ru.vsu.сs.shemenev.server;

import ru.vsu.сs.shemenev.Player;
import ru.vsu.сs.shemenev.swing.Bot;

import java.io.*;
import java.net.Socket;

public class GameSession implements Runnable {
    private final GameServerLogic gameLogic;
    private BufferedReader reader;
    private PrintWriter out;
    private final Socket socket;

    public GameSession(Socket socket) {
        gameLogic = new GameServerLogic(new Player("Player"), new Bot("Bor@001"));
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!gameLogic.isGameEnd()) {
            try {
                String input = reader.readLine();
                System.out.println("From client:" + input);
                String[] parsedCommand = input.split(":");
                String[] parsedMove = parsedCommand[1].split("->");
                String moveBot = gameLogic.getServerMove(parsedMove[0], parsedMove[1]);
                System.out.println("To client:" + moveBot);
                out.println(moveBot);
                System.out.println(gameLogic.getStatisticsForServer());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        try {
            reader.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
