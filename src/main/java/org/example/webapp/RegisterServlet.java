package org.example.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;
import java.util.Random;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("fname");
        String lastName = request.getParameter("lname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType");
        String programme = request.getParameter("prog_id");
        String staffRole = request.getParameter("role");

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Generate a unique username
        String username = generateUsername(firstName, lastName);

        // Insert user information into USER_ACC table
        String sqlUserAcc = "INSERT INTO USER_ACC (USERNAME_DB, EMAIL_DB, PWORD_DB, ROLE_ID_DB) VALUES (?, ?, ?, ?)";
        int roleId = "student".equals(userType) ? 1 : 2; // Assuming 1 for students and 2 for staff

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmtUserAcc = conn.prepareStatement(sqlUserAcc, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmtUserAcc.setString(1, username);
            stmtUserAcc.setString(2, email);
            stmtUserAcc.setString(3, hashedPassword);
            stmtUserAcc.setInt(4, roleId);
            stmtUserAcc.executeUpdate();

            // Get generated user ID
            int userId = 0;
            try (ResultSet generatedKeys = stmtUserAcc.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }
            }

            // Insert student or staff-specific information
            if ("student".equals(userType)) {
                String sqlStudent = "INSERT INTO STUDENT (STUD_NUM, PROG_ID_DB, USER_ID_DB) VALUES (?, ?, ?)";
                try (PreparedStatement stmtStudent = conn.prepareStatement(sqlStudent)) {
                    stmtStudent.setInt(1, userId);
                    stmtStudent.setString(2, programme);
                    stmtStudent.setString(3, firstName);
                    stmtStudent.setString(4, lastName);
                    stmtStudent.executeUpdate();
                }
            } else if ("staff".equals(userType)) {
                String sqlStaff = "INSERT INTO STAFF (STAFF_NUM, STAFF_TYPE_DB, USER_ID_DB) VALUES (?, ?, ?)";
                try (PreparedStatement stmtStaff = conn.prepareStatement(sqlStaff)) {
                    stmtStaff.setInt(1, userId);
                    stmtStaff.setString(2, staffRole);
                    stmtStaff.setString(3, firstName);
                    stmtStaff.setString(4, lastName);
                    stmtStaff.executeUpdate();
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user type");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred during registration");
            return;
        }

        // Redirect to a success page
        response.sendRedirect("home.jsp");
    }

    private String generateUsername(String firstName, String lastName) {
        String firstInitial = firstName.length() > 0 ? String.valueOf(firstName.charAt(0)) : "";
        String lastFour = lastName.length() >= 4 ? lastName.substring(lastName.length() - 4) : lastName;
        String randomNumbers = String.format("%03d", new Random().nextInt(1000));

        return (firstInitial + lastFour + randomNumbers).toLowerCase();
    }
}
