package org.example.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null) {
            response.sendRedirect("login.jsp?error=Invalid%20credentials");
            return;
        }

        try {
            boolean isAuthenticated = authenticateUser(username, password);

            if (isAuthenticated) {
                request.getSession().setAttribute("username", username);
                response.sendRedirect("home.jsp");
            } else {
                response.sendRedirect("login.jsp?error=Invalid%20username%20or%20password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Database%20error");
        }
    }

    private boolean authenticateUser(String username, String password) throws SQLException {
        boolean isValidUser = false;
        String sql = "SELECT PWORD FROM USER_ACC WHERE USERNAME = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPasswordHash = resultSet.getString("PWORD");
                    if (BCrypt.checkpw(password, storedPasswordHash)) {
                        isValidUser = true;
                    } else {
                        System.out.println("Password mismatch for user: " + username);
                    }
                } else {
                    System.out.println("No user found with username: " + username);
                }
            }
        }
        return isValidUser;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("login.jsp");
    }
}
