package com.project.ChatEncryptedApp.Client;

import com.project.ChatEncryptedApp.RSA.RSA;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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


import static com.project.ChatEncryptedApp.Client.Registration.response;
import static com.project.ChatEncryptedApp.Client.Registration.socket;

public class Chatting extends Thread implements Initializable {

    //private final Socket socket;
    //private final User user;
    @FXML
    public Pane chatBox;
    @FXML
    public TextField msgField;
    @FXML
    public TextField toField;
    @FXML
    public Button sendButton;
    static PrintWriter write;
    static BufferedReader read;
    @FXML
    public TextArea msgArea;
    public Label userIdProfile;
    public Label publicKeyProfile;

    public Chatting() {

    }
   public Chatting(TextArea m) {
       this.msgArea = m;
    }

    @Override
    public void run() {
        try {
            try {
                read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                write = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                // receiving messages from clients
                while (true) {
                    String message = read.readLine();
                    String[] messageArray = message.split("-");
                    String from = messageArray[2];
                    if (!from.equals(response.getUserID())) {
                        if (messageArray[0].equals("ALL")) {
                            System.out.println("Message from " + from + ": " + messageArray[1]);
                            msgArea.appendText("Message from " + from + ": " + messageArray[1] + "\n");
                        } else {
                            String plainText = RSA.Decrypt(messageArray[1], response.getN(), response.getPrivateKey());
                            System.out.println("Message from " + from + ": " + plainText);
                            msgArea.appendText("Message from " + from + ": " + plainText + "\n");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
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
        String to = toField.getText();
        String msg = msgField.getText();
        if (to.toUpperCase().equals("ALL")) {
            write.println("ALL-" + msg + "-" + response.getUserID());
            System.out.println("Message sent: " + msg);
            msgArea.appendText("Message sent: " + msg + "\n");
        } else {
            int receiverN = Integer.parseInt(to.replaceAll(" ", "").split(",")[1]);
            int receiverE = Integer.parseInt(to.replaceAll(" ", "").split(",")[0]);
            String cipherText = RSA.Enctypt(msg, receiverN, receiverE);
            write.println("ENCRYPTED-" + cipherText + "-" + response.getUserID());
            System.out.println("Message sent: " + msg);
            msgArea.appendText("Message sent: " + msg + "\n");
        }
    }

    private void ConnectSocket() {
        try {
            socket = new Socket("localhost", 8989);
            System.out.println("Connected to the server.");
            // Start the chat functionality using the socket
            Chatting chatClient = new Chatting(msgArea);
            chatClient.start();
        } catch (IOException e) {
            System.out.println("Error connecting to the server: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userIdProfile.setText("User ID: " + response.getUserID());
        publicKeyProfile.setText("Public Key: " + response.getPublicKey() + ", " + response.getN());
        ConnectSocket();
    }
}

