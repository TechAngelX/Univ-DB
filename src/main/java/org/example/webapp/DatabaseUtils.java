package org.example.webapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/xe";
    private static final String USER = "system";
    private static final String PASSWORD = "duda123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
