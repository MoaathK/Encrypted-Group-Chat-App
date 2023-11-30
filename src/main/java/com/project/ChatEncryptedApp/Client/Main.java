package com.project.ChatEncryptedApp.Client;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Starting();
    }

    public static void Starting() {
        Scanner in = new Scanner(System.in);

        System.out.println("Please ِEِِِِِِnter 1 For Sign in Or 2 For Sign Up: ");

        int ans = in.nextInt();

        if (ans == 1) {
            Registaration.SignIn();
        }
        else if(ans == 2) {
            Registaration.SignUp();
        }
    }

}
