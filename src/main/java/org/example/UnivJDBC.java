package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UnivJDBC {
    public static void main(String[] args) {
        // JDBC URL, username, and password
        String url = "jdbc:oracle:thin:@localhost:1521/xe";
        String user = "system"; // Use a standard user account
        String password = "duda123"; // Ensure this matches the user's password

        try {
            // Establish the connection
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");

            // Perform database operations (optional)

            // Close the connection
            connection.close();
        } catch (SQLException e) {
            // Print SQL exception details
            e.printStackTrace();
        }
    }
}
