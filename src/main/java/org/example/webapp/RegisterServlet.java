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
import java.util.Map;
import java.util.Random;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private final Random random = new Random();
    private final String emailStudDomain = "@std.techangelx.ac.uk";
    private final String emailStaffDomain = "@techangelx.ac.uk";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve parameters and handle null or empty values
        String fname = getParameter(request, "fname");
        String lname = getParameter(request, "lname");
        String pword = getParameter(request, "pword");
        String pwordConfirm = getParameter(request, "pwordConfirm");

        // Retrieve integer parameters and handle parsing
        int accType = getParameterAsInt(request, "accType", 0);
        int degreetypeid = getParameterAsInt(request, "degreetypeid", 0);
        int studylevelid = getParameterAsInt(request, "studylevelid", 0);

        // Retrieve string parameters
        String staffroleid = getParameter(request, "staffroleid", "");
        String deptid = getParameter(request, "deptid", "");
        String progid = getParameter(request, "progid", "");


        // REGISTRATION FORM VALIDATION
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
        if (accType == 1) { // Student
            email = generateUsername(fname, lname) + emailStudDomain;
        } else if (accType == 2) { // Staff
            email = generateUsername(fname, lname) + emailStaffDomain;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid account type");
            return;
        }

        // SQL STATEMENTS FOR INSERTION
        String sqlUserAcc = "INSERT INTO USER_ACC (FNAME, LNAME, EMAIL, USERNAME, PWORD, ACCTYPEID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmtUserAcc = conn.prepareStatement(sqlUserAcc, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Insert into USER_ACC table
            stmtUserAcc.setString(1, fname);
            stmtUserAcc.setString(2, lname);
            stmtUserAcc.setString(3, email);
            stmtUserAcc.setString(4, generateUsername(fname, lname));
            stmtUserAcc.setString(5, hashedPassword);
            stmtUserAcc.setInt(6, accType);

            // Execute the user account insertion
            int rowsAffected = stmtUserAcc.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating user account failed, no rows affected.");
            }

//             Retrieve the generated user ID
//

            if (accType == 1) { // Student
                String sqlInsertStudent = "INSERT INTO STUDENT (STUDENTID, FNAME, LNAME, EMAIL, PROGID, DEGREETYPEID, STUDYLEVELID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmtStudent = conn.prepareStatement(sqlInsertStudent)) {
                    stmtStudent.setInt(1, generateStudentNumber());
                    stmtStudent.setString(2, fname);
                    stmtStudent.setString(3, lname);
                    stmtStudent.setString(4, email);
                    stmtStudent.setString(5, progid);
                    stmtStudent.setInt(6, degreetypeid);
                    stmtStudent.setInt(7, studylevelid);
//                    stmtStudent.setInt(8, userId);

                    stmtStudent.executeUpdate();
                }
            } else if (accType == 2) { // Staff
                String sqlInsertStaff = "INSERT INTO STAFF (STAFFID, STAFFROLEID, EMAIL, FNAME, LNAME, DEPTID,USERID) VALUES (?,?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmtInsertStaff = conn.prepareStatement(sqlInsertStaff)) {
                    stmtInsertStaff.setString(1, generateStaffID(fname, lname));
                    stmtInsertStaff.setString(2, staffroleid);
                    stmtInsertStaff.setString(3, email);
                    stmtInsertStaff.setString(4, fname);
                    stmtInsertStaff.setString(5, lname);
                    stmtInsertStaff.setString(6, deptid);
//                    stmtInsertStaff.setInt(7, userId);


                    int rowsAffectedStaff = stmtInsertStaff.executeUpdate();
                    if (rowsAffectedStaff == 0) {
                        throw new SQLException("Creating staff record failed, no rows affected.");
                    }
                }
            }

            // Redirect to a success page
            response.sendRedirect("home.jsp");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred during registration");
        }
    }

    // Helper method to retrieve string parameters
    private String getParameter(HttpServletRequest request, String name) {
        return getParameter(request, name, "");
    }

    // Overloaded helper method to retrieve string parameters with a default value
    private String getParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        return value != null ? value.trim() : defaultValue;
    }

    // Helper method to retrieve integer parameters with a default value
    private int getParameterAsInt(HttpServletRequest request, String name, int defaultValue) {
        String value = request.getParameter(name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    // Generates a unique username based on the user's first and last names
    private String generateUsername(String fname, String lname) {
        String firstInitial = !fname.isEmpty() ? String.valueOf(fname.charAt(0)) : "";
        String lastFour = lname.length() >= 4 ? lname.substring(0, 4) : lname;
        String randomNumbers;
        do {
            randomNumbers = String.format("%03d", random.nextInt(1000));
        } while (randomNumbers.startsWith("1") || randomNumbers.startsWith("0"));

        return (firstInitial + lastFour + randomNumbers).toLowerCase();
    }

    // Generates a unique staff ID based on the user's first and last names
    private String generateStaffID(String fname, String lname) {
        String firstInitial = !fname.isEmpty() ? String.valueOf(fname.charAt(0)) : "";
        String lastFour = lname.length() >= 4 ? lname.substring(0, 4) : lname;
        String randomNumbers;
        do {
            randomNumbers = String.format("%03d", random.nextInt(1000));
        } while (randomNumbers.startsWith("1") || randomNumbers.startsWith("0"));

        return (firstInitial + lastFour + randomNumbers).toLowerCase();
    }

    // Generates a random student number
    private int generateStudentNumber() {
        return random.nextInt((25168942 - 2001852) + 1) + 2001852;
    }
}