package com.oceanview.dao;

import com.oceanview.exception.DataAccessException;
import com.oceanview.model.Room;
import com.oceanview.model.RoomType;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RoomDAO {


    public Room findById(String roomId) {
        String sql = "SELECT r.*, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Rooms r " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE r.room_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToRoom(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find room by ID: " + roomId, e);
        }
    }


    public Room findByRoomNumber(String roomNumber) {
        String sql = "SELECT r.*, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Rooms r " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE r.room_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToRoom(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find room by number: " + roomNumber, e);
        }
    }


    public List<Room> findAll() {
        String sql = "SELECT r.*, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Rooms r " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "ORDER BY r.room_number ASC";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rooms.add(mapRowToRoom(rs));
            }
            return rooms;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all rooms", e);
        }
    }


    public List<Room> findByStatus(String status) {
        String sql = "SELECT r.*, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Rooms r " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE r.status = ? " +
                "ORDER BY r.room_number ASC";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rooms.add(mapRowToRoom(rs));
            }
            return rooms;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find rooms by status: " + status, e);
        }
    }


    public List<Room> findAvailableRooms(Date checkIn, Date checkOut) {
        String sql = "SELECT r.*, rt.type_name, rt.price_per_night, " +
                "rt.description AS type_desc, rt.max_occupancy " +
                "FROM Rooms r " +
                "JOIN RoomTypes rt ON r.type_id = rt.type_id " +
                "WHERE r.status = 'Available' " +
                "AND r.room_id NOT IN ( " +
                "    SELECT res.room_id FROM Reservations res " +
                "    WHERE res.status IN ('Confirmed', 'Checked-In') " +
                "    AND res.check_in_date  < ? " +
                "    AND res.check_out_date > ? " +
                ") " +
                "ORDER BY r.room_number ASC";
        List<Room> rooms = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(checkOut.getTime()));
            ps.setDate(2, new java.sql.Date(checkIn.getTime()));
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                rooms.add(mapRowToRoom(rs));
            }
            return rooms;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find available rooms", e);
        }
    }


    public boolean isRoomAvailable(String roomId, Date checkIn, Date checkOut,
                                   String excludeReservationId) {
        String sql = "SELECT COUNT(*) FROM Reservations " +
                "WHERE room_id = ? " +
                "AND status IN ('Confirmed', 'Checked-In') " +
                "AND check_in_date  < ? " +
                "AND check_out_date > ? " +
                "AND reservation_id != ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            ps.setDate(2, new java.sql.Date(checkOut.getTime()));
            ps.setDate(3, new java.sql.Date(checkIn.getTime()));
            // If no reservation to exclude, use empty string (won't match anything)
            ps.setString(4, excludeReservationId != null ? excludeReservationId : "");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // available if count is 0
            }
            return true;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to check room availability", e);
        }
    }


    public boolean insert(Room room) {
        String sql = "INSERT INTO Rooms " +
                "(room_id, room_number, type_id, floor, status, description) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room.getRoomId());
            ps.setString(2, room.getRoomNumber());
            ps.setString(3, room.getRoomType().getTypeId());
            ps.setInt(4, room.getFloor());
            ps.setString(5, room.getStatus());
            ps.setString(6, room.getDescription());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert room: " + room.getRoomNumber(), e);
        }
    }


    public boolean update(Room room) {
        String sql = "UPDATE Rooms SET room_number=?, type_id=?, floor=?, " +
                "status=?, description=? WHERE room_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getRoomType().getTypeId());
            ps.setInt(3, room.getFloor());
            ps.setString(4, room.getStatus());
            ps.setString(5, room.getDescription());
            ps.setString(6, room.getRoomId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update room: " + room.getRoomId(), e);
        }
    }


    public boolean updateStatus(String roomId, String status) {
        String sql = "UPDATE Rooms SET status=? WHERE room_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, roomId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update room status: " + roomId, e);
        }
    }


    public boolean delete(String roomId) {
        String sql = "DELETE FROM Rooms WHERE room_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete room: " + roomId, e);
        }
    }


    public boolean roomNumberExists(String roomNumber) {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE room_number = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1) > 0;
            return false;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to check room number: " + roomNumber, e);
        }
    }


    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to count rooms by status", e);
        }
    }


    private Room mapRowToRoom(ResultSet rs) throws SQLException {
        // First map the RoomType
        RoomType roomType = new RoomType();
        roomType.setTypeId(rs.getString("type_id"));
        roomType.setTypeName(rs.getString("type_name"));
        roomType.setPricePerNight(rs.getDouble("price_per_night"));
        roomType.setDescription(rs.getString("type_desc"));
        roomType.setMaxOccupancy(rs.getInt("max_occupancy"));

        // Then map the Room with the RoomType inside it
        Room room = new Room();
        room.setRoomId(rs.getString("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(roomType);
        room.setFloor(rs.getInt("floor"));
        room.setStatus(rs.getString("status"));
        room.setDescription(rs.getString("description"));
        return room;
    }
}
