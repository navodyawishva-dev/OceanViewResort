package com.oceanview.dao;

import com.oceanview.exception.DataAccessException;
import com.oceanview.model.Admin;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AdminDAO {


    public Admin findByUsername(String username) {
        String sql = "SELECT * FROM Admins WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToAdmin(rs);
            }
            return null; // not found

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find admin by username: " + username, e);
        }
    }

    public Admin findById(String adminId) {
        String sql = "SELECT * FROM Admins WHERE admin_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, adminId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToAdmin(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find admin by ID: " + adminId, e);
        }
    }

    public List<Admin> findAll() {
        String sql = "SELECT * FROM Admins ORDER BY created_at DESC";
        List<Admin> admins = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                admins.add(mapRowToAdmin(rs));
            }
            return admins;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all admins", e);
        }
    }

    public boolean insert(Admin admin) {
        String sql = "INSERT INTO Admins " +
                "(admin_id, username, password, full_name, email, phone) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, admin.getId());
            ps.setString(2, admin.getUsername());
            ps.setString(3, admin.getPassword());
            ps.setString(4, admin.getFullName());
            ps.setString(5, admin.getEmail());
            ps.setString(6, admin.getPhone());

            return ps.executeUpdate() > 0; // returns true if 1 row inserted

        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert admin: " + admin.getUsername(), e);
        }
    }

    public boolean update(Admin admin) {
        String sql = "UPDATE Admins SET full_name=?, email=?, phone=? " +
                "WHERE admin_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, admin.getFullName());
            ps.setString(2, admin.getEmail());
            ps.setString(3, admin.getPhone());
            ps.setString(4, admin.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update admin: " + admin.getId(), e);
        }
    }

    public boolean updatePassword(String adminId, String newPassword) {
        String sql = "UPDATE Admins SET password=? WHERE admin_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, adminId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update password for admin: " + adminId, e);
        }
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Admins WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        } catch (SQLException e) {
            System.out.println("Admin usernameExists error: " + e.getMessage());
            return false;
        }
    }

    private Admin mapRowToAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setId(rs.getString("admin_id"));        // ← must be "admin_id"
        admin.setUsername(rs.getString("username"));
        admin.setPassword(rs.getString("password"));
        admin.setFullName(rs.getString("full_name"));  // ← must be "full_name"
        admin.setEmail(rs.getString("email"));
        admin.setPhone(rs.getString("phone"));
        admin.setCreatedAt(rs.getTimestamp("created_at")); // ← must be "created_at"
        return admin;
    }
}
