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

    private final String emailStudDomain = "@std.techangelx.ac.uk";
    private final String emailStaffDomain = "@techangelx.ac.uk";
    private int minStudNum = 2001852; // Minimum for student number generator
    private int maxStudNum = 25168942; // Minimum for student number generator

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String pword = request.getParameter("pword");
        String pwordConfirm = request.getParameter("pwordConfirm");
        String accType = request.getParameter("accType");
        String studylevel = request.getParameter("studylevel");
        String staffrole = request.getParameter("staffrole");

        String username = generateUsername(fname, lname);

        // REGISTRATION FORM VALIDATION
        //-------------------------------------------------------------------------------------------------------------------------
        Map<String, String> errors = RegFormValidator.validateForm(fname, lname, pword, pwordConfirm);

        if (!errors.isEmpty()) {
            for (String error : errors.values()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, error);
                return;
            }
        }

        // Hash the password
        String hashedPassword = BCrypt.hashpw(pword, BCrypt.gensalt());

        // Determine the email domain based on the account type
        String email;
        if ("1".equals(accType)) { // Student
            email = username + emailStudDomain;
        } else if ("2".equals(accType)) { // Staff
            email = username + emailStaffDomain;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid account type");
            return;
        }

        // SQL STATEMENT TO INSERT USER INTO USER_ACC TABLE
        //-------------------------------------------------------------------------------------------------------------------------

        String sqlUserAcc = "INSERT INTO USER_ACC (FNAME, LNAME, EMAIL, USERNAME, PWORD, ACCTYPEID, STUDYLEVELID, STAFFROLEID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmtUserAcc = conn.prepareStatement(sqlUserAcc)) {

            // Set parameters
            stmtUserAcc.setString(1, fname);
            stmtUserAcc.setString(2, lname);
            stmtUserAcc.setString(3, email);
            stmtUserAcc.setString(4, username);
            stmtUserAcc.setString(5, hashedPassword);
            stmtUserAcc.setInt(6, Integer.parseInt(accType)); // Handle conversion from string to integer

            if ("1".equals(accType)) { // Student
                stmtUserAcc.setInt(7, Integer.parseInt(studylevel)); // Set study level
                stmtUserAcc.setNull(8, java.sql.Types.VARCHAR); // Set STAFFROLEID to NULL
            } else if ("2".equals(accType)) { // Staff
                stmtUserAcc.setNull(7, java.sql.Types.INTEGER); // Set STUDYLEVELID to NULL
                stmtUserAcc.setString(8, staffrole); // Set staff role as a string
            }


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

    private static int generateStudentNumber(int minStudNum, int maxStudNum) {
        Random random = new Random();
        return random.nextInt((maxStudNum - minStudNum) + 1) + minStudNum;

    }
}
