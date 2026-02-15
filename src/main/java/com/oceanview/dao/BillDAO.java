package com.oceanview.dao;

import com.oceanview.exception.DataAccessException;
import com.oceanview.model.*;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BillDAO {


    public Bill findById(String billId) {
        String sql = "SELECT b.*, " +
                "res.customer_id, res.room_id, res.check_in_date, " +
                "res.check_out_date, res.num_guests, res.status AS res_status, " +
                "res.special_requests, res.created_by, res.created_at AS res_created, " +
                "c.full_name AS cust_name, c.email AS cust_email, " +
                "c.phone AS cust_phone, c.national_id, c.address, " +
                "r.room_number, r.floor, r.status AS room_status, " +
                "r.description AS room_desc, " +
                "rt.type_id, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Bills b " +
                "JOIN Reservations res ON b.reservation_id = res.reservation_id " +
                "JOIN Customers c      ON res.customer_id  = c.customer_id " +
                "JOIN Rooms r          ON res.room_id      = r.room_id " +
                "JOIN RoomTypes rt     ON r.type_id        = rt.type_id " +
                "WHERE b.bill_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, billId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToBill(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find bill by ID: " + billId, e);
        }
    }


    public Bill findByReservationId(String reservationId) {
        String sql = "SELECT b.*, " +
                "res.customer_id, res.room_id, res.check_in_date, " +
                "res.check_out_date, res.num_guests, res.status AS res_status, " +
                "res.special_requests, res.created_by, res.created_at AS res_created, " +
                "c.full_name AS cust_name, c.email AS cust_email, " +
                "c.phone AS cust_phone, c.national_id, c.address, " +
                "r.room_number, r.floor, r.status AS room_status, " +
                "r.description AS room_desc, " +
                "rt.type_id, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Bills b " +
                "JOIN Reservations res ON b.reservation_id = res.reservation_id " +
                "JOIN Customers c      ON res.customer_id  = c.customer_id " +
                "JOIN Rooms r          ON res.room_id      = r.room_id " +
                "JOIN RoomTypes rt     ON r.type_id        = rt.type_id " +
                "WHERE b.reservation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToBill(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find bill for reservation: " +
                    reservationId, e);
        }
    }


    public List<Bill> findAll() {
        String sql = "SELECT b.*, " +
                "res.customer_id, res.room_id, res.check_in_date, " +
                "res.check_out_date, res.num_guests, res.status AS res_status, " +
                "res.special_requests, res.created_by, res.created_at AS res_created, " +
                "c.full_name AS cust_name, c.email AS cust_email, " +
                "c.phone AS cust_phone, c.national_id, c.address, " +
                "r.room_number, r.floor, r.status AS room_status, " +
                "r.description AS room_desc, " +
                "rt.type_id, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Bills b " +
                "JOIN Reservations res ON b.reservation_id = res.reservation_id " +
                "JOIN Customers c      ON res.customer_id  = c.customer_id " +
                "JOIN Rooms r          ON res.room_id      = r.room_id " +
                "JOIN RoomTypes rt     ON r.type_id        = rt.type_id " +
                "ORDER BY b.generated_at DESC";

        List<Bill> bills = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                bills.add(mapRowToBill(rs));
            }
            return bills;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all bills", e);
        }
    }


    public List<Bill> findByPaymentStatus(String paymentStatus) {
        String sql = "SELECT b.*, " +
                "res.customer_id, res.room_id, res.check_in_date, " +
                "res.check_out_date, res.num_guests, res.status AS res_status, " +
                "res.special_requests, res.created_by, res.created_at AS res_created, " +
                "c.full_name AS cust_name, c.email AS cust_email, " +
                "c.phone AS cust_phone, c.national_id, c.address, " +
                "r.room_number, r.floor, r.status AS room_status, " +
                "r.description AS room_desc, " +
                "rt.type_id, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Bills b " +
                "JOIN Reservations res ON b.reservation_id = res.reservation_id " +
                "JOIN Customers c      ON res.customer_id  = c.customer_id " +
                "JOIN Rooms r          ON res.room_id      = r.room_id " +
                "JOIN RoomTypes rt     ON r.type_id        = rt.type_id " +
                "WHERE b.payment_status = ? " +
                "ORDER BY b.generated_at DESC";

        List<Bill> bills = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                bills.add(mapRowToBill(rs));
            }
            return bills;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find bills by payment status", e);
        }
    }


    public boolean insert(Bill bill) {
        String sql = "INSERT INTO Bills " +
                "(bill_id, reservation_id, num_nights, room_price, " +
                "subtotal, tax_amount, total_amount, payment_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, bill.getBillId());
            ps.setString(2, bill.getReservation().getReservationId());
            ps.setInt(3, bill.getNumNights());
            ps.setDouble(4, bill.getRoomPrice());
            ps.setDouble(5, bill.getSubtotal());
            ps.setDouble(6, bill.getTaxAmount());
            ps.setDouble(7, bill.getTotalAmount());
            ps.setString(8, bill.getPaymentStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert bill: " + bill.getBillId(), e);
        }
    }


    public boolean updatePaymentStatus(String billId, String paymentStatus) {
        String sql = "UPDATE Bills SET payment_status=? WHERE bill_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ps.setString(2, billId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update payment status: " + billId, e);
        }
    }


    public boolean delete(String billId) {
        String sql = "DELETE FROM Bills WHERE bill_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, billId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete bill: " + billId, e);
        }
    }


    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM Bills WHERE payment_status = 'Paid'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getDouble(1);
            return 0.0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to calculate total revenue", e);
        }
    }


    public int countByPaymentStatus(String paymentStatus) {
        String sql = "SELECT COUNT(*) FROM Bills WHERE payment_status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentStatus);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to count bills by status", e);
        }
    }


    private Bill mapRowToBill(ResultSet rs) throws SQLException {

        // Map RoomType
        RoomType roomType = new RoomType();
        roomType.setTypeId(rs.getString("type_id"));
        roomType.setTypeName(rs.getString("type_name"));
        roomType.setPricePerNight(rs.getDouble("price_per_night"));
        roomType.setDescription(rs.getString("type_desc"));
        roomType.setMaxOccupancy(rs.getInt("max_occupancy"));

        // Map Room
        Room room = new Room();
        room.setRoomId(rs.getString("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(roomType);
        room.setFloor(rs.getInt("floor"));
        room.setStatus(rs.getString("room_status"));
        room.setDescription(rs.getString("room_desc"));

        // Map Customer
        Customer customer = new Customer();
        customer.setId(rs.getString("customer_id"));
        customer.setFullName(rs.getString("cust_name"));
        customer.setEmail(rs.getString("cust_email"));
        customer.setPhone(rs.getString("cust_phone"));
        customer.setNationalId(rs.getString("national_id"));
        customer.setAddress(rs.getString("address"));

        // Map Reservation
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getString("reservation_id"));
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckInDate(rs.getDate("check_in_date"));
        reservation.setCheckOutDate(rs.getDate("check_out_date"));
        reservation.setNumGuests(rs.getInt("num_guests"));
        reservation.setStatus(rs.getString("res_status"));
        reservation.setSpecialRequests(rs.getString("special_requests"));
        reservation.setCreatedBy(rs.getString("created_by"));
        reservation.setCreatedAt(rs.getTimestamp("res_created"));

        // Map Bill
        Bill bill = new Bill();
        bill.setBillId(rs.getString("bill_id"));
        bill.setReservation(reservation);
        bill.setNumNights(rs.getInt("num_nights"));
        bill.setRoomPrice(rs.getDouble("room_price"));
        bill.setSubtotal(rs.getDouble("subtotal"));
        bill.setTaxAmount(rs.getDouble("tax_amount"));
        bill.setTotalAmount(rs.getDouble("total_amount"));
        bill.setPaymentStatus(rs.getString("payment_status"));
        bill.setGeneratedAt(rs.getTimestamp("generated_at"));

        return bill;
    }
}