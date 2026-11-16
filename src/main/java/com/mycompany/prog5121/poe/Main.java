package com.mycompany.prog5121.poe;

import java.util.Scanner;

public class Main {

    private static final Scanner input =
            new Scanner(System.in);

    public static void main(String[] args) {

        boolean running = true;

        while (running) {

            System.out.println("\n================================");
            System.out.println(" QUICKCHAT SYSTEM ");
            System.out.println("================================");
            System.out.println("1. Create Message");
            System.out.println("2. View Sent Messages");
            System.out.println("3. View Stored Messages Report");
            System.out.println("4. Search Message by ID");
            System.out.println("5. Search Messages by Recipient");
            System.out.println("6. Delete Message by Hash");
            System.out.println("7. Show Longest Message");
            System.out.println("8. Show Total Messages Sent");
            System.out.println("9. Exit");

            System.out.print("\nSelect option: ");

            int choice = Integer.parseInt(
                    input.nextLine());

            switch (choice) {

                case 1:
                    createMessage();
                    break;

                case 2:
                    viewSentMessages();
                    break;

                case 3:
                    viewStoredReport();
                    break;

                case 4:
                    searchByID();
                    break;

                case 5:
                    searchByRecipient();
                    break;

                case 6:
                    deleteByHash();
                    break;

                case 7:
                    showLongestMessage();
                    break;

                case 8:
                    showTotalMessages();
                    break;

                case 9:

                    running = false;

                    System.out.println(
                            "Closing QuickChat..."
                    );

                    break;

                default:

                    System.out.println(
                            "Invalid selection."
                    );
            }
        }
    }

    /**
     * Create Message
     */
    private static void createMessage() {

        System.out.print(
                "\nEnter Recipient Number (+27xxxxxxxxx): ");

        String recipient =
                input.nextLine();

        System.out.print(
                "Enter Message: ");

        String text =
                input.nextLine();

        Message msg =
                new Message(recipient, text);

        System.out.println(
                "\n" + msg.checkRecipientCell());

        System.out.println(
                msg.validateMessage());

        if (!msg.checkRecipientCell()
                .equals(
                        "Cell phone number successfully captured.")) {

            return;
        }

        if (!msg.validateMessage()
                .equals(
                        "Message ready to send.")) {

            return;
        }

        System.out.println("\nChoose Option");
        System.out.println("1. Send");
        System.out.println("2. Disregard");
        System.out.println("3. Store");

        int option =
                Integer.parseInt(
                        input.nextLine());

        System.out.println(
                "\n" + msg.sendMessage(option));

        System.out.println(
                "\nMessage Details");

        System.out.println(
                msg.printMessage());
    }

    /**
     * View Sent Messages
     */
    private static void viewSentMessages() {

        String messages =
                Message.printMessages();

        if (messages.isEmpty()) {

            System.out.println(
                    "\nNo sent messages.");
        } else {

            System.out.println(
                    "\n=== SENT MESSAGES ===");

            System.out.println(messages);
        }
    }

    /**
     * Display Stored Report
     */ 
    private static void viewStoredReport() {

        System.out.println(
                "\n" + Message.displayReport());
    }

    /**
     * Search by Message ID
     */
    private static void searchByID() {

        System.out.print(
                "\nEnter Message ID: ");

        String id =
                input.nextLine();

        System.out.println(
                "\n" +
                Message.searchMessageByID(id)
        );
    }

    /**
     * Search by Recipient
     */
    private static void searchByRecipient() {

        System.out.print(
                "\nEnter Recipient Number: ");

        String recipient =
                input.nextLine();

        System.out.println(
                "\n" +
                Message.searchMessagesByRecipient(
                        recipient)
        );
    }

    /**
     * Delete Message by Hash
     */
    private static void deleteByHash() {

        System.out.print(
                "\nEnter Message Hash: ");

        String hash =
                input.nextLine();

        System.out.println(
                "\n" +
                Message.deleteMessageByHash(
                        hash)
        );
    }

    /**
     * Longest Message
     */
    private static void showLongestMessage() {

        System.out.println(
                "\nLongest Stored Message:"
        );

        System.out.println(
                Message.displayLongestMessage());
    }

    /**
     * Total Messages Sent
     */
    private static void showTotalMessages() {

        System.out.println(
                "\nTotal Messages Sent: "
                +
                Message.returnTotalMessages());
    }
}