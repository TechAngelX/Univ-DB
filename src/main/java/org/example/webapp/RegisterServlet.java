package org.example.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Check if the username or password is missing
        if (username == null || password == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Username or password is missing");
            return;
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // SQL statement to insert user into USER_ACC table
        String sqlUserAcc = "INSERT INTO USER_ACC (USERNAME, PWORD) VALUES (?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmtUserAcc = conn.prepareStatement(sqlUserAcc)) {

            // Set parameters
            stmtUserAcc.setString(1, username);
            stmtUserAcc.setString(2, hashedPassword);

            // Execute update
            stmtUserAcc.executeUpdate();

            // Redirect to a success page
            response.sendRedirect("home.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred during registration");
        }
    }
}
