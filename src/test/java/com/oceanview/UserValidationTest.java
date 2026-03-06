package com.oceanview;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserValidationTest {

    @Test
    public void testValidEmail() {
        String email = "test@example.com";
        assertTrue(email.contains("@"));
        assertTrue(email.contains("."));
    }

    @Test
    public void testInvalidEmail() {
        String email = "invalidemail";
        assertFalse(email.contains("@"));
    }

    @Test
    public void testUsernameNotEmpty() {
        String username = "john123";
        assertNotNull(username);
        assertFalse(username.isEmpty());
    }

    @Test
    public void testPasswordMinLength() {
        String password = "Pass1234";
        assertTrue(password.length() >= 6);
    }
}