
package com.mycompany.prog5121.poe;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Login login = new Login();

        // ================= REGISTRATION =================
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
                username, password, cellPhone, firstName, lastName
        );

        System.out.println(registrationResult);

        if (!"User successfully registered.".equals(registrationResult)) {
            input.close();
            return;
        }

        // ================= LOGIN =================
        System.out.println("\n===== LOGIN =====");

        System.out.print("Username: ");
        String loginUsername = input.nextLine();

        System.out.print("Password: ");
        String loginPassword = input.nextLine();

        boolean loggedIn = login.loginUser(
                loginUsername, loginPassword
        );

        System.out.println(login.returnLoginStatus(loggedIn));

        if (!loggedIn) {
            input.close();
            return;
        }

        // ================= QUICKCHAT =================
        System.out.println("\nWelcome to QuickChat!");

        int numMessages = readNonNegativeInt(
                input, "How many messages would you like to send? "
        );

        int choice;

        do {
            System.out.println("\n===== QUICKCHAT =====");
            System.out.println("1. Send Messages");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. Quit");

            choice = readInt(input, "Choose an option: ");

            switch (choice) {

                case 1:

                    for (int i = 0; i < numMessages; i++) {

                        System.out.println("\nMessage " + (i + 1));

                        System.out.print("Recipient Number (+27xxxxxxxxx): ");
                        String recipient = input.nextLine();

                        System.out.print("Enter Message: ");
                        String messageText = input.nextLine();

                        Message message = new Message(
                                recipient, messageText
                        );

                        String recipientResult =
                                message.checkRecipientCell();

                        String validationResult =
                                message.validateMessage();

                        System.out.println(recipientResult);
                        System.out.println(validationResult);

                        if (!"Cell phone number successfully captured."
                                .equals(recipientResult)
                                || !"Message ready to send."
                                .equals(validationResult)) {

                            System.out.println(
                                    "Message was not sent. "
                                    + "Please correct the details."
                            );
                            continue;
                        }

                        System.out.println("\n1. Send Message");
                        System.out.println("2. Disregard Message");
                        System.out.println("3. Store Message");

                        int sendChoice = readInt(
                                input, "Choose an option: "
                        );

                        if (sendChoice < 1 || sendChoice > 3) {
                            System.out.println("Invalid option.");
                            continue;
                        }

                        System.out.println(
                                message.sendMessage(sendChoice)
                        );

                        if (sendChoice == 1) {
                            System.out.println(message.printMessage());
                        }
                    }

                    break;

                case 2:

                    System.out.println(Message.printMessages());
                    break;

                case 3:

                    System.out.println("Goodbye.");
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose 1, 2, or 3."
                    );
            }

        } while (choice != 3);

        System.out.println(
                "\nTotal Messages Sent: "
                + Message.returnTotalMessages()
        );

        input.close();
    }

    // Read an integer safely
    private static int readInt(Scanner input, String prompt) {

        while (true) {
            System.out.print(prompt);

            if (input.hasNextInt()) {
                int number = input.nextInt();
                input.nextLine();
                return number;
            }

            System.out.println("Invalid input. Please enter a number.");
            input.nextLine();
        }
    }

    // Read zero or a positive integer
    private static int readNonNegativeInt(
            Scanner input, String prompt) {

        while (true) {
            int number = readInt(input, prompt);

            if (number >= 0) {
                return number;
            }

            System.out.println(
                    "Please enter zero or a positive number."
            );
        }
    }
}
