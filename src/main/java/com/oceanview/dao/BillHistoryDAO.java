package com.oceanview.dao;

import com.oceanview.exception.DataAccessException;
import com.oceanview.model.BillHistory;
import com.oceanview.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class BillHistoryDAO {


    public BillHistory findById(String historyId) {
        String sql = "SELECT * FROM BillHistory WHERE history_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, historyId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRowToBillHistory(rs);
            }
            return null;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find history record: " + historyId, e);
        }
    }


    public List<BillHistory> findByBillId(String billId) {
        String sql = "SELECT * FROM BillHistory " +
                "WHERE bill_id = ? " +
                "ORDER BY action_date ASC";

        List<BillHistory> historyList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, billId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                historyList.add(mapRowToBillHistory(rs));
            }
            return historyList;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find history for bill: " + billId, e);
        }
    }


    public List<BillHistory> findAll() {
        String sql = "SELECT * FROM BillHistory ORDER BY action_date DESC";
        List<BillHistory> historyList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                historyList.add(mapRowToBillHistory(rs));
            }
            return historyList;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all bill history", e);
        }
    }


    public List<BillHistory> findByStaffId(String staffId) {
        String sql = "SELECT * FROM BillHistory " +
                "WHERE performed_by = ? " +
                "ORDER BY action_date DESC";

        List<BillHistory> historyList = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, staffId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                historyList.add(mapRowToBillHistory(rs));
            }
            return historyList;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to find history for staff: " + staffId, e);
        }
    }


    public boolean insert(BillHistory billHistory) {
        String sql = "INSERT INTO BillHistory " +
                "(history_id, bill_id, action, performed_by) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, billHistory.getHistoryId());
            ps.setString(2, billHistory.getBillId());
            ps.setString(3, billHistory.getAction());
            ps.setString(4, billHistory.getPerformedBy());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to insert bill history record", e);
        }
    }


    public boolean deleteByBillId(String billId) {
        String sql = "DELETE FROM BillHistory WHERE bill_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, billId);
            return ps.executeUpdate() >= 0;

        } catch (SQLException e) {
            throw new DataAccessException("Failed to delete history for bill: " + billId, e);
        }
    }


    private BillHistory mapRowToBillHistory(ResultSet rs) throws SQLException {
        BillHistory history = new BillHistory();
        history.setHistoryId(rs.getString("history_id"));
        history.setBillId(rs.getString("bill_id"));
        history.setAction(rs.getString("action"));
        history.setActionDate(rs.getTimestamp("action_date"));
        history.setPerformedBy(rs.getString("performed_by"));
        return history;
    }
}
