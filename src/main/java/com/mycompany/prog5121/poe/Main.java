package com.mycompany.prog5121.poe;

import java.util.Scanner;

/**
 * Main class used to run the registration and login system.
 */
public class Main {

    public static void main(String[] args) {

        // Create Scanner object for keyboard input
        Scanner input = new Scanner(System.in);

        // Create Login object
        Login login = new Login();

        System.out.println("===== REGISTRATION =====");

        // Get user's first name
        System.out.print("Enter First Name: ");
        String firstName = input.nextLine();

        // Get user's last name
        System.out.print("Enter Last Name: ");
        String lastName = input.nextLine();

        // Get username
        System.out.print("Enter Username: ");
        String username = input.nextLine();

        // Get password
        System.out.print("Enter Password: ");
        String password = input.nextLine();

        // Get cell phone number
        System.out.print("Enter Cell Number (+27xxxxxxxxx): ");
        String cellPhone = input.nextLine();

        // Register the user
        String registrationResult = login.registerUser(
                username,
                password,
                cellPhone,
                firstName,
                lastName
        );

        // Display registration result
        System.out.println(registrationResult);

        // Continue to login only if registration was successful
        if (registrationResult.equals("User successfully registered.")) {

            System.out.println("\n===== LOGIN =====");

            // Get login username
            System.out.print("Username: ");
            String loginUsername = input.nextLine();

            // Get login password
            System.out.print("Password: ");
            String loginPassword = input.nextLine();

            // Check login details
            boolean loggedIn = login.loginUser(
                    loginUsername,
                    loginPassword
            );

            // Display login status
            System.out.println(
                    login.returnLoginStatus(loggedIn)
            );
        }

        // Close Scanner
        input.close();
    }
}