package com.oceanview.util;

import java.util.Date;


public class ValidationUtil {


    private static final String EMAIL_PATTERN   = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_PATTERN   = "^[0-9]{10}$";        // 10 digit Sri Lankan number
    private static final String NIC_PATTERN     = "^([0-9]{9}[VvXx]|[0-9]{12})$"; // old or new NIC
    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{4,20}$"; // 4-20 chars, no spaces


    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }


    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) return false;
        return email.trim().matches(EMAIL_PATTERN);
    }


    public static boolean isValidPhone(String phone) {
        if (!isNotEmpty(phone)) return false;
        // Remove spaces and dashes before checking
        String cleaned = phone.trim().replaceAll("[\\s-]", "");
        return cleaned.matches(PHONE_PATTERN);
    }


    public static boolean isValidNIC(String nic) {
        if (!isNotEmpty(nic)) return false;
        return nic.trim().matches(NIC_PATTERN);
    }


    public static boolean isValidUsername(String username) {
        if (!isNotEmpty(username)) return false;
        return username.trim().matches(USERNAME_PATTERN);
    }


    public static boolean isValidPassword(String password) {
        return isNotEmpty(password) && password.length() >= 6;
    }


    public static boolean isValidDateRange(Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null) return false;
        return checkOut.after(checkIn);
    }


    public static boolean isPositive(double value) {
        return value > 0;
    }


    public static boolean isPositive(int value) {
        return value > 0;
    }


    public static boolean isValidRoomNumber(String roomNumber) {
        if (!isNotEmpty(roomNumber)) return false;
        return roomNumber.trim().length() <= 10;
    }


    public static boolean isValidPrice(double price) {
        return price > 0 && price < 1000000;
    }


    public static void main(String[] args) {
        System.out.println("Testing ValidationUtil...");
        System.out.println();


        System.out.println("-- Email --");
        System.out.println("john@gmail.com  : " + isValidEmail("john@gmail.com"));
        System.out.println("johnATgmail     : " + isValidEmail("johnATgmail"));


        System.out.println("\n-- Phone --");
        System.out.println("0771234567      : " + isValidPhone("0771234567"));
        System.out.println("077-123         : " + isValidPhone("077-123"));


        System.out.println("\n-- NIC --");
        System.out.println("123456789V      : " + isValidNIC("123456789V"));
        System.out.println("200012345678    : " + isValidNIC("200012345678"));
        System.out.println("ABCDEF          : " + isValidNIC("ABCDEF"));


        System.out.println("\n-- Username --");
        System.out.println("staff_1         : " + isValidUsername("staff_1"));
        System.out.println("ab              : " + isValidUsername("ab"));


        System.out.println("\n-- Password --");
        System.out.println("admin123        : " + isValidPassword("admin123"));
        System.out.println("abc             : " + isValidPassword("abc"));
    }
}