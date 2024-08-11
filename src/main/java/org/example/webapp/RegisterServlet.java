package org.example.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String firstName = request.getParameter("fname");
        String lastName = request.getParameter("lname");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("pwordConfirm");
        String email = request.getParameter("email");

        // Validate form data
        if (isInvalidInput(firstName, lastName, password, confirmPassword, email)) {
            response.getWriter().println("All fields are required.");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            response.getWriter().println("Invalid email format.");
            return;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            response.getWriter().println("Passwords do not match.");
            return;
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Insert data into the database
        if (registerUser(firstName, lastName, email, hashedPassword)) {
            // Registration successful, set session and redirect
            request.getSession().setAttribute("username", firstName);
            response.sendRedirect("home.jsp");
        } else {
            response.getWriter().println("Error: Unable to register user.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("register.jsp"); // Display the registration page
    }

    private boolean isInvalidInput(String firstName, String lastName, String password, String confirmPassword, String email) {
        return firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty() ||
                email == null || email.isEmpty();
    }

    private boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

    private boolean registerUser(String firstName, String lastName, String email, String hashedPassword) throws ServletException {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String sql = "INSERT INTO USER_ACCOUNT (role_id_db, fName_db, lName_db, uName_db, pWordHash_db, eMail_db) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            // Assuming role ID 1 is default for new users
            int defaultRoleId = 1;

            stmt.setInt(1, defaultRoleId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, email); // Assuming username is the same as email
            stmt.setString(5, hashedPassword);
            stmt.setString(6, email);

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            throw new ServletException("Database access error", e);
        }
    }
}
