package org.example.webapp;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {

    // Define the directory and filename of the .env file containing the database credentials
    private static final String ENV_DIRECTORY = "/Users/xeon2035/Library/CloudStorage/OneDrive-Personal/CONFIG_ENVS/jdbc/";
    private static final String ENV_FILENAME = "envUnivDB_JDBC.txt";
    //    private static final String ENV_DIRECTORY = System.getenv("ENV_DIRECTORY");
//    private static final String ENV_FILENAME = System.getenv("ENV_FILENAME");
    private static Connection connection = null; // Static connection variable to store the database connection


    static {
        try {
            // Load the JDBC driver class for Oracle. This step is necessary for establishing a connection.
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            // If the driver class is not found, throw a RuntimeException to stop the application.
            throw new RuntimeException("Oracle JDBC driver not found", e);
        }
    }

    /**
     * Method to get a database connection.
     * If the connection does not exist or is closed, a new one will be established using the credentials from the .env file.
     *
     * @return a Connection object to the database
     * @throws SQLException if a database access error occurs or environment variables are not set properly
     */
    public static Connection getConnection() throws SQLException {
        // Load environment variables each time getConnection() is called to ensure the latest credentials are used
        Dotenv dotenv = Dotenv.configure()
                .directory(ENV_DIRECTORY)
                .filename(ENV_FILENAME)
                .load();


        // Retrieve database connection details from environment variables
        String url = dotenv.get("DB_URL");
        String user = dotenv.get("DB_USER");
        String password = dotenv.get("DB_PASSWORD");

        // Test Print to Console for debugging
        System.out.println("Loaded DB_URL: " + url);
        System.out.println("Loaded DB_USER: " + user);
        System.out.println("Loaded DB_PASSWORD: " + password);
        System.out.println("Using environment file directory: " + ENV_DIRECTORY);
        System.out.println("Using environment file: " + ENV_FILENAME);


        // Check if the essential environment variables are set
        if (url == null || user == null || password == null) {
            throw new SQLException("Database connection details are not properly configured. Please set DB_URL, DB_USER, and DB_PASSWORD.");

        }

        // If the connection exists and is open, close it to refresh with new credentials
        if (connection != null && !connection.isClosed()) {
            connection.close(); // Close the current connection
        }

        // Establish a new connection with the updated credentials from the environment variables
        connection = DriverManager.getConnection(url, user, password);

        return connection; // Return the newly established connection
    }
}
