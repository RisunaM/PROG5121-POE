/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121.poe;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author risun
 */
public class MessageTest {
    
    // Test 1: Message length valid
    @Test
    public void testMessageLengthSuccess() {

        Message msg = new Message(
                "+27718693002",
                "Hi Mike, can you join us for dinner tonight?"
        );

        assertEquals(
                "Message ready to send.",
                msg.validateMessage()
        );
    }

    // Test 2: Message length exceeds 250 characters
    @Test
    public void testMessageLengthFailure() {

        String longMessage = "A".repeat(260);

        Message msg = new Message(
                "+27718693002",
                longMessage
        );

        assertEquals(
                "Message exceeds 250 characters by 10, please reduce the size.",
                msg.validateMessage()
        );
    }

    // Test 3: Recipient number correctly formatted
    @Test
    public void testRecipientNumberSuccess() {

        Message msg = new Message(
                "+27718693002",
                "Hello"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                msg.checkRecipientCell()
        );
    }

    // Test 4: Recipient number incorrectly formatted
    @Test
    public void testRecipientNumberFailure() {

        Message msg = new Message(
                "08575975889",
                "Hello"
        );

        assertEquals(
                "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                msg.checkRecipientCell()
        );
    }

    // Test 5: Message ID generation
    @Test
    public void testMessageIDCreated() {

        Message msg = new Message(
                "+27718693002",
                "Hello"
        );

        assertTrue(msg.checkMessageID());
    }

    // Test 6: Send Message option
    @Test
    public void testSendMessageOption() {

        Message msg = new Message(
                "+27718693002",
                "Hello"
        );

        assertEquals(
                "Message successfully sent.",
                msg.sendMessage(1)
        );
    }

    // Test 7: Disregard Message option
    @Test
    public void testDisregardMessageOption() {

        Message msg = new Message(
                "+27718693002",
                "Hello"
        );

        assertEquals(
                "Press 0 to delete the message.",
                msg.sendMessage(2)
        );
    }

    // Test 8: Store Message option
    @Test
    public void testStoreMessageOption() {

        Message msg = new Message(
                "+27718693002",
                "Hello"
        );

        assertEquals(
                "Message successfully stored.",
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
    }

    // Test 10: Total Messages Sent
    @Test
    public void testTotalMessagesSent() {

        Message msg1 = new Message(
                "+27718693002",
                "Hello"
        );

        msg1.sendMessage(1);

        assertTrue(
                Message.returnTotalMessages() > 0
        );
    }
}
