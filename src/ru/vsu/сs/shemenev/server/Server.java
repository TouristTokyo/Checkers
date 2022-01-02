package ru.vsu.сs.shemenev.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket1;


    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            Server server = new Server(port);
            server.start();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot start the server", e);
        }
    }
    public Server(int port) throws IOException {
        serverSocket1 = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("Game server started");
        while (true) {
            Socket clientSocket = serverSocket1.accept();
            System.out.println("Client connected from: "+ clientSocket.getInetAddress());
            GameSession gameSession = new GameSession(clientSocket);
            Thread t = new Thread(gameSession);
            t.start();
        }
    }
}
