/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121.poe;
import java.util.Scanner;

/**
 *
 * @author risun
 */
public class Main {
    
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Login login = new Login();

        System.out.println("===== REGISTRATION =====");

        System.out.print("Enter First Name: ");
        String firstName = input.nextLine();

        System.out.print("Enter Last Name: ");
        String lastName = input.nextLine();

        System.out.print("Enter Username: ");
        String username = input.nextLine();

        System.out.print("Enter Password: ");
        String password = input.nextLine();

        System.out.print("Enter Cell Number (+27xxxxxxxxx): ");
        String cellPhone = input.nextLine();

        String registrationResult = login.registerUser(
                username,
                password,
                cellPhone,
                firstName,
                lastName);

        System.out.println(registrationResult);
        
        //only continue to login if the registration details
        //meet the required validation rules.
        if (registrationResult.equals("User successfully registered.")) {

            System.out.println("\n===== LOGIN =====");

            System.out.print("Username: ");
            String loginUsername = input.nextLine();

            System.out.print("Password: ");
            String loginPassword = input.nextLine();

            boolean loggedIn =
                    login.loginUser(loginUsername, loginPassword);

            System.out.println(
                    login.returnLoginStatus(loggedIn));
        }
    }
}
