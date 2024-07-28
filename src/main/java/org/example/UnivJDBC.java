package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UnivJDBC {
    // Database connection details
    private static final String HOST = "localhost";
    private static final String PORT = "1521";
    private static final String DBNAME = "XE";
    private static final String USERNAME = "system";
    private static final String PASSWORD = "duda123";

    private static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + DBNAME;
        return DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD);
    }

    public static void main(String[] args) {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("Oracle JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.err.println("Oracle JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
            return;
        }

        // Example usage
        String testUsername = "testUser";
        String testPassword = "testPassword";
        String testEmail = "testUser@example.com";

        if (register(testUsername, testPassword, testEmail)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed.");
        }

        if (login(testUsername, testPassword)) {
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public static boolean register(String username, String password, String email) {
        String insertSQL = "INSERT INTO USER_ACCOUNT (userName, hashedPword, email) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                // ORA-00001: unique constraint violated
                System.out.println("A user with this username already exists.");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static boolean login(String username, String password) {
        String sql = "SELECT * FROM USER_ACCOUNT WHERE userName = ? AND hashedPword = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
