/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121.poe;

/**
 *
 * @author risun
 */
public class Login {

    // Variables used to store user information
    private String username;
    private String password;
    private String cellPhone;
    private String firstName;
    private String lastName;

    // Method to check if the username is valid
    // Username must contain an underscore (_) and be 5 characters or less
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    // Method to check password complexity
    public boolean checkPasswordComplexity(String password) {

        // Flags used to check each requirement
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // Loop through every character in the password
        for (char c : password.toCharArray()) {

            // Check for uppercase letter
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            }

            // Check for number
            if (Character.isDigit(c)) {
                hasNumber = true;
            }

            // Check for special character
            if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        // Return true only if all requirements are met
        return password.length() >= 8
                && hasCapital
                && hasNumber
                && hasSpecial;
    }

    // Method to validate a South African cell number
    // Number must start with +27 and contain 9 digits after that
    public boolean checkCellPhoneNumber(String cellPhone) {

        return cellPhone.matches("^\\+27\\d{9}$");
    }

    // Method used to register a new user
    public String registerUser(String username,
                               String password,
                               String cellPhone,
                               String firstName,
                               String lastName) {

        // Check username format
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        // Check password format
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        // Check cell phone number format
        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // Save user details if all validations pass
        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone;
        this.firstName = firstName;
        this.lastName = lastName;

        return "User successfully registered.";
    }

    // Method to verify login details
    public boolean loginUser(String username, String password) {

        // Compare entered details with stored details
        return this.username.equals(username)
                && this.password.equals(password);
    }

    // Method to display login result
    public String returnLoginStatus(boolean loginSuccessful) {

        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}

