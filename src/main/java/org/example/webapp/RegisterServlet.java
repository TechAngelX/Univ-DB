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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final Random RANDOM = new Random();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("fname");
        String lastName = request.getParameter("lname");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("pwordConfirm");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        if (isInvalidInput(firstName, lastName, password, confirmPassword, email, role)) {
            response.getWriter().println("All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            response.getWriter().println("Invalid email format.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            response.getWriter().println("Passwords do not match.");
            return;
        }

        // Generate a unique username
        String username = generateUniqueUsername(firstName, lastName);

        if (username == null) {
            response.getWriter().println("Error: Unable to generate a unique username.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        int roleId = mapRoleToId(role);

        if (roleId == -1) {
            response.getWriter().println("Invalid role selected.");
            return;
        }

        if (registerUser(firstName, lastName, username, email, hashedPassword, roleId)) {
            request.getSession().setAttribute("username", username);
            response.sendRedirect("home.jsp");
        } else {
            response.getWriter().println("Error: Unable to register user.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("register.jsp");
    }

    private boolean isInvalidInput(String firstName, String lastName, String password, String confirmPassword, String email, String role) {
        return firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty() ||
                email == null || email.isEmpty() ||
                role == null || role.isEmpty();
    }

    private boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }

    private String generateUniqueUsername(String firstName, String lastName) {
        String baseUsername = firstName.substring(0, 1).toLowerCase() + lastName.substring(0, Math.min(4, lastName.length())).toLowerCase();
        String username;
        int attempts = 0;

        do {
            String randomDigits = String.format("%03d", RANDOM.nextInt(1000));
            username = baseUsername + randomDigits;
            attempts++;
            if (attempts > 10) {
                return null; // Avoid infinite loops
            }
        } while (!isUsernameUnique(username));

        return username;
    }

    private boolean isUsernameUnique(String username) {
        String sql = "SELECT COUNT(*) FROM USER_ACC WHERE UNAME_DB = ?";

        try (Connection connection = DatabaseUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) == 0; // No matching username found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log the exception
        }

        return false;
    }

    private boolean registerUser(String firstName, String lastName, String username, String email, String hashedPassword, int roleId) throws ServletException {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String sql = "INSERT INTO USER_ACC (ROLE_ID_DB, FNAME_DB, LNAME_DB, UNAME_DB, PWORDHASH_DB, EMAIL_DB) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, roleId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, username); // Store the generated username
            stmt.setString(5, hashedPassword);
            stmt.setString(6, email);

            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database access error: " + e.getMessage(), e);
        }
    }

    private int mapRoleToId(String roleName) {
        switch (roleName) {
            case "Student":
                return 1;
            case "ProvServ Staff": // Fixed role name to match expected format
                return 2;
            case "Academic Staff":
                return 3;
            default:
                return -1;
        }
    }
}
