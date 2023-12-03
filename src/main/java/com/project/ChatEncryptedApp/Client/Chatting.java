package com.project.ChatEncryptedApp.Client;

import ch.qos.logback.core.net.server.Client;
import com.project.ChatEncryptedApp.Entity.User;
import com.project.ChatEncryptedApp.RSA.RSA;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import static com.project.ChatEncryptedApp.Client.Registration.response;
import static com.project.ChatEncryptedApp.Client.Registration.socket;


public class Chatting extends Thread implements Initializable {

    //private final Socket socket;
    //private final User user;
    public Pane chatBox;
    public TextField msgField;
    public TextField toField;
    public Button sendButton;
    static PrintWriter write;
    static BufferedReader read;
    @FXML
    public TextArea msgArea;

    public Chatting() {

    }
//    public Chatting(Socket socket, User user) {
//        this.socket = socket;
//        this.user = user;
//    }

    @Override
    public void run() {
        try {
            try {
                read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                write = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //Thread messageReceiver = new Thread( () -> {
                try {
                    // receiving messages from clients
                    while (true) {
                        String message = read.readLine();
                        String[] messageArray = message.split("-");
                        String from = messageArray[2];
                        if (!from.equals(response.getUserID())) {
                            if (messageArray[0].equals("ALL")) {
                                System.out.println("Message from " + from + ": " + messageArray[1]);
                                this.msgArea.appendText("Message from " + from + ": " + messageArray[1] + "\n");
                            }
                            else {
                                String plainText = RSA.Decrypt(messageArray[1], response.getN(), response.getPrivateKey());
                                System.out.println("Message from " + from + ": " + plainText);
                                this.msgArea.appendText("Message from " + from + ": " + plainText + "\n");
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
           // });
           // messageReceiver.start();

        }
        catch (Exception e) {
            System.out.println("Error in the chat client: " + e.getMessage());
        }
//        finally {
//            try {
//                System.out.println("Ddcdc");
//                socket.close();
//            } catch (IOException e) {
//                System.out.println("Error closing the socket: " + e.getMessage());
//            }
//        }
    }
    public void sendMessage() {
        //write messages to clients
        System.out.println("Please enter the public key of the receiver or (ALL) then the message.");
        String to = toField.getText();
        String msg = msgField.getText();
        if (to.toUpperCase().equals("ALL")) {
            write.println("ALL-" + msg + "-" + response.getUserID());
            System.out.println("Message sent: " + msg);
            msgArea.appendText("Message sent: " + msg + "\n");
        } else {
            int receiverN = Integer.parseInt(to.replaceAll(" ", "").split(",")[0]);
            int receiverE = Integer.parseInt(to.replaceAll(" ", "").split(",")[1]);
            String cipherText = RSA.Enctypt(msg, receiverN, receiverE);
            write.println("ENCRYPTED-" + cipherText + "-" + response.getUserID());
            System.out.println("Message sent: " + msg);
            msgArea.appendText("Message sent: " + msg + "\n");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

