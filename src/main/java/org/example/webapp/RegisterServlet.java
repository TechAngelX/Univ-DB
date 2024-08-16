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
import java.util.Random;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private Random random = new Random();


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String email = request.getParameter("email");
        String pword = request.getParameter("pword");
        String pwordConfirm = request.getParameter("pwordConfirm");  // Retrieve password confirmation from request. Not entered into dbase.
        String accType = request.getParameter("accType");

        String username = generateUsername(fname,lname);
        // REGISTRATION FORM VALIDATION: -------------------------------------------------------------------------------
        Map<String, String> errors = RegFormValidator.validateForm(fname, lname, email, pword, pwordConfirm);

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
    private String generateUsername(String fname, String lname) {
        String firstInitial = !fname.isEmpty() ? String.valueOf(fname.charAt(0)) : "";
        String lastFour = lname.length() >= 4 ? lname.substring(0, 4) : lname;
        String randomNumbers;
        do {
            randomNumbers = String.format("%03d", random.nextInt(1000));
        } while (randomNumbers.startsWith("1") || randomNumbers.startsWith("0"));

        return (firstInitial + lastFour + randomNumbers).toLowerCase();
    }
}
