
package com.mycompany.prog5121.poe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * Handles message creation, validation, storage and display.
 */
public class Message {

    private String messageID;
    private String recipient;
    private String message;
    private String messageHash;
    private int messageNumber;
    private String sender;
    private String status = "";

    private static int totalMessages = 0;

    // Collection 1: All sent messages
    private static ArrayList<String> sentMessages = new ArrayList<>();

    // Collection 2: All disregarded messages
    private static ArrayList<String> disregardedMessages = new ArrayList<>();

    // Collection 3: All stored messages
    private static ArrayList<Message> storedMessages = new ArrayList<>();

    // Collection 4: All message hashes
    private static ArrayList<String> messageHashes = new ArrayList<>();

    // Collection 5: All message IDs
    private static ArrayList<String> messageIDs = new ArrayList<>();

    /**
     * Constructor that uses Developer as the default sender.
     */
    public Message(String recipient, String message) {
        this("Developer", recipient, message);
    }

    /**
     * Constructor with sender, recipient and message.
     */
    public Message(String sender, String recipient, String message) {

        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.messageNumber = totalMessages + 1;

        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();

        messageIDs.add(messageID);
        messageHashes.add(messageHash);
    }

    /**
     * Constructor used when loading an existing stored message.
     * It preserves the original ID and hash.
     */
    private Message(String sender, String recipient, String message,
                    String messageID, String messageHash) {

        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.messageID = messageID;
        this.messageHash = messageHash;
        this.status = "Stored";
        this.messageNumber = 0;
    }

    /**
     * Generates a random 10-digit message ID.
     */
    private String generateMessageID() {

        Random random = new Random();

        long id = 1000000000L
                + (long) (random.nextDouble() * 9000000000L);

        return String.valueOf(id);
    }

    /**
     * Checks that the message ID is not more than 10 characters.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Validates the recipient's cell phone number.
     */
    public String checkRecipientCell() {

        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /**
     * Validates message length.
     */
    public String validateMessage() {

        if (message != null && message.length() <= 250) {
            return "Message ready to send.";
        }

        int excess = message == null ? 250 : message.length() - 250;

        return "Message exceeds 250 characters by "
                + excess
                + ", please reduce the size.";
    }

    /**
     * Creates the message hash.
     */
    public String createMessageHash() {

        String cleanMessage = message == null
                ? ""
                : message.replaceAll("[^a-zA-Z0-9 ]", "");

        String[] words = cleanMessage.trim().isEmpty()
                ? new String[]{""}
                : cleanMessage.trim().split("\\s+");

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return messageID.substring(0, 2)
                + ":"
                + messageNumber
                + ":"
                + (firstWord + lastWord).toUpperCase();
    }

    /**
     * Handles send, disregard and store options.
     */
    public String sendMessage(int option) {

        switch (option) {

            case 1:
                status = "Sent";
                totalMessages++;

                sentMessages.add(message);

                return "Message successfully sent.";

            case 2:
                status = "Disregarded";

                disregardedMessages.add(message);

                return "Message disregarded.";

            case 3:
                status = "Stored";

                storeMessage();

                if (!storedMessages.contains(this)) {
                    storedMessages.add(this);
                }

                return "Message successfully stored.";

            default:
                return "Invalid option.";
        }
    }

    /**
     * Stores a message in messages.json.
     */
    public void storeMessage() {

        JSONObject obj = new JSONObject();

        obj.put("MessageID", messageID);
        obj.put("MessageHash", messageHash);
        obj.put("Sender", sender);
        obj.put("Recipient", recipient);
        obj.put("Message", message);
        obj.put("Status", status);

        try (FileWriter file = new FileWriter("messages.json", true)) {

            file.write(obj.toJSONString());
            file.write(System.lineSeparator());

        } catch (IOException e) {

            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    /**
     * Loads stored messages from the JSON file.
     */
    public static void loadStoredMessages() {

        storedMessages.clear();

        File file = new File("messages.json");

        if (!file.exists()) {
            return;
        }

        JSONParser parser = new JSONParser();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                JSONObject obj = (JSONObject) parser.parse(line);

                String sender = obj.get("Sender") == null
                        ? "Developer"
                        : (String) obj.get("Sender");

                String recipient = (String) obj.get("Recipient");
                String message = (String) obj.get("Message");
                String id = (String) obj.get("MessageID");
                String hash = (String) obj.get("MessageHash");

                if (recipient == null || message == null
                        || id == null || hash == null) {
                    continue;
                }

                Message storedMessage = new Message(
                        sender, recipient, message, id, hash
                );

                storedMessages.add(storedMessage);

                if (!messageIDs.contains(id)) {
                    messageIDs.add(id);
                }

                if (!messageHashes.contains(hash)) {
                    messageHashes.add(hash);
                }
            }

        } catch (Exception e) {

            System.out.println("Error loading stored messages: "
                    + e.getMessage());
        }
    }

    /**
     * Displays the sender and recipient of stored messages.
     */
    public static String displayStoredSendersAndRecipients() {

        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        StringBuilder output = new StringBuilder();

        for (Message msg : storedMessages) {

            output.append("Sender: ")
                    .append(msg.sender)
                    .append("\nRecipient: ")
                    .append(msg.recipient)
                    .append("\n\n");
        }

        return output.toString();
    }

    /**
     * Finds the longest stored message.
     */
    public static String displayLongestMessage() {

        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }

        Message longest = storedMessages.get(0);

        for (Message msg : storedMessages) {

            if (msg.message.length() > longest.message.length()) {
                longest = msg;
            }
        }

        return longest.message;
    }

    /**
     * Searches for a stored message using its ID.
     */
    public static String searchMessageByID(String id) {

        loadStoredMessages();

        for (Message msg : storedMessages) {

            if (msg.messageID.equals(id)) {

                return "Recipient: " + msg.recipient
                        + "\nMessage: " + msg.message;
            }
        }

        return "Message ID not found.";
    }

    /**
     * Searches for all stored messages belonging to a recipient.
     */
    public static String searchMessagesByRecipient(String recipientNumber) {

        loadStoredMessages();

        StringBuilder output = new StringBuilder();

        for (Message msg : storedMessages) {

            if (msg.recipient.equals(recipientNumber)) {

                output.append("Message ID: ")
                        .append(msg.messageID)
                        .append("\nMessage: ")
                        .append(msg.message)
                        .append("\n\n");
            }
        }

        if (output.length() == 0) {
            return "No messages found for this recipient.";
        }

        return output.toString();
    }

    /**
     * Deletes a stored message using its message hash.
     */
    public static String deleteMessageByHash(String hash) {

        loadStoredMessages();

        boolean removed = storedMessages.removeIf(
                msg -> msg.messageHash.equals(hash)
        );

        if (!removed) {
            return "Message hash not found.";
        }

        if (!rewriteJSONFile()) {
            return "Could not update the JSON file. Message was not deleted.";
        }

        messageIDs.removeIf(id ->
                storedMessages.stream().noneMatch(msg -> msg.messageID.equals(id))
        );

        messageHashes.removeIf(savedHash ->
                storedMessages.stream().noneMatch(msg -> msg.messageHash.equals(savedHash))
        );

        return "Message successfully deleted.";
    }

    /**
     * Rewrites the JSON file after deleting a message.
     */
    private static boolean rewriteJSONFile() {

        try (FileWriter file = new FileWriter("messages.json", false)) {

            for (Message msg : storedMessages) {

                JSONObject obj = new JSONObject();

                obj.put("MessageID", msg.messageID);
                obj.put("MessageHash", msg.messageHash);
                obj.put("Sender", msg.sender);
                obj.put("Recipient", msg.recipient);
                obj.put("Message", msg.message);
                obj.put("Status", "Stored");

                file.write(obj.toJSONString());
                file.write(System.lineSeparator());
            }

            return true;

        } catch (IOException e) {

            System.out.println("Error updating JSON file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Displays a complete report of all stored messages.
     */
    public static String displayReport() {

        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }

        StringBuilder report = new StringBuilder();

        report.append("========== STORED MESSAGES REPORT ==========\n\n");

        for (Message msg : storedMessages) {

            report.append("Message ID: ")
                    .append(msg.messageID)
                    .append("\n");

            report.append("Message Hash: ")
                    .append(msg.messageHash)
                    .append("\n");

            report.append("Sender: ")
                    .append(msg.sender)
                    .append("\n");

            report.append("Recipient: ")
                    .append(msg.recipient)
                    .append("\n");

            report.append("Message: ")
                    .append(msg.message)
                    .append("\n");

            report.append("--------------------------------------------\n");
        }

        return report.toString();
    }

    /**
     * Displays a sent message.
     */
    public String printMessage() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nSender: " + sender
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    /**
     * Returns all messages sent.
     */
    public static String printMessages() {

        StringBuilder output = new StringBuilder();

        for (String msg : sentMessages) {
            output.append(msg).append("\n\n");
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
     * Returns the message ID.
     */
    public String getMessageID() {
        return messageID;
    }

    /**
     * Returns the message hash.
     */
    public String getMessageHash() {
        return messageHash;
    }

    /**
     * Returns the recipient.
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Returns the sender.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the message status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Returns all sent messages.
     */
    public static ArrayList<String> getSentMessages() {
        return sentMessages;
    }

    /**
     * Returns all disregarded messages.
     */
    public static ArrayList<String> getDisregardedMessages() {
        return disregardedMessages;
    }

    /**
     * Returns all stored messages.
     */
    public static ArrayList<Message> getStoredMessages() {
        loadStoredMessages();
        return storedMessages;
    }

    /**
     * Returns all message hashes.
     */
    public static ArrayList<String> getMessageHashes() {
        return messageHashes;
    }

    /**
     * Returns all message IDs.
     */
    public static ArrayList<String> getMessageIDs() {
        return messageIDs;
    }
}
