package com.oceanview.util;

import org.mindrot.jbcrypt.BCrypt;


public class PasswordUtil {


    private static final int SALT_ROUNDS = 10;


    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(SALT_ROUNDS));
    }


    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) return false;
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }


    public static void main(String[] args) {


        String adminHash = hashPassword("admin123");
        String staffHash = hashPassword("staff123");

        System.out.println("admin123 hash: " + adminHash);
        System.out.println("staff123 hash: " + staffHash);


        System.out.println("\nVerifying admin123: " + verifyPassword("admin123", adminHash));
        System.out.println("Verifying wrongpwd: " + verifyPassword("wrongpwd", adminHash));


    }
}