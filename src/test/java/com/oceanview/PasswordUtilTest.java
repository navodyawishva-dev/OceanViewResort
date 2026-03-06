package com.oceanview;

import com.oceanview.util.PasswordUtil;
import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordUtilTest {

    @Test
    public void testPasswordHashing() {
        String password = "Test1234";
        String hashed = PasswordUtil.hashPassword(password);
        assertNotNull(hashed);
        assertNotEquals(password, hashed);
    }

    @Test
    public void testPasswordVerification() {
        String password = "Test1234";
        String hashed = PasswordUtil.hashPassword(password);
        assertTrue(PasswordUtil.verifyPassword(password, hashed));
    }

    @Test
    public void testWrongPasswordFails() {
        String password = "Test1234";
        String hashed = PasswordUtil.hashPassword(password);
        assertFalse(PasswordUtil.verifyPassword("WrongPass", hashed));
    }
}