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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("fname");
        String lastName = request.getParameter("lname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType"); // Should be '1' for student or '2' for staff
        String programme = request.getParameter("prog_id");
        String staffRole = request.getParameter("role"); // Used only for staff, should be 'academic' or 'professional'

        // Validate input fields
        if (firstName == null || lastName == null || email == null || password == null || userType == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing form details");
            return;
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Generate a unique username
        String username = generateUsername(firstName, lastName);

        // Determine the unique number and role ID based on userType
        String uniqueNumber;
        int roleId;
        int userTypeId;
        if ("1".equals(userType)) { // Student
            uniqueNumber = generateUniqueNumber('2'); // Students start with '2'
            roleId = 3; // Assuming role ID 3 for students (you may need to adjust this based on your roles table)
            userTypeId = 1; // User type ID for students
        } else if ("2".equals(userType)) { // Staff
            uniqueNumber = generateUniqueNumber('5'); // Staff start with '5'
            if ("academic".equalsIgnoreCase(staffRole)) {
                roleId = 1; // Academic Staff
            } else if ("professional".equalsIgnoreCase(staffRole)) {
                roleId = 2; // Professional Services Staff
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid staff role");
                return;
            }
            userTypeId = 2; // User type ID for staff
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user type");
            return;
        }

        // Insert user information into USER_ACC table
        String sqlUserAcc = "INSERT INTO USER_ACC (USERNAME_DB, EMAIL_DB, PWORD_DB, ROLE_ID_DB, FNAME_DB, LNAME_DB) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmtUserAcc = conn.prepareStatement(sqlUserAcc, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmtUserAcc.setString(1, username);
            stmtUserAcc.setString(2, email);
            stmtUserAcc.setString(3, hashedPassword);
            stmtUserAcc.setInt(4, roleId);
            stmtUserAcc.setString(5, firstName);
            stmtUserAcc.setString(6, lastName);

            stmtUserAcc.executeUpdate();

            // Retrieve the generated user ID
            int userId = -1;
            try (ResultSet rs = stmtUserAcc.getGeneratedKeys()) {
                if (rs.next()) {
                    userId = rs.getInt(1);
                } else {
                    throw new SQLException("Failed to obtain user ID.");
                }
            }

            // Insert student or staff-specific information
            if (userTypeId == 1) { // Student
                String sqlStudent = "INSERT INTO STUDENT (STUD_NUM, PROG_ID_DB, USER_ID_DB) VALUES (?, ?, ?)";
                try (PreparedStatement stmtStudent = conn.prepareStatement(sqlStudent)) {
                    stmtStudent.setString(1, uniqueNumber);
                    stmtStudent.setString(2, programme);
                    stmtStudent.setInt(3, userId);
                    stmtStudent.executeUpdate();
                }
            } else if (userTypeId == 2) { // Staff
                String sqlStaff = "INSERT INTO STAFF (STAFF_NUM, STAFF_TYPE_DB, USER_ID_DB) VALUES (?, ?, ?)";
                try (PreparedStatement stmtStaff = conn.prepareStatement(sqlStaff)) {
                    stmtStaff.setString(1, uniqueNumber);
                    stmtStaff.setString(2, staffRole); // Assuming `staffRole` is a string describing the staff type
                    stmtStaff.setInt(3, userId);
                    stmtStaff.executeUpdate();
                }
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

    private String generateUniqueNumber(char prefix) {
        Random random = new Random();
        // Generate 7 random digits
        int number = random.nextInt(10000000); // This generates a number between 0 and 9999999
        // Format it to 7 digits with leading zeros if necessary
        String formattedNumber = String.format("%07d", number);
        // Concatenate prefix with the formatted number
        return prefix + formattedNumber;
    }
}
