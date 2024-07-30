package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UnivJDBC {
    public static void main(String[] args) {
        // Retrieve JDBC URL, username, and password from environment variables
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Check if the environment variables are set
        if (url == null || user == null || password == null) {
            System.err.println("Missing required environment variables. Please set DB_URL, DB_USER, and DB_PASSWORD.");
            return;
        }

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
