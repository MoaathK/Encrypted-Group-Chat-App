package com.project.ChatEncryptedApp.Client;

import animatefx.animation.FadeIn;
import com.project.ChatEncryptedApp.Entity.User;
import com.project.ChatEncryptedApp.RSA.RSA;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

@RequiredArgsConstructor
public class Registration {

    static String userID, password;
    static Scanner in = new Scanner(System.in);

    static User response;

    static Socket socket;

    static String response2;
    private static RestTemplate restTemplate = new RestTemplate();
    public Pane pnSignUp;
    public Pane pnSignIn;
    public TextField userIdField; //in sign in
    public TextField passwordField; //in sign in
    public Button loginButton; //in sign in
    public Button signupBottun; // in sign in
    public Label loginFailed; // in sign in, incorrect login
    public TextField userIdField1; // in sign up
    public TextField nameField; // in sign up
    public TextField passwordField1; //in sign up
    public Button signUpButton; // in sign up
    public Button backToLogin; // in sign up
    public Label signUpSuccess; // signup success message
    public Label siguUpExist; // userid already exist

    public void SignIn() {
        userID = userIdField.getText();
        password = passwordField.getText();
        //getting the object of the user holding userID
        response = restTemplate.getForObject("http://localhost:7033/findbyUserIDUser/" + userID, User.class);
        if (response == null) {
            loginFailed.setOpacity(1);
        } else {
            if (response.getPassword().equals(password)) {
                chattingWindow();
                ConnectSocket();
            } else {
                loginFailed.setOpacity(1);
            }

        }
    }

    private static void ConnectSocket() {

        try {
            socket = new Socket("localhost", 8989);
            System.out.println("Connected to the server.");
            // Start the chat functionality using the socket
            Chatting chatClient = new Chatting();
            chatClient.start();
        } catch (IOException e) {
            System.out.println("Error connecting to the server: " + e.getMessage());
        }
    }
    public void SignUp() {

        userID = userIdField1.getText();
        password = passwordField1.getText();
        String name = nameField.getText();

        response = restTemplate.getForObject("http://localhost:7033/findbyUserIDUser/" + userID, User.class);
        if (response == null) {
            final int[] Keys = RSA.keysGenerator();

            User newUser = new User();

            newUser.setUserID(userID);
            newUser.setName(name);
            newUser.setPassword(password);
            newUser.setPublicKey(Keys[0]);
            newUser.setPrivateKey(Keys[1]);
            newUser.setN(Keys[2]);

            response2 = restTemplate.postForObject("http://localhost:7033/adduser", newUser, String.class);

            if (response2.contains("Successfully saved user")) {
                signUpSuccess.setOpacity(1);
                siguUpExist.setOpacity(0);
            }
        } else {
            siguUpExist.setOpacity(1);
        }

    }

    public void chattingWindow() {
        try {
            Stage stage = (Stage) userIdField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Chatting.fxml"));
            stage.setScene(new Scene(root, 330, 560));
            stage.setTitle(response.getName());
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource().equals(signupBottun)) {
            new FadeIn(pnSignUp).play();
            pnSignUp.toFront();
        }
        if (event.getSource().equals(backToLogin)) {
            new FadeIn(pnSignIn).play();
            pnSignIn.toFront();
        }

    }

}