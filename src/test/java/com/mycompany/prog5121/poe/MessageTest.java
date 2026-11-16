
package com.mycompany.prog5121.poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    // Test 1: Message length valid
    @Test
    public void testMessageLengthSuccess() {

        Message msg = new Message(
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals("Message ready to send.", msg.validateMessage());
    }

    // Test 2: Message length exceeds 250 characters
    @Test
    public void testMessageLengthFailure() {

        String longMessage = "A".repeat(260);

        Message msg = new Message("+27718693002", longMessage);

        assertEquals(
                "Message exceeds 250 characters by 10 characters.",
                msg.validateMessage()
        );
    }

    // Test 3: Recipient number correctly formatted
    @Test
    public void testRecipientNumberSuccess() {

        Message msg = new Message("+27718693002", "Hello");

        assertEquals(
                "Cell phone number successfully captured.",
                msg.checkRecipientCell()
        );
    }

    // Test 4: Recipient number incorrectly formatted
    @Test
    public void testRecipientNumberFailure() {

        Message msg = new Message("08575975889", "Hello");

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain"
                        + " an international code.",
                msg.checkRecipientCell()
        );
    }

    // Test 5: Message ID generation
    @Test
    public void testMessageIDCreated() {

        Message msg = new Message("+27718693002", "Hello");

        assertTrue(msg.checkMessageID());
        assertNotNull(msg.getMessageID());
    }

    // Test 6: Send Message option
    @Test
    public void testSendMessageOption() {

        Message msg = new Message("+27718693002", "Hello");

        assertEquals(
                "Message successfully sent.",
                msg.sendMessage(1)
        );
    }

    // Test 7: Disregard Message option
    @Test
    public void testDisregardMessageOption() {

        Message msg = new Message("+27718693002", "Hello");

        assertEquals(
                "Message disregarded.",
                msg.sendMessage(2)
        );
    }

    // Test 8: Store Message option
    @Test
    public void testStoreMessageOption() {

        Message msg = new Message("+27718693002", "Hello");

        assertEquals(
                "Message stored successfully.",
                msg.sendMessage(3)
        );
    }

    // Test 9: Message Hash generated
    @Test
    public void testMessageHashCreated() {

        Message msg = new Message(
                "+27718693002",
                "Hi Mike can you join us tonight"
        );

        assertNotNull(msg.createMessageHash());
        assertFalse(msg.getMessageHash().isEmpty());
    }

    // Test 10: Total Messages Sent
    @Test
    public void testTotalMessagesSent() {

        Message msg = new Message("+27718693002", "Hello");

        int previousTotal = Message.returnTotalMessages();

        msg.sendMessage(1);

        assertEquals(
                previousTotal + 1,
                Message.returnTotalMessages()
        );
    }

    // Helper method for creating stored test messages
    private Message createStoredTestMessage(
            String recipient, String messageText) {

        Message msg = new Message(recipient, messageText);

        assertEquals(
                "Message stored successfully.",
                msg.sendMessage(3)
        );

        return msg;
    }

    // Test 11: Check contents of sent messages array
    @Test
    public void testSentMessagesArray() {

        String testMessage = "Sent array test message 12345";

        Message msg = new Message(
                "+27718693002",
                testMessage
        );

        msg.sendMessage(1);

        assertTrue(
                Message.getSentMessages().contains(testMessage)
        );
    }

    // Test 12: Find the longest stored message
    @Test
    public void testLongestStoredMessage() {

        createStoredTestMessage(
                "+27718693002",
                "This is a test of the longest stored message."
        );

        String expectedLongest = "";
        
        for (Message msg : Message.getStoredMessages()) {

            if (msg.getMessage().length() > expectedLongest.length()) {
                expectedLongest = msg.getMessage();
            }
        }

        assertTrue(
                Message.displayLongestMessage()
                       .contains(expectedLongest)
        );
    }

    // Test 13: Search for a message using its ID
    @Test
    public void testSearchMessageByID() {

        Message msg = createStoredTestMessage(
                "+27718693002",
                "Search by ID test message."
        );

        String result = Message.searchMessageByID(
                msg.getMessageID()
        );

        assertTrue(result.contains("+27718693002"));
        assertTrue(result.contains("Search by ID test message."));
    }

    // Test 14: Search for messages using recipient number
    @Test
    public void testSearchMessagesByRecipient() {

        String testMessage = "Recipient search test 67890";

        createStoredTestMessage(
                "+27718693002",
                testMessage
        );

        String result = Message.searchMessagesByRecipient(
                "+27718693002"
        );

        assertTrue(result.contains(testMessage));
    }

    // Test 15: Delete a stored message using its hash
    @Test
    public void testDeleteMessageByHash() {

        Message msg = createStoredTestMessage(
                "+27718693002",
                "Message to delete using hash 98765."
        );

        String hash = msg.getMessageHash();

        assertEquals(
                "Message successfully deleted.",
                Message.deleteMessageByHash(hash)
        );

        assertEquals(
                "Message ID not found.",
                Message.searchMessageByID(msg.getMessageID())
        );
    }

    // Test 16: Display a full report of stored messages
    @Test
    public void testDisplayReport() {

        String testMessage = "Full report test message 54321";

        createStoredTestMessage(
                "+27718693002",
                testMessage
        );

        String report = Message.displayReport();

        assertTrue(report.contains("STORED MESSAGES REPORT"));
        assertTrue(report.contains(testMessage));
        assertTrue(report.contains("Message ID:"));
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
    }
}
