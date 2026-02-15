package com.oceanview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DATABASE = "OceanViewResort";

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;" +
                    "instanceName=SQLEXPRESS;" +
                    "databaseName=OceanViewResort;" +
                    "user=oceanview_user;" +
                    "password=Ocean@1234;" +
                    "encrypt=false;" +
                    "trustServerCertificate=true;" +
                    "loginTimeout=30;";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL);
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "SQL Server Driver not found. Check pom.xml.", e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        try (Connection conn = getConnection()) {
            if (conn != null) {
                System.out.println("SUCCESS - Connected to: " + DATABASE);
            }
        } catch (SQLException e) {
            System.out.println("FAILED - " + e.getMessage());
        }
    }
}