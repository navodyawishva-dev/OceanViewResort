package com.oceanview.dao;

import com.oceanview.exception.DataAccessException;
import com.oceanview.model.RoomType;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class RoomTypeDAO {


    public RoomType findById(String typeId) {
        String sql = "SELECT * FROM RoomTypes WHERE type_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, typeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToRoomType(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find room type by ID: " + typeId, e);
        }
    }


    public RoomType findByName(String typeName) {
        String sql = "SELECT * FROM RoomTypes WHERE type_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, typeName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToRoomType(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find room type by name: " + typeName, e);
        }
    }


    public List<RoomType> findAll() {
        String sql = "SELECT * FROM RoomTypes ORDER BY price_per_night ASC";
        List<RoomType> roomTypes = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roomTypes.add(mapRowToRoomType(rs));
            }
            return roomTypes;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all room types", e);
        }
    }


    public boolean insert(RoomType roomType) {
        String sql = "INSERT INTO RoomTypes " +
                "(type_id, type_name, price_per_night, description, max_occupancy) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomType.getTypeId());
            ps.setString(2, roomType.getTypeName());
            ps.setDouble(3, roomType.getPricePerNight());
            ps.setString(4, roomType.getDescription());
            ps.setInt(5, roomType.getMaxOccupancy());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert room type: " + roomType.getTypeName(), e);
        }
    }


    public boolean update(RoomType roomType) {
        String sql = "UPDATE RoomTypes SET type_name=?, price_per_night=?, " +
                "description=?, max_occupancy=? WHERE type_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomType.getTypeName());
            ps.setDouble(2, roomType.getPricePerNight());
            ps.setString(3, roomType.getDescription());
            ps.setInt(4, roomType.getMaxOccupancy());
            ps.setString(5, roomType.getTypeId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update room type: " + roomType.getTypeId(), e);
        }
    }


    public boolean delete(String typeId) {
        String sql = "DELETE FROM RoomTypes WHERE type_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, typeId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete room type: " + typeId, e);
        }
    }


    public boolean typeNameExists(String typeName) {
        String sql = "SELECT COUNT(*) FROM RoomTypes WHERE type_name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, typeName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1) > 0;
            return false;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to check type name: " + typeName, e);
        }
    }


    public boolean hasRooms(String typeId) {
        String sql = "SELECT COUNT(*) FROM Rooms WHERE type_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, typeId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1) > 0;
            return false;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to check rooms for type: " + typeId, e);
        }
    }


    private RoomType mapRowToRoomType(ResultSet rs) throws SQLException {
        RoomType roomType = new RoomType();
        roomType.setTypeId(rs.getString("type_id"));
        roomType.setTypeName(rs.getString("type_name"));
        roomType.setPricePerNight(rs.getDouble("price_per_night"));
        roomType.setDescription(rs.getString("description"));
        roomType.setMaxOccupancy(rs.getInt("max_occupancy"));
        return roomType;
    }
}
