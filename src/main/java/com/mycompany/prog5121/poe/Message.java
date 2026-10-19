package com.mycompany.prog5121.poe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONObject;

/**
 * Handles message creation, validation, storage and display.
 */
public class Message {

    private String messageID;
    private String recipient;
    private String message;
    private String messageHash;
    private int messageNumber;

    private static int totalMessages = 0;

    private static ArrayList<String> sentMessages =
            new ArrayList<>();

    /**
     * Constructor
     */
    public Message(String recipient,
                   String message) {

        this.recipient = recipient;
        this.message = message;
        this.messageNumber = totalMessages;

        this.messageID = generateMessageID();

        this.messageHash = createMessageHash();
    }

    /**
     * Generates a random 10-digit message ID.
     */
    private String generateMessageID() {

        Random random = new Random();

        long id =
                1000000000L
                + (long) (random.nextDouble()
                * 9000000000L);

        return String.valueOf(id);
    }

    /**
     * Checks that Message ID is not more than
     * 10 characters long.
     */
    public boolean checkMessageID() {

        return messageID.length() <= 10;
    }

    /**
     * Validates recipient number.
     */
    public String checkRecipientCell() {

        if (recipient.matches("^\\+27\\d{9}$")) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /**
     * Validates message length.
     */
    public String validateMessage() {

        if (message.length() <= 250) {

            return "Message ready to send.";
        }

        int excess = message.length() - 240;

        return "Message exceeds 250 characters by "
                + excess
                + ", please reduce the size.";
    }

    /**s
     * Creates the message hash.
     */
    public String createMessageHash() {

        String cleanMessage =
                message.replaceAll(
                        "[^a-zA-Z0-9 ]",
                        ""
                );

        String[] words =
                cleanMessage.trim()
                        .split("\\s+");

        String firstWord = words[0];

        String lastWord =
                words[words.length - 1];

        return messageID.substring(0, 2)
                + ":"
                + messageNumber
                + ":"
                + (firstWord + lastWord)
                .toUpperCase();
    }

    /**
     * Handles send/store/disregard options.
     */
    public String sendMessage(int option) {

        switch (option) {

            case 1:

                totalMessages++;

                sentMessages.add(
                        printMessage()
                );

                return "Message successfully sent.";

            case 2:

                return "Press 0 to delete the message.";

            case 3:

                storeMessage();

                return "Message successfully stored.";

            default:

                return "Invalid option.";
        }
    }

    /**
     * Stores message into JSON file.
     */
    public void storeMessage() {

        JSONObject obj =
                new JSONObject();

        obj.put("MessageID", messageID);
        obj.put("MessageHash", messageHash);
        obj.put("Recipient", recipient);
        obj.put("Message", message);

        try {

            FileWriter file =
                    new FileWriter(
                            "messages.json",
                            true
                    );

            file.write(
                    obj.toJSONString()
            );

            file.write(
                    System.lineSeparator()
            );

            file.close();

        } catch (IOException e) {

            System.out.println(
                    "Error storing message."
            );
        }
    }

    /**
     * Displays a sent message.
     */
    public String printMessage() {

        return
                "Message ID: "
                + messageID
                + "\nMessage Hash: "
                + messageHash
                + "\nRecipient: "
                + recipient
                + "\nMessage: "
                + message;
    }

    /**
     * Returns all messages sent.
     */
    public static String printMessages() {

        StringBuilder output =
                new StringBuilder();

        for (String msg : sentMessages) {

            output.append(msg)
                  .append("\n\n");
        }

        return output.toString();
    }

    /**
     * Returns total messages sent.
     */
    public static int returnTotalMessages() {

        return totalMessages;
    }

    /**
     * Getter for Message ID.
     */
    public String getMessageID() {

        return messageID;
    }

    /**
     * Getter for Message Hash.
     */
    public String getMessageHash() {

        return messageHash;
    }

    /**
     * Getter for Recipient.
     */
    public String getRecipient() {

        return recipient;
    }

    /**
     * Getter for Message.
     */
    public String getMessage() {

        return message;
    }
}