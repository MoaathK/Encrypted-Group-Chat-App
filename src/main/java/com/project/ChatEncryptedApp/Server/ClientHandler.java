package com.project.ChatEncryptedApp.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {
    private final Socket socket;
    private final BufferedReader reader;
    private final List<PrintWriter> clientWriters;

    public ClientHandler(Socket socket, List<PrintWriter> clientWriters) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.clientWriters = clientWriters;
    }

    public void run() {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            clientWriters.add(writer);

            String message;

            //server output
            while ((message = reader.readLine()) != null) {
                System.out.println("Received message from client: " + message);
                broadcastMessage(message);
            }
        }
        catch (IOException e) {
            System.out.println("Error handling client connection: " + e.getMessage());
        }
        finally {
            try {
                 socket.close();
            }
            catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    private void broadcastMessage(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }
}