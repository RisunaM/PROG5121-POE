package com.mycompany.prog5121.poe;

import java.io.*;
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

    // Array 1: All sent messages
    private static ArrayList<String> sentMessages =
            new ArrayList<>();
    
    //Array 2: All disregarded messages
    private static ArrayList<String> disregardedMessages =
            new ArrayList<>();
    
    //Array 3: All stored messages
    private static ArrayList<Message> storedMessages =
            new ArrayList<>();
    
    //Array 4: All messages hashes
    private static ArrayList<String> messageHashes =
            new ArrayList<>();
    
    // Array 5: All message IDs
    private static ArrayList<String> messageIDs = 
            new ArrayList<>();

    /**
     * Constructor
     */
    public Message(String recipient,
                   String message) {
        this ("Developer", recipient, message);
    }
    
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

        int excess = message.length() - 250;

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

        String firstWord = words.length>0 ? words[0]:"";

        String lastWord =
                words.length>0 ? words[words.length - 1]: "";

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

                storedMessages.add(this);
                
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
        obj.put("Sender", sender);
        obj.put("Recipient", recipient);
        obj.put("Message", message);
        obj.put("Status", status);

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
                    "Error storing message." + e.getMessage()
            );
        }
    }
    /** 
     * Reads stored messages from the JSON file.
     */        
    public stastic void loadStoredMessages() throws FileNotFoundException {
        
        storedMessages.clear();
        
        JSONParser parser = new JSONParser();
        
        File file = new File("message.json");
        
        if (!file.exists()) {
            return;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            
            while ((line = reader.readLine()) !=null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                JSONObject obj = (JSONObject) parser.parse(line);
                
                String sender = (String) obj.get("Sender");
                String recipient = (String) obj.get("Recipient");
                String message = (String)obj.get("Message");
                
                Message storedMessage =
                        new Message(sender, recipient, message);
                
                storedMessage.messageID =
                        (String)obj.get("Message");
                
                storedMessage.messageHash =
                        (String)obj.get("MessageHash");
                
                storedMessage.status = "Stored";
                
                storedMessages.add(storedMessage);     
            }
        } catch (Exception e) {
            
            System.out.println("Error loading stored messages:" + e.getMessage()
            );
        }
    }
    /**
     * Display the sender and recipient of stored messages.
     */
    public static String displayStoredSendersAndRecipients() {
        
        loadStoredMessages();
        
        StringBuilder output = new StringBuilder();
        
        for (Message msg : storedMessages) {
            output.append("Sender:")
                   .append(msg.sender)
                    .append("\nRecipient: ")
                    .append("\n\n");
        }
        
        if (storedMessages.isEmpty()) {
            return "No stored messages found.";
        }
        
        return output.toString();
    }
    
    /**
     * Finds the longest stored message.
     */
    public static String displayLongestMesssage() {
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
        loadStoredMessage();
        
        for (Message msg : storedMessages) {
            if(msg.messageID.equals(id)) {
                return "Recipient: " + msg.recipient
                        + "\nMessage: " + msg.message;
            }
        }
        
        return "Message ID not found.";
    }
    /**
     * Searches for all stored messages belonging to a recipient.
     */
    public static String searchMessagesByReciipient(String recipientNumber) {
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
        if (output.length() == 0){
            return "No messages found for this recipient.";
        }
        
        return output.toString();
    }   
    
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
    
    rewriteJSONFile();
    
    return "Message successfully deleted.";
    }
/**
 * Rewrites the JSON file after deleting a message.
 */
private static void rewriteJSONFile() {
    try (FileWriter file = new FileWriter("message.json", false)) {
      for (Message msg : storedMessages) {
          JSONObject obj = new JSONObject();
          
          obj.put("MessageID", msg.messageID);
          obj.put("MessageHash", msg.messageHash);
          obj.put("Sender", msg.sender);
          obj.put("Recipient", msg.recipient);
          obj.put(Message, msg.message);
          obj.put("Status", "Stored" );
          
          file.write(obj.toJSONString());
          file.write(System.lineSeparator());
          
      }  
    } catch (IOException e) {
        
        System.out.println(
        "Error updating JSON file: " + e.getMessage()
        );
    }
}
/**
 * Displays a complete report of all stored messages.
 */
public static String displayReport() {
    loadStoredMessages();
    
    if (storedMessages.isEmpty()) {
        return "No stored messages available."
    }
    
    StringBuilder report = new StringBuilder();
    report.append("========== STORED MESSAGES REPORT ==========\n\n");
    
    for(Message msg : storedMessages) {
        report.append("Message ID: ")
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
        
        report.append("-----------------------------------\n");
    }
    return report.toString();
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
    
    /**
     * Return all sent messages.
     */
    public static ArrayList<String> getSentMessages() {
        return sentMessages;
    }
    
     /**
     * Return all disregarded messages.
     */
    public static ArrayList<String> getDisregardedMessages() {
        return disregardedMessages;
    }
    
    
    /**
     * Returns all stored messages.
     */
    public static ArrayList<Message> getStoredMessages() {
        return storedMessages;
    }

    /**
     * Returns all message hashes.
     */
    public static ArrayList<Message> getMessageHashes() {
        return messageHashes;
    }
    
     /**
     * Returns all message IDs.
     */
    public static ArrayList<Message> getMessageIDs() {
        return messageIDs;
    }   
}