package com.oceanview.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class IdGenerator {

    private static String generateId(String tableName, String idColumn,
                                     String prefix, int digits) {
        String sql = "SELECT MAX(" + idColumn + ") FROM " + tableName;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {

            if (rs.next() && rs.getString(1) != null) {

                String lastId = rs.getString(1);

                String numberPart = lastId.substring(prefix.length());
                int nextNumber = Integer.parseInt(numberPart) + 1;

                return prefix + String.format("%0" + digits + "d", nextNumber);
            } else {

                return prefix + String.format("%0" + digits + "d", 1);
            }

        } catch (SQLException e) {

            return prefix + System.currentTimeMillis();
        }
    }

    // ── Public methods — one for each entity ──

    public static String generateReservationId() {
        return generateId("Reservations", "reservation_id", "R", 3);
    }

    public static String generateStaffId() {
        return generateId("Staff", "staff_id", "S", 3);
    }

    public static String generateCustomerId() {
        return generateId("Customers", "customer_id", "C", 3);
    }

    public static String generateAdminId() {
        return generateId("Admins", "admin_id", "A", 3);
    }

    public static String generateRoomId() {
        return generateId("Rooms", "room_id", "RM", 3);
    }

    public static String generateRoomTypeId() {
        return generateId("RoomTypes", "type_id", "RT", 3);
    }

    public static String generateBillId() {
        return generateId("Bills", "bill_id", "B", 3);
    }

    public static String generatePaymentId() {
        return generateId("Payments", "payment_id", "P", 3);
    }

    public static String generateHistoryId() {
        return generateId("BillHistory", "history_id", "H", 3);
    }


    public static void main(String[] args) {
        System.out.println("Testing ID Generator...");
        System.out.println("Next Reservation ID : " + generateReservationId());
        System.out.println("Next Staff ID       : " + generateStaffId());
        System.out.println("Next Customer ID    : " + generateCustomerId());
        System.out.println("Next Room ID        : " + generateRoomId());
        System.out.println("Next Bill ID        : " + generateBillId());
    }
}