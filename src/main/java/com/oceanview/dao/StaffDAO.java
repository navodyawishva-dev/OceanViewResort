package com.oceanview.dao;

import com.oceanview.exception.DataAccessException;
import com.oceanview.model.Staff;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffDAO {


    public Staff findByUsername(String username) {
        String sql = "SELECT * FROM Staff WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToStaff(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find staff by username: " + username, e);
        }
    }


    public Staff findById(String staffId) {
        String sql = "SELECT * FROM Staff WHERE staff_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staffId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToStaff(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find staff by ID: " + staffId, e);
        }
    }


    public List<Staff> findAll() {
        String sql = "SELECT * FROM Staff ORDER BY created_at DESC";
        List<Staff> staffList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                staffList.add(mapRowToStaff(rs));
            }
            return staffList;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all staff", e);
        }
    }


    public List<Staff> findByStatus(String status) {
        String sql = "SELECT * FROM Staff WHERE status = ? ORDER BY created_at DESC";
        List<Staff> staffList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                staffList.add(mapRowToStaff(rs));
            }
            return staffList;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find staff by status: " + status, e);
        }
    }


    public boolean insert(Staff staff) {
        String sql = "INSERT INTO Staff " +
                "(staff_id, username, password, full_name, email, phone, role, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staff.getId());
            ps.setString(2, staff.getUsername());
            ps.setString(3, staff.getPassword());
            ps.setString(4, staff.getFullName());
            ps.setString(5, staff.getEmail());
            ps.setString(6, staff.getPhone());
            ps.setString(7, staff.getRole());
            ps.setString(8, staff.getStatus());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert staff: " + staff.getUsername(), e);
        }
    }


    public boolean update(Staff staff) {
        String sql = "UPDATE Staff SET full_name=?, email=?, phone=?, role=? " +
                "WHERE staff_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staff.getFullName());
            ps.setString(2, staff.getEmail());
            ps.setString(3, staff.getPhone());
            ps.setString(4, staff.getRole());
            ps.setString(5, staff.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update staff: " + staff.getId(), e);
        }
    }


    public boolean updateStatus(String staffId, String status) {
        String sql = "UPDATE Staff SET status=? WHERE staff_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setString(2, staffId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update status for staff: " + staffId, e);
        }
    }


    public boolean updatePassword(String staffId, String newPassword) {
        String sql = "UPDATE Staff SET password=? WHERE staff_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, staffId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update password for staff: " + staffId, e);
        }
    }


    public boolean delete(String staffId) {
        String sql = "DELETE FROM Staff WHERE staff_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staffId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete staff: " + staffId, e);
        }
    }


    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Staff WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
            return false;
        } catch (SQLException e) {
            System.out.println("Staff usernameExists error: " + e.getMessage());
            return false;
        }
    }


    public int countByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Staff WHERE status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to count staff by status", e);
        }
    }


    private Staff mapRowToStaff(ResultSet rs) throws SQLException {
        Staff staff = new Staff();
        staff.setId(rs.getString("staff_id"));         // ← must be "staff_id"
        staff.setUsername(rs.getString("username"));
        staff.setPassword(rs.getString("password"));
        staff.setFullName(rs.getString("full_name"));  // ← must be "full_name"
        staff.setEmail(rs.getString("email"));
        staff.setPhone(rs.getString("phone"));
        staff.setRole(rs.getString("role"));
        staff.setStatus(rs.getString("status"));
        staff.setCreatedAt(rs.getTimestamp("created_at"));
        return staff;
    }
}