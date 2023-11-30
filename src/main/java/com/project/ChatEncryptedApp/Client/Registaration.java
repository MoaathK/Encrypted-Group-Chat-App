package com.project.ChatEncryptedApp.Client;

import com.project.ChatEncryptedApp.Entity.User;
import com.project.ChatEncryptedApp.RSA.RSA;
import com.project.ChatEncryptedApp.Repository.UserRepository;
import com.project.ChatEncryptedApp.Service.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class Registaration {

    static String userID, password;
    static Scanner in = new Scanner(System.in);

    static User response;

    static  String response2;
    private static RestTemplate restTemplate = new RestTemplate();

    public static void SignIn() {
        while (true) {
            System.out.print("Please Enter Your UserID To Sign in: ");
             userID = in.next();

            System.out.print("PLease Enter Your Password To Sign in: ");
             password = in.next();

            //getting the object of the user holding userID
            response = restTemplate.getForObject("http://localhost:7033/findbyUserIDUser/" + userID, User.class);

            if (response == null) {
                System.out.println("\nThis UserID Is Not Exist, Try Again");
            }
            else {
                if (response.getPassword().equals(password)) {
                    ConnectSocket();
                    break;
                }
                else {
                    System.out.println("\n UserID Or Password Is Incorrect, Try Again.");
                    Main.Starting();
                }
            }
        }
    }

    private static void ConnectSocket() {

        try {
            Socket socket = new Socket("localhost", 8989);
            System.out.println("Connected to the server.");

            // Start the chat functionality using the socket
            Chatting chatClient = new Chatting(socket, response);
            chatClient.start();
        } catch (IOException e) {
            System.out.println("Error connecting to the server: " + e.getMessage());
        }

    }


    public static void SignUp() {

        System.out.print("Please enter a UserID: ");
        userID = in.next();
        while (true) {
            response = restTemplate.getForObject("http://localhost:7033/findbyUserIDUser/" + userID, User.class);
            if (response == null)
                break;
            System.out.print("\nUserID Already Exist, Please Enter Again: ");
            userID = in.next();
        }
        System.out.print("Please Enter Your Name: ");
        String name = in.nextLine();
        System.out.print("Please Create A Password: ");
        password = in.next();

        final int[] Keys = RSA.keysGenerator();

        User newUser = new User();

        newUser.setUserID(userID);
        newUser.setName(name);
        newUser.setPassword(password);
        newUser.setN(Keys[0]);
        newUser.setPublicKey(Keys[1]);
        newUser.setPrivateKey(Keys[2]);

        response2 = restTemplate.postForObject("http://localhost:7033/adduser", newUser, String.class);

        if(response2.contains("Successfully saved user")) {
            System.out.println("\nYou Signed Up Successfully, Continue To Sign in.");
            SignIn();
        }
        else {
            System.out.println("\nPlease try again");
            Main.Starting();
        }

    }

}