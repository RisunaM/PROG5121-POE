
package com.mycompany.prog5121.poe;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Message {

    private String messageID;
    private String recipient;
    private String message;
    private String messageHash;
    private int messageNumber;
    private String sender;
    private String status = "";

    private static int totalMessages = 0;

    // Array 1: Sent messages
    private static ArrayList<String> sentMessages =
            new ArrayList<>();

    // Array 2: Disregarded messages
    private static ArrayList<String> disregardedMessages =
            new ArrayList<>();

    // Array 3: Stored messages
    private static ArrayList<Message> storedMessages =
            new ArrayList<>();

    // Array 4: Message hashes
    private static ArrayList<String> messageHashes =
            new ArrayList<>();

    // Array 5: Message IDs
    private static ArrayList<String> messageIDs =
            new ArrayList<>();

    // Constructor
    public Message(String recipient, String message) {
        this("Developer", recipient, message);
    }

    // Overloaded constructor
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

    // Constructor used when loading messages from JSON
    private Message(String sender,
                    String recipient,
                    String message,
                    String messageID,
                    String messageHash,
                    String status) {

        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.messageID = messageID;
        this.messageHash = messageHash;
        this.status = status;
    }

    // Generate Message ID
    private String generateMessageID() {

        Random random = new Random();

        long id = 1000000000L
                + (long) (random.nextDouble() * 9000000000L);

        return String.valueOf(id);
    }

    // Validate Message ID
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Validate Recipient
    public String checkRecipientCell() {

        if (recipient != null &&
                recipient.matches("^\\+27\\d{9}$")) {

            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code.";
    }

    // Validate Message Length
    public String validateMessage() {

        if (message != null && message.length() <= 250) {
            return "Message ready to send.";
        }

        int excess = message.length() - 250;

        return "Message exceeds 250 characters by "
                + excess + " characters.";
    }

    // Create Message Hash
    public String createMessageHash() {

        String cleanMessage =
                message.replaceAll("[^a-zA-Z0-9 ]", "");

        if (cleanMessage.trim().isEmpty()) {

            return messageID.substring(0, 2)
                    + ":" + messageNumber + ":EMPTY";
        }

        String[] words =
                cleanMessage.trim().split("\\s+");

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return messageID.substring(0, 2)
                + ":" + messageNumber + ":"
                + (firstWord + lastWord).toUpperCase();
    }

    // Send Message Options
    public String sendMessage(int option) {

        switch (option) {

            case 1:

                status = "Sent";
                sentMessages.add(message);
                totalMessages++;

                return "Message successfully sent.";

            case 2:

                status = "Disregarded";
                disregardedMessages.add(message);

                return "Message disregarded.";

            case 3:

                status = "Stored";
                storedMessages.add(this);

                storeMessage();

                return "Message stored successfully.";

            default:

                return "Invalid option.";
        }
    }

    // Store Message in JSON File
    @SuppressWarnings("unchecked")
    public void storeMessage() {

        File file = new File("messages.json");

        try {

            JSONArray jsonArray = readStoredJSON(file);

            JSONObject obj = new JSONObject();

            obj.put("MessageID", messageID);
            obj.put("MessageHash", messageHash);
            obj.put("Sender", sender);
            obj.put("Recipient", recipient);
            obj.put("Message", message);
            obj.put("Status", status);

            jsonArray.add(obj);

            try (FileWriter writer = new FileWriter(file)) {

                writer.write(jsonArray.toJSONString());
            }

        } catch (Exception e) {

            System.out.println(
                    "Error storing message: " + e.getMessage());
        }
    }

    // Read Existing JSON Data
    private static JSONArray readStoredJSON(File file)
            throws Exception {

        JSONArray jsonArray = new JSONArray();

        if (!file.exists() || file.length() == 0) {
            return jsonArray;
        }

        String content = new String(
                Files.readAllBytes(file.toPath()),
                StandardCharsets.UTF_8
        );

        JSONParser parser = new JSONParser();

        try {

            Object result = parser.parse(content);

            if (result instanceof JSONArray) {
                return (JSONArray) result;
            }

            if (result instanceof JSONObject) {
                jsonArray.add(result);
                return jsonArray;
            }

        } catch (Exception e) {

            // Try reading newline-separated JSON objects
            String[] lines = content.split("\\r?\\n");

            for (String line : lines) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {

                    Object result = parser.parse(line);

                    if (result instanceof JSONObject) {
                        jsonArray.add(result);
                    }

                } catch (Exception lineError) {
                    // Ignore invalid individual lines
                }
            }
        }

        return jsonArray;
    }

    // Load Stored Messages
    public static void loadStoredMessages() {

        storedMessages.clear();
        messageIDs.clear();
        messageHashes.clear();

        File file = new File("messages.json");

        if (!file.exists()) {
            return;
        }

        try {

            JSONArray jsonArray = readStoredJSON(file);

            for (Object object : jsonArray) {

                if (!(object instanceof JSONObject)) {
                    continue;
                }

                JSONObject obj = (JSONObject) object;

                String sender = (String) obj.get("Sender");
                String recipient = (String) obj.get("Recipient");
                String message = (String) obj.get("Message");
                String id = (String) obj.get("MessageID");
                String hash = (String) obj.get("MessageHash");
                String status = (String) obj.get("Status");

                if (sender == null) {
                    sender = "Developer";
                }

                if (recipient == null ||
                        message == null ||
                        id == null ||
                        hash == null) {
                    continue;
                }

                if (status == null) {
                    status = "Stored";
                }

                Message storedMessage = new Message(
                        sender,
                        recipient,
                        message,
                        id,
                        hash,
                        status
                );

                storedMessages.add(storedMessage);
                messageIDs.add(id);
                messageHashes.add(hash);
            }

        } catch (Exception e) {

            System.out.println(
                    "Error loading messages: " + e.getMessage());
        }
    }

    // Search By Message ID
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

    // Search By Recipient
    public static String searchMessagesByRecipient(
            String recipientNumber) {

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

        return output.length() == 0
                ? "No messages found."
                : output.toString();
    }

    // Delete Message By Hash
    public static String deleteMessageByHash(String hash) {

        loadStoredMessages();

        boolean removed = storedMessages.removeIf(
                msg -> msg.messageHash.equals(hash)
        );

        if (!removed) {
            return "Message hash not found.";
        }

        rewriteJSONFile();

        return "Message successfully deleted.";
    }

    // Rewrite JSON File After Deletion
    @SuppressWarnings("unchecked")
    private static void rewriteJSONFile() {

        JSONArray jsonArray = new JSONArray();

        messageIDs.clear();
        messageHashes.clear();

        for (Message msg : storedMessages) {

            JSONObject obj = new JSONObject();

            obj.put("MessageID", msg.messageID);
            obj.put("MessageHash", msg.messageHash);
            obj.put("Sender", msg.sender);
            obj.put("Recipient", msg.recipient);
            obj.put("Message", msg.message);
            obj.put("Status", msg.status);

            jsonArray.add(obj);

            messageIDs.add(msg.messageID);
            messageHashes.add(msg.messageHash);
        }

        try (FileWriter writer = new FileWriter("messages.json")) {

            writer.write(jsonArray.toJSONString());

        } catch (IOException e) {

            System.out.println(
                    "Error updating file: " + e.getMessage());
        }
    }

    // Display Longest Message
    public static String displayLongestMessage() {

        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            return "No stored messages.";
        }

        Message longest = storedMessages.get(0);

        for (Message msg : storedMessages) {

            if (msg.message.length() > longest.message.length()) {
                longest = msg;
            }
        }

        return longest.message;
    }

    // Display Stored Messages Report
    public static String displayReport() {

        loadStoredMessages();

        if (storedMessages.isEmpty()) {
            return "No stored messages available.";
        }

        StringBuilder report = new StringBuilder();

        report.append(
                "========== STORED MESSAGES REPORT ==========\n\n");

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

            report.append("----------------------------------\n");
        }

        return report.toString();
    }

    // Display Stored Senders And Recipients
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

    // Display Message Hashes
    public static String displayMessageHashes() {

        loadStoredMessages();

        if (messageHashes.isEmpty()) {
            return "No message hashes available.";
        }

        StringBuilder output = new StringBuilder();

        for (String hash : messageHashes) {

            output.append(hash).append("\n");
        }

        return output.toString();
    }

    // Print Message Details
    public String printMessage() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nSender: " + sender
                + "\nRecipient: " + recipient
                + "\nMessage: " + message;
    }

    // Print Sent Messages
    public static String printMessages() {

        if (sentMessages.isEmpty()) {
            return "No sent messages.";
        }

        StringBuilder output = new StringBuilder();

        for (String msg : sentMessages) {

            output.append(msg).append("\n\n");
        }

        return output.toString();
    }

    // Return Total Messages
    public static int returnTotalMessages() {
        return totalMessages;
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public static ArrayList<String> getSentMessages() {
        return sentMessages;
    }

    public static ArrayList<String> getDisregardedMessages() {
        return disregardedMessages;
    }

    public static ArrayList<Message> getStoredMessages() {
        return storedMessages;
    }

    public static ArrayList<String> getMessageHashes() {
        return messageHashes;
    }

    public static ArrayList<String> getMessageIDs() {
        return messageIDs;
    }
}
