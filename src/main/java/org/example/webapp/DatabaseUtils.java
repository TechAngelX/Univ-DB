package org.example.webapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static Connection connection = null;

    static {
        try {
            // Load the JDBC driver
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Oracle JDBC driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        // Check if environment variables are set
        if (URL == null || USER == null || PASSWORD == null) {
            throw new SQLException("Database connection details are not properly configured. Please set DB_URL, DB_USER, and DB_PASSWORD.");
        }

        // Initialize connection if it doesn't exist or is closed
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }
}