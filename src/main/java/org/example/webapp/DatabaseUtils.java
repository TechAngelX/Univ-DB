package org.example.webapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
    private static final String HOST = System.getenv("DB_HOST");
    private static final String PORT = System.getenv("DB_PORT");
    private static final String DBNAME = System.getenv("DB_NAME");
    private static final String USERNAME = System.getenv("DB_USERNAME");
    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        String jdbcUrl = "jdbc:oracle:thin:@" + HOST + ":" + PORT + ":" + DBNAME;
        return DriverManager.getConnection(jdbcUrl, USERNAME, PASSWORD);
    }
}
