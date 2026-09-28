package com.mycompany.prog5121.poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Login class.
 */
public class LoginTest {

    /**
     * Tests a valid username.
     */
    @Test
    public void testValidUserName() {

        Login instance = new Login();

        String username = "kyl_1";

        boolean result = instance.checkUserName(username);

        assertTrue(result);
    }

    /**
     * Tests an invalid username.
     */
    @Test
    public void testInvalidUserName() {

        Login instance = new Login();

        String username = "kyle!!!!!!!";

        boolean result = instance.checkUserName(username);

        assertFalse(result);
    }

    /**
     * Tests a valid password.
     */
    @Test
    public void testValidPassword() {

        Login instance = new Login();

        String password = "Ch&&sec@ke99!";

        boolean result = instance.checkPasswordComplexity(password);

        assertTrue(result);
    }

    /**
     * Tests an invalid password.
     */
    @Test
    public void testInvalidPassword() {

        Login instance = new Login();

        String password = "password";

        boolean result = instance.checkPasswordComplexity(password);

        assertFalse(result);
    }

    /**
     * Tests a valid South African cell phone number.
     */
    @Test
    public void testValidCellPhoneNumber() {

        Login instance = new Login();

        String cellPhone = "+27838968976";

        boolean result = instance.checkCellPhoneNumber(cellPhone);

        assertTrue(result);
    }

    /**
     * Tests an invalid cell phone number.
     */
    @Test
    public void testInvalidCellPhoneNumber() {

        Login instance = new Login();

        String cellPhone = "08966553";

        boolean result = instance.checkCellPhoneNumber(cellPhone);

        assertFalse(result);
    }

    /**
     * Tests successful registration.
     */
    @Test
    public void testSuccessfulRegistration() {

        Login instance = new Login();

        String result = instance.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Risuna",
                "Mashele"
        );

        assertEquals(
                "User successfully registered.",
                result
        );
    }

    /**
     * Tests registration with an invalid username.
     */
    @Test
    public void testRegistrationInvalidUsername() {

        Login instance = new Login();

        String result = instance.registerUser(
                "kyle!!!!!!!",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Risuna",
                "Mashele"
        );

        assertEquals(
                "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
                result
        );
    }

    /**
     * Tests registration with an invalid password.
     */
    @Test
    public void testRegistrationInvalidPassword() {

        Login instance = new Login();

        String result = instance.registerUser(
                "kyl_1",
                "password",
                "+27838968976",
                "Risuna",
                "Mashele"
        );

        assertEquals(
                "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
                result
        );
    }

    /**
     * Tests registration with an invalid cell phone number.
     */
    @Test
    public void testRegistrationInvalidCellPhone() {

        Login instance = new Login();

        String result = instance.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "08966553",
                "Risuna",
                "Mashele"
        );

        assertEquals(
                "Cell phone number incorrectly formatted or does not contain international code.",
                result
        );
    }

    /**
     * Tests successful login.
     */
    @Test
    public void testSuccessfulLogin() {

        Login instance = new Login();

        instance.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Risuna",
                "Mashele"
        );

        boolean result = instance.loginUser(
                "kyl_1",
                "Ch&&sec@ke99!"
        );

        assertTrue(result);
    }

    /**
     * Tests unsuccessful login.
     */
    @Test
    public void testUnsuccessfulLogin() {

        Login instance = new Login();

        instance.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Risuna",
                "Mashele"
        );

        boolean result = instance.loginUser(
                "wrong",
                "wrong"
        );

        assertFalse(result);
    }

    /**
     * Tests successful login status message.
     */
    @Test
    public void testSuccessfulLoginStatus() {

        Login instance = new Login();

        instance.registerUser(
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838968976",
                "Risuna",
                "Mashele"
        );

        String result = instance.returnLoginStatus(true);

        assertEquals(
                "Welcome Risuna Mashele, it is great to see you again.",
                result
        );
    }

    /**
     * Tests unsuccessful login status message.
     */
    @Test
    public void testUnsuccessfulLoginStatus() {

        Login instance = new Login();

        String result = instance.returnLoginStatus(false);

        assertEquals(
                "Username or password incorrect, please try again.",
                result
        );
    }
}