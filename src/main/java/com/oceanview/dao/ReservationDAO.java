package com.oceanview.dao;

import com.oceanview.exception.DataAccessException;
import com.oceanview.model.*;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ReservationDAO {


    public Reservation findById(String reservationId) {
        String sql = "SELECT res.*, " +
                "c.full_name AS cust_name, c.email AS cust_email, " +
                "c.phone AS cust_phone, c.national_id, c.address, " +
                "r.room_number, r.floor, r.status AS room_status, " +
                "r.description AS room_desc, " +
                "rt.type_id, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Reservations res " +
                "JOIN Customers c  ON res.customer_id = c.customer_id " +
                "JOIN Rooms r      ON res.room_id = r.room_id " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE res.reservation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservationId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToReservation(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find reservation: " + reservationId, e);
        }
    }


    public List<Reservation> findAll() {
        String sql = "SELECT res.*, " +
                "c.full_name AS cust_name, c.email AS cust_email, " +
                "c.phone AS cust_phone, c.national_id, c.address, " +
                "r.room_number, r.floor, r.status AS room_status, " +
                "r.description AS room_desc, " +
                "rt.type_id, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Reservations res " +
                "JOIN Customers c  ON res.customer_id = c.customer_id " +
                "JOIN Rooms r      ON res.room_id = r.room_id " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "ORDER BY res.created_at DESC";

        List<Reservation> reservations = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
            return reservations;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all reservations", e);
        }
    }


    public List<Reservation> findByCustomerId(String customerId) {
        String sql = "SELECT res.*, " +
                "c.full_name AS cust_name, c.email AS cust_email, " +
                "c.phone AS cust_phone, c.national_id, c.address, " +
                "r.room_number, r.floor, r.status AS room_status, " +
                "r.description AS room_desc, " +
                "rt.type_id, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Reservations res " +
                "JOIN Customers c  ON res.customer_id = c.customer_id " +
                "JOIN Rooms r      ON res.room_id = r.room_id " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE res.customer_id = ? " +
                "ORDER BY res.check_in_date DESC";

        List<Reservation> reservations = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
            return reservations;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find reservations for customer: " + customerId, e);
        }
    }


    public List<Reservation> findByStatus(String status) {
        String sql = "SELECT res.*, " +
                "c.full_name AS cust_name, c.email AS cust_email, " +
                "c.phone AS cust_phone, c.national_id, c.address, " +
                "r.room_number, r.floor, r.status AS room_status, " +
                "r.description AS room_desc, " +
                "rt.type_id, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Reservations res " +
                "JOIN Customers c  ON res.customer_id = c.customer_id " +
                "JOIN Rooms r      ON res.room_id = r.room_id " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE res.status = ? " +
                "ORDER BY res.check_in_date ASC";

        List<Reservation> reservations = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
            return reservations;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find reservations by status: " + status, e);
        }
    }


    public List<Reservation> search(String keyword) {
        String sql = "SELECT res.*, " +
                "c.full_name AS cust_name, c.email AS cust_email, " +
                "c.phone AS cust_phone, c.national_id, c.address, " +
                "r.room_number, r.floor, r.status AS room_status, " +
                "r.description AS room_desc, " +
                "rt.type_id, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Reservations res " +
                "JOIN Customers c  ON res.customer_id = c.customer_id " +
                "JOIN Rooms r      ON res.room_id = r.room_id " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE res.reservation_id LIKE ? " +
                "OR c.full_name LIKE ? " +
                "OR r.room_number LIKE ? " +
                "ORDER BY res.created_at DESC";

        List<Reservation> reservations = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String wildcard = "%" + keyword + "%";
            ps.setString(1, wildcard);
            ps.setString(2, wildcard);
            ps.setString(3, wildcard);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                reservations.add(mapRowToReservation(rs));
            }
            return reservations;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to search reservations: " + keyword, e);
        }
    }


    public boolean insert(Reservation reservation) {
        String sql = "INSERT INTO Reservations " +
                "(reservation_id, customer_id, room_id, check_in_date, " +
                "check_out_date, num_guests, status, special_requests, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservation.getReservationId());
            ps.setString(2, reservation.getCustomer().getId());
            ps.setString(3, reservation.getRoom().getRoomId());
            ps.setDate(4, new java.sql.Date(reservation.getCheckInDate().getTime()));
            ps.setDate(5, new java.sql.Date(reservation.getCheckOutDate().getTime()));
            ps.setInt(6, reservation.getNumGuests());
            ps.setString(7, reservation.getStatus());
            ps.setString(8, reservation.getSpecialRequests());
            ps.setString(9, reservation.getCreatedBy());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert reservation", e);
        }
    }


    public boolean update(Reservation reservation) {
        String sql = "UPDATE Reservations SET customer_id=?, room_id=?, " +
                "check_in_date=?, check_out_date=?, num_guests=?, " +
                "special_requests=? WHERE reservation_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservation.getCustomer().getId());
            ps.setString(2, reservation.getRoom().getRoomId());
            ps.setDate(3, new java.sql.Date(reservation.getCheckInDate().getTime()));
            ps.setDate(4, new java.sql.Date(reservation.getCheckOutDate().getTime()));
            ps.setInt(5, reservation.getNumGuests());
            ps.setString(6, reservation.getSpecialRequests());
            ps.setString(7, reservation.getReservationId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update reservation: " +
                    reservation.getReservationId(), e);
        }
    }


    public boolean updateStatus(String reservationId, String status) {
        String sql = "UPDATE Reservations SET status=? WHERE reservation_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, reservationId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update reservation status: " +
                    reservationId, e);
        }
    }


    public boolean delete(String reservationId) {
        String sql = "DELETE FROM Reservations WHERE reservation_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete reservation: " + reservationId, e);
        }
    }


    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Reservations WHERE status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to count reservations by status", e);
        }
    }


    private Reservation mapRowToReservation(ResultSet rs) throws SQLException {


        RoomType roomType = new RoomType();
        roomType.setTypeId(rs.getString("type_id"));
        roomType.setTypeName(rs.getString("type_name"));
        roomType.setPricePerNight(rs.getDouble("price_per_night"));
        roomType.setDescription(rs.getString("type_desc"));
        roomType.setMaxOccupancy(rs.getInt("max_occupancy"));


        Room room = new Room();
        room.setRoomId(rs.getString("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(roomType);
        room.setFloor(rs.getInt("floor"));
        room.setStatus(rs.getString("room_status"));
        room.setDescription(rs.getString("room_desc"));


        Customer customer = new Customer();
        customer.setId(rs.getString("customer_id"));
        customer.setFullName(rs.getString("cust_name"));
        customer.setEmail(rs.getString("cust_email"));
        customer.setPhone(rs.getString("cust_phone"));
        customer.setNationalId(rs.getString("national_id"));
        customer.setAddress(rs.getString("address"));


        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getString("reservation_id"));
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckInDate(rs.getDate("check_in_date"));
        reservation.setCheckOutDate(rs.getDate("check_out_date"));
        reservation.setNumGuests(rs.getInt("num_guests"));
        reservation.setStatus(rs.getString("status"));
        reservation.setSpecialRequests(rs.getString("special_requests"));
        reservation.setCreatedBy(rs.getString("created_by"));
        reservation.setCreatedAt(rs.getTimestamp("created_at"));

        return reservation;
    }
}
