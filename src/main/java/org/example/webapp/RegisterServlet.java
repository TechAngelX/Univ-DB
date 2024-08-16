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
import java.util.Map;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String pword = request.getParameter("pword");
        String pwordConfirm = request.getParameter("pwordConfirm");  // Retrieve password confirmation from request. Not entered into dbase.
        String accType = request.getParameter("accType");

        // REGISTRATION FORM VALIDATION: -------------------------------------------------------------------------------
        Map<String, String> errors = RegFormValidator.validateForm(fname, lname, email, username, pword, pwordConfirm);

        // If there are validation errors, send a response with the first error found
        if (!errors.isEmpty()) {
            for (String error : errors.values()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, error);
                return;
            }
        }
        // -----------------------------------------------------------------------------------------------------

        // Hash the password
        String hashedPassword = BCrypt.hashpw(pword, BCrypt.gensalt());

        // SQL statement to insert user into
        // USER_ACC table
        String sqlUserAcc = "INSERT INTO USER_ACC (FNAME, LNAME, EMAIL, USERNAME ,PWORD, ACCTYPEID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmtUserAcc = conn.prepareStatement(sqlUserAcc)) {

            // Set parameters
            stmtUserAcc.setString(1, fname);
            stmtUserAcc.setString(2, lname);
            stmtUserAcc.setString(3, email);
            stmtUserAcc.setString(4, username);
            stmtUserAcc.setString(5, hashedPassword);
            stmtUserAcc.setInt(6, Integer.parseInt(accType)); // Handle conversion from string to integer



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
