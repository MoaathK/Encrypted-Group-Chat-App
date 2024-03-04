package com.project.ChatEncryptedApp.Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Scanner;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

//    public static void Starting() {
//        Scanner in = new Scanner(System.in);
//
//        System.out.println("Please ِEِِِِِِnter 1 For Sign in Or 2 For Sign Up: ");
//
//        int ans = in.nextInt();
//
//        if (ans == 1) {
//            Registration.SignIn();
//        }
//        else if(ans == 2) {
//            Registration.SignUp();
//        }
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/Registration.fxml"));
        primaryStage.setTitle("Encrypted Group Chat");
        primaryStage.setScene(new Scene(root, 330, 560));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
