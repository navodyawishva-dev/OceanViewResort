package com.oceanview.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class DateUtil {


    private static final String DATE_FORMAT         = "yyyy-MM-dd";
    private static final String DISPLAY_FORMAT      = "dd MMM yyyy";   // e.g. 15 Jan 2024
    private static final String DATETIME_FORMAT     = "dd MMM yyyy HH:mm"; // e.g. 15 Jan 2024 14:30

    private static final SimpleDateFormat sdf         = new SimpleDateFormat(DATE_FORMAT);
    private static final SimpleDateFormat displaySdf  = new SimpleDateFormat(DISPLAY_FORMAT);
    private static final SimpleDateFormat datetimeSdf = new SimpleDateFormat(DATETIME_FORMAT);


    public static Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            return sdf.parse(dateStr.trim());
        } catch (ParseException e) {
            System.out.println("DateUtil: Could not parse date: " + dateStr);
            return null;
        }
    }


    public static String formatDate(Date date) {
        if (date == null) return "";
        return displaySdf.format(date);
    }


    public static String formatDateForInput(Date date) {
        if (date == null) return "";
        return sdf.format(date);
    }


    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return datetimeSdf.format(date);
    }


    public static java.sql.Date toSqlDate(Date date) {
        if (date == null) return null;
        return new java.sql.Date(date.getTime());
    }


    public static Date fromSqlDate(java.sql.Date sqlDate) {
        if (sqlDate == null) return null;
        return new Date(sqlDate.getTime());
    }


    public static int calculateNights(Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null) return 0;
        long diff = checkOut.getTime() - checkIn.getTime();
        return (int) TimeUnit.MILLISECONDS.toDays(diff);
    }


    public static boolean isValidDateRange(Date checkIn, Date checkOut) {
        if (checkIn == null || checkOut == null) return false;
        return checkOut.after(checkIn);
    }


    public static boolean isTodayOrFuture(Date date) {
        if (date == null) return false;
        Date today = new Date();
        // Reset time to midnight for fair comparison
        String todayStr = sdf.format(today);
        String dateStr  = sdf.format(date);
        return dateStr.compareTo(todayStr) >= 0;
    }


    public static String getTodayString() {
        return sdf.format(new Date());
    }


    public static void main(String[] args) {
        System.out.println("Testing DateUtil...");


        Date checkIn  = parseDate("2024-03-10");
        Date checkOut = parseDate("2024-03-13");

        System.out.println("Parsed check-in  : " + checkIn);
        System.out.println("Formatted display: " + formatDate(checkIn));
        System.out.println("Formatted input  : " + formatDateForInput(checkIn));
        System.out.println("Nights stayed    : " + calculateNights(checkIn, checkOut));
        System.out.println("Valid date range : " + isValidDateRange(checkIn, checkOut));
        System.out.println("Today's date     : " + getTodayString());
        System.out.println("Is future date   : " + isTodayOrFuture(checkOut));
    }
}
