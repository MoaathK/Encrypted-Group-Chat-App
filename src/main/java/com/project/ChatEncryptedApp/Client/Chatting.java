package com.project.ChatEncryptedApp.Client;

import com.project.ChatEncryptedApp.Entity.User;
import com.project.ChatEncryptedApp.RSA.RSA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Chatting extends Thread {

    private final Socket socket;
    private final User user;

    public Chatting(Socket socket, User user) {
        this.socket = socket;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            BufferedReader read;
            PrintWriter write;
            try {
                read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                write = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Thread messageReceiver = new Thread(() -> {
                try {
                    // receiving messages from clients
                    while (true) {
                        String message = read.readLine();
                        String[] messageArray = message.split("-");
                        String from = messageArray[2];
                        if (!from.equals(user.getUserID())) {
                            if (messageArray[0].equals("ALL")) {
                                System.out.println("Message from " + from + ": " + messageArray[1]);
                            } else {
                                String plainText = RSA.Decrypt(messageArray[1], user.getN(), user.getPrivateKey());
                                System.out.println("Message from " + from + ": " + plainText);
                            }

                        }
                    }
                } catch (IOException e) {
                    System.out.println("Error reading messages from the server: " + e.getMessage());
                }
            });
            messageReceiver.start();

            Scanner scanner = new Scanner(System.in);

            //write messages to clients
            while (true) {
                System.out.println("Please enter the public key of the receiver or (ALL) then the message.");
                String to = scanner.nextLine();
                String msg = scanner.nextLine();
                if (to.toUpperCase().equals("ALL")) {
                    write.println("ALL-" + msg + "-" + user.getUserID());
                    System.out.println("Message sent: " + msg);

                } else {
                    int receiverN = Integer.parseInt(to.replaceAll(" ", "").split(",")[0]);
                    int receiverE = Integer.parseInt(to.replaceAll(" ", "").split(",")[1]);
                    String cipherText = RSA.Enctypt(msg, receiverN, receiverE);
                    write.println("ENCRYPTED-" + cipherText + "-" + user.getUserID());
                    System.out.println("Message sent: " + msg);
                }
            }

        } catch (Exception e) {
            System.out.println("Error in the chat client: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Error closing the socket: " + e.getMessage());
            }
        }
    }
}

