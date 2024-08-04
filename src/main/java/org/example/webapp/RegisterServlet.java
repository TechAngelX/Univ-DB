package org.example.webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form data
        String firstName = request.getParameter("fName");
        String lastName = request.getParameter("lName");
        String password = request.getParameter("pWord");
        String confirmPassword = request.getParameter("pword-confirm");
        String email = request.getParameter("email");

        // Validate form data
//        if (firstName == null || firstName.isEmpty() ||
//            lastName == null || lastName.isEmpty() ||
//            password == null || password.isEmpty() ||
//            confirmPassword == null || confirmPassword.isEmpty() ||
//            email == null || email.isEmpty()) {
//            response.getWriter().println("All fields are required.");
//            return;
//        }

//        // Validate email format
//        Pattern emailPattern = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
//        Matcher emailMatcher = emailPattern.matcher(email);
//        if (!emailMatcher.matches()) {
//            response.getWriter().println("Invalid email format.");
//            return;
//        }

//        // Check if passwords match
//        if (!password.equals(confirmPassword)) {
//            response.getWriter().println("Passwords do not match.");
//            return;
//        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // Insert data into the database
        try (Connection connection = DatabaseUtils.getConnection()) {
            String sql = "INSERT INTO USER_ACCOUNT (FNAME_DB, LNAME_DB, PWORDHASH_DB, EMAIL_DB) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, email);

            int result = stmt.executeUpdate();

            if (result > 0) {
                // Registration successful, set session and redirect
                request.getSession().setAttribute("username", firstName);
                response.sendRedirect("home.jsp"); // Redirect to the target page
            } else {
                response.getWriter().println("Error: Unable to submit user.");
            }
        } catch (SQLException e) {
            throw new ServletException("Database access error", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("register.jsp"); // Display the registration page
    }
}
