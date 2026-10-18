
package com.mycompany.prog5121.poe;

import java.util.Random;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Message {

    private String messageID;
    private String recipient;
    private String message;
    private String messageHash;

    private static int totalMessages = 0;

    private static final ArrayList<String> messages =
            new ArrayList<>();

    private static final Path JSON_FILE =
            Paths.get("messages.json");

    public Message(String recipient, String message) {

        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.message = message;

        this.messageHash = createMessageHash();
    }

    // Generate a random 10-digit message ID
    private String generateMessageID() {

        Random random = new Random();

        long number = 1000000000L
                + (long) (random.nextDouble() * 9000000000L);

        return String.valueOf(number);
    }

    // Check that the message ID contains no more than 10 digits
    public boolean checkMessageID() {

        return messageID != null
                && messageID.matches("\\d{1,10}");
    }

    // Validate the recipient's South African cell phone number
    public String checkRecipientCell() {

        if (recipient != null
                && recipient.matches("^\\+27\\d{9}$")) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.";
    }

    // Validate the message length
    public String validateMessage() {

        if (message == null || message.trim().isEmpty()) {
            return "Message cannot be empty.";
        }

        if (message.length() <= 250) {
            return "Message ready to send.";
        }

        int excess = message.length() - 250;

        return "Message exceeds 250 characters by "
                + excess
                + ", please reduce the size.";
    }

    // Create a message hash
    public String createMessageHash() {

        String safeMessage = (message == null) ? "" : message.trim();

        String[] words = safeMessage.isEmpty()
                ? new String[0]
                : safeMessage.split("\\s+");

        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 0
                ? words[words.length - 1] : "";

        return messageID.substring(0, 2)
                + ":"
                + (totalMessages + 1)
                + ":"
                + (firstWord + lastWord).toUpperCase();
    }

    // Send, disregard, or store a message
    public String sendMessage(int option) {

        switch (option) {

            case 1:

                if (!checkRecipientCell().equals(
                        "Cell phone number successfully captured.")) {
                    return "Message not sent. Please enter a valid recipient number.";
                }

                if (!validateMessage().equals(
                        "Message ready to send.")) {
                    return "Message not sent. Please correct the message.";
                }

                totalMessages++;

                // Refresh the hash using the sent-message count
                messageHash = createMessageHashForSentMessage();

                messages.add(printMessage());

                return "Message successfully sent.";

            case 2:

                return "Message disregarded.";

            case 3:

                if (!checkRecipientCell().equals(
                        "Cell phone number successfully captured.")) {
                    return "Message not stored. Please enter a valid recipient number.";
                }

                if (!validateMessage().equals(
                        "Message ready to send.")) {
                    return "Message not stored. Please correct the message.";
                }

                if (saveMessageToJson()) {
                    return "Message successfully stored.";
                }

                return "Unable to store message. Please check file permissions.";

            default:

                return "Invalid option.";
        }
    }

    // Create the hash using the current sent-message total
    private String createMessageHashForSentMessage() {

        String safeMessage = message.trim();
        String[] words = safeMessage.split("\\s+");

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return messageID.substring(0, 2)
                + ":"
                + totalMessages
                + ":"
                + (firstWord + lastWord).toUpperCase();
    }

    // Display message details
    public String printMessage() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    // Display all successfully sent messages
    public static String printMessages() {

        if (messages.isEmpty()) {
            return "No sent messages available.";
        }

        StringBuilder output = new StringBuilder();

        for (String msg : messages) {
            output.append(msg).append("\n\n");
        }

        return output.toString();
    }

    // Return the total number of successfully sent messages
    public static int returnTotalMessages() {

        return totalMessages;
    }

    // Store the message in a JSON file
    public void storeMessage() {

        if (saveMessageToJson()) {
            System.out.println("Message stored in JSON file.");
        } else {
            System.out.println("Error: Message could not be stored.");
        }
    }

    // Write valid JSON containing stored messages
    private boolean saveMessageToJson() {

        try {
            String existingJson = "[]";

            if (Files.exists(JSON_FILE)) {
                existingJson = new String(
                        Files.readAllBytes(JSON_FILE),
                        StandardCharsets.UTF_8
                ).trim();

                if (existingJson.isEmpty()) {
                    existingJson = "[]";
                }
            }

            if (!existingJson.startsWith("[")
                    || !existingJson.endsWith("]")) {
                return false;
            }

            String jsonMessage = "{"
                    + "\"messageID\":\"" + escapeJson(messageID) + "\","
                    + "\"messageHash\":\"" + escapeJson(messageHash) + "\","
                    + "\"recipient\":\"" + escapeJson(recipient) + "\","
                    + "\"message\":\"" + escapeJson(message) + "\""
                    + "}";

            String content = existingJson.substring(
                    1, existingJson.length() - 1
            ).trim();

            String updatedJson;

            if (content.isEmpty()) {
                updatedJson = "[" + jsonMessage + "]";
            } else {
                updatedJson = "[" + content + "," + jsonMessage + "]";
            }

            Files.write(
                    JSON_FILE,
                    updatedJson.getBytes(StandardCharsets.UTF_8)
            );

            return true;

        } catch (IOException e) {

            return false;
        }
    }

    // Escape special characters so the JSON remains valid
    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
