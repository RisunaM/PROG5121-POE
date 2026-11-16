
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

        // Load previously stored messages
        Message.loadStoredMessages();

        int numMessages = readNonNegativeInt(
                input, "How many messages would you like to send? "
        );

        int choice;

        do {
            System.out.println("\n===== QUICKCHAT =====");
            System.out.println("1. Send Messages");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");

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

                case 4:

                    storedMessagesMenu(input);
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose 1, 2, 3, or 4."
                    );
            }

        } while (choice != 3);

        System.out.println(
                "\nTotal Messages Sent: "
                + Message.returnTotalMessages()
        );

        input.close();
    }

    // ================= STORED MESSAGES MENU =================

    private static void storedMessagesMenu(Scanner input) {

        String option;

        do {
            System.out.println("\n===== STORED MESSAGES =====");
            System.out.println("a. Display sender and recipient of stored messages");
            System.out.println("b. Display the longest stored message");
            System.out.println("c. Search for a message using Message ID");
            System.out.println("d. Display messages for a particular recipient");
            System.out.println("e. Delete a message using its Message Hash");
            System.out.println("f. Display a full report of stored messages");
            System.out.println("g. Return to main menu");

            System.out.print("Choose an option: ");
            option = input.nextLine().trim().toLowerCase();

            switch (option) {

                case "a":

                    System.out.println(
                            Message.displayStoredSendersAndRecipients()
                    );
                    break;

                case "b":

                    System.out.println(
                            "Longest stored message:\n"
                            + Message.displayLongestMessage()
                    );
                    break;

                case "c":

                    System.out.print("Enter Message ID: ");
                    String messageID = input.nextLine();

                    System.out.println(
                            Message.searchMessageByID(messageID)
                    );
                    break;

                case "d":

                    System.out.print("Enter recipient number: ");
                    String recipient = input.nextLine();

                    System.out.println(
                            Message.searchMessagesByRecipient(recipient)
                    );
                    break;

                case "e":

                    System.out.print("Enter Message Hash: ");
                    String messageHash = input.nextLine();

                    System.out.println(
                            Message.deleteMessageByHash(messageHash)
                    );
                    break;

                case "f":

                    System.out.println(Message.displayReport());
                    break;

                case "g":

                    System.out.println("Returning to main menu...");
                    break;

                default:

                    System.out.println(
                            "Invalid option. Please choose a to g."
                    );
            }

        } while (!option.equals("g"));
    }

    // ================= INPUT VALIDATION =================

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
