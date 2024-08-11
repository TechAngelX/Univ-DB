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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("uname");
        String password = request.getParameter("pword");

        if (username == null || password == null) {
            response.sendRedirect("login.jsp?error=Invalid%20credentials");
            return;
        }

        boolean isAuthenticated = authenticateUser(username, password);

        if (isAuthenticated) {
            request.getSession().setAttribute("username", username);
            response.sendRedirect("home.jsp"); // Redirect to the home page
        } else {
            response.sendRedirect("login.jsp?error=Invalid%20username%20or%20password");
        }
    }

    private boolean authenticateUser(String username, String password) {
        boolean isValidUser = false;
        String sql = "SELECT pWordHash_db FROM USER_ACCOUNT WHERE uName_db = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("pWordHash_db");
                    // For this example, we are comparing the passwords directly
                    // In a real application, you'd hash and compare passwords securely
                    if (password.equals(storedPassword)) {
                        isValidUser = true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }

        return isValidUser;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("login.jsp");
    }
}
