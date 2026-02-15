package com.oceanview.dao;

import com.oceanview.exception.DataAccessException;
import com.oceanview.model.Customer;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CustomerDAO {


    public Customer findById(String customerId) {
        String sql = "SELECT * FROM Customers WHERE customer_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToCustomer(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find customer by ID: " + customerId, e);
        }
    }


    public Customer findByNationalId(String nationalId) {
        String sql = "SELECT * FROM Customers WHERE national_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nationalId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToCustomer(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find customer by NIC: " + nationalId, e);
        }
    }


    public Customer findByPhone(String phone) {
        String sql = "SELECT * FROM Customers WHERE phone = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToCustomer(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find customer by phone: " + phone, e);
        }
    }


    public List<Customer> searchByName(String name) {
        String sql = "SELECT * FROM Customers WHERE full_name LIKE ? " +
                "ORDER BY full_name ASC";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%"); // % = wildcard
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                customers.add(mapRowToCustomer(rs));
            }
            return customers;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to search customers by name: " + name, e);
        }
    }


    public List<Customer> findAll() {
        String sql = "SELECT * FROM Customers ORDER BY full_name ASC";
        List<Customer> customers = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                customers.add(mapRowToCustomer(rs));
            }
            return customers;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all customers", e);
        }
    }


    public boolean insert(Customer customer) {
        String sql = "INSERT INTO Customers " +
                "(customer_id, full_name, email, phone, national_id, address) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getId());
            ps.setString(2, customer.getFullName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.setString(5, customer.getNationalId());
            ps.setString(6, customer.getAddress());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert customer: " + customer.getFullName(), e);
        }
    }


    public boolean update(Customer customer) {
        String sql = "UPDATE Customers SET full_name=?, email=?, phone=?, address=? " +
                "WHERE customer_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhone());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to update customer: " + customer.getId(), e);
        }
    }


    public boolean delete(String customerId) {
        String sql = "DELETE FROM Customers WHERE customer_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customerId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete customer: " + customerId, e);
        }
    }


    public boolean nationalIdExists(String nationalId) {
        String sql = "SELECT COUNT(*) FROM Customers WHERE national_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nationalId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1) > 0;
            return false;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to check national ID: " + nationalId, e);
        }
    }


    public int countAll() {
        String sql = "SELECT COUNT(*) FROM Customers";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to count customers", e);
        }
    }


    private Customer mapRowToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getString("customer_id"));
        customer.setFullName(rs.getString("full_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        customer.setNationalId(rs.getString("national_id"));
        customer.setAddress(rs.getString("address"));
        customer.setCreatedAt(rs.getTimestamp("created_at"));
        return customer;
    }
}