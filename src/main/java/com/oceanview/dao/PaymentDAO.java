package com.oceanview.dao;

import com.oceanview.exception.DataAccessException;
import com.oceanview.model.*;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class PaymentDAO {


    public Payment findById(String paymentId) {
        String sql = "SELECT p.*, " +
                "b.reservation_id, b.num_nights, b.room_price, " +
                "b.subtotal, b.tax_amount, b.total_amount, " +
                "b.payment_status, b.generated_at " +
                "FROM Payments p " +
                "JOIN Bills b ON p.bill_id = b.bill_id " +
                "WHERE p.payment_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToPayment(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find payment: " + paymentId, e);
        }
    }


    public List<Payment> findByBillId(String billId) {
        String sql = "SELECT p.*, " +
                "b.reservation_id, b.num_nights, b.room_price, " +
                "b.subtotal, b.tax_amount, b.total_amount, " +
                "b.payment_status, b.generated_at " +
                "FROM Payments p " +
                "JOIN Bills b ON p.bill_id = b.bill_id " +
                "WHERE p.bill_id = ? " +
                "ORDER BY p.payment_date DESC";

        List<Payment> payments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, billId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                payments.add(mapRowToPayment(rs));
            }
            return payments;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find payments for bill: " + billId, e);
        }
    }


    public List<Payment> findAll() {
        String sql = "SELECT p.*, " +
                "b.reservation_id, b.num_nights, b.room_price, " +
                "b.subtotal, b.tax_amount, b.total_amount, " +
                "b.payment_status, b.generated_at " +
                "FROM Payments p " +
                "JOIN Bills b ON p.bill_id = b.bill_id " +
                "ORDER BY p.payment_date DESC";

        List<Payment> payments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                payments.add(mapRowToPayment(rs));
            }
            return payments;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all payments", e);
        }
    }


    public boolean insert(Payment payment) {
        String sql = "INSERT INTO Payments " +
                "(payment_id, bill_id, amount_paid, payment_method, processed_by) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, payment.getPaymentId());
            ps.setString(2, payment.getBill().getBillId());
            ps.setDouble(3, payment.getAmountPaid());
            ps.setString(4, payment.getPaymentMethod());
            ps.setString(5, payment.getProcessedBy());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert payment: " +
                    payment.getPaymentId(), e);
        }
    }


    public boolean delete(String paymentId) {
        String sql = "DELETE FROM Payments WHERE payment_id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paymentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete payment: " + paymentId, e);
        }
    }


    public double getTotalPaidForBill(String billId) {
        String sql = "SELECT SUM(amount_paid) FROM Payments WHERE bill_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, billId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getDouble(1);
            return 0.0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to get total paid for bill: " + billId, e);
        }
    }


    public int countByMethod(String method) {
        String sql = "SELECT COUNT(*) FROM Payments WHERE payment_method = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, method);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) return rs.getInt(1);
            return 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to count payments by method", e);
        }
    }


    private Payment mapRowToPayment(ResultSet rs) throws SQLException {

        // Map Bill (lightweight — no nested reservation here)
        Bill bill = new Bill();
        bill.setBillId(rs.getString("bill_id"));
        bill.setNumNights(rs.getInt("num_nights"));
        bill.setRoomPrice(rs.getDouble("room_price"));
        bill.setSubtotal(rs.getDouble("subtotal"));
        bill.setTaxAmount(rs.getDouble("tax_amount"));
        bill.setTotalAmount(rs.getDouble("total_amount"));
        bill.setPaymentStatus(rs.getString("payment_status"));
        bill.setGeneratedAt(rs.getTimestamp("generated_at"));

        // Map Payment
        Payment payment = new Payment();
        payment.setPaymentId(rs.getString("payment_id"));
        payment.setBill(bill);
        payment.setAmountPaid(rs.getDouble("amount_paid"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setPaymentDate(rs.getTimestamp("payment_date"));
        payment.setProcessedBy(rs.getString("processed_by"));

        return payment;
    }
}
