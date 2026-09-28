package com.mycompany.prog5121.poe;

/**
 * Handles user registration and login functionality.
 */
public class Login {

    // Variables used to store registered user information
    private String username;
    private String password;
    private String cellPhone;
    private String firstName;
    private String lastName;

    /**
     * Checks whether the username is correctly formatted.
     * The username must contain an underscore and be
     * no more than five characters long.
     *
     * @param username username to validate
     * @return true if the username is valid, otherwise false
     */
    public boolean checkUserName(String username) {

        return username != null
                && username.contains("_")
                && username.length() <= 7;
    }

    /**
     * Checks whether the password meets the required
     * complexity requirements.
     *
     * The password must:
     * - contain at least eight characters
     * - contain a capital letter
     * - contain a number
     * - contain a special character
     *
     * @param password password to validate
     * @return true if the password meets all requirements
     */
    public boolean checkPasswordComplexity(String password) {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char character : password.toCharArray()) {

            if (Character.isUpperCase(character)) {
                hasCapital = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)) {
                hasSpecial = true;
            }
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Checks whether the South African cell phone number
     * is correctly formatted.
     *
     * The number must start with +27 and contain nine
     * digits after the country code.
     *
     * @param cellPhone cell phone number to validate
     * @return true if the cell phone number is valid
     */
    public boolean checkCellPhoneNumber(String cellPhone) {

        if (cellPhone == null) {
            return false;
        }

        return cellPhone.matches("^\\+27\\d{9}$");
    }

    /**
     * Registers a user after checking the username,
     * password and cell phone number.
     *
     * @param username user's username
     * @param password user's password
     * @param cellPhone user's cell phone number
     * @param firstName user's first name
     * @param lastName user's last name
     * @return registration result message
     */
    public String registerUser(String username,
                               String password,
                               String cellPhone,
                               String firstName,
                               String lastName) {

        // Validate username
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        // Validate password
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        // Validate cell phone number
        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // Store user information after successful validation
        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone;
        this.firstName = firstName;
        this.lastName = lastName;

        return "User successfully registered.";
    }

    /**
     * Checks whether the login username and password
     * match the registered user's details.
     *
     * @param username username entered during login
     * @param password password entered during login
     * @return true if the login details are correct
     */
    public boolean loginUser(String username, String password) {

        if (this.username == null || this.password == null) {
            return false;
        }

        return this.username.equals(username)
                && this.password.equals(password);
    }

    /**
     * Displays the appropriate login status message.
     *
     * @param loginSuccessful indicates whether login was successful
     * @return login status message
     */
    public String returnLoginStatus(boolean loginSuccessful) {

        if (loginSuccessful) {
            return "Welcome " + firstName + ", "
                    + lastName
                    + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}