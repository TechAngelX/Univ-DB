package org.example.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.sql.*;

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

        // Convert to UPPERCASE
        fname = (fname != null) ? fname.toUpperCase() : "";
        lname = (lname != null) ? lname.toUpperCase() : "";

        // Retrieve integer parameters and handle parsing
        int accType = getParameterAsInt(request, "accType", 0);
        int degreetypeid = getParameterAsInt(request, "degreetypeid", 0);
        int studylevelid = getParameterAsInt(request, "studylevelid", 0);
        int progid = getParameterAsInt(request, "progid", 0);


        // Retrieve string parameters
        String staffroleid = getParameter(request, "staffroleid", "");
        String deptid = getParameter(request, "deptid", "");
        String awardname = getParameter(request, "awardname", "");

        // REGISTRATION FORM VALIDATION
        Map<String, String> errors = RegFormValidator.validateForm(fname, lname, pword, pwordConfirm);

        if (!errors.isEmpty()) {
            for (String error : errors.values()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, error);
                return;
            }
        }

        // Hash the password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);  // 12 is the cost factor
        String hashedPassword = encoder.encode(pword);

        // Determine the email domain based on the account type
        String email;
        if (accType == 1) { // Student
            email = generateAcctUsername(fname, lname) + emailStudDomain.toLowerCase();
        } else if (accType == 2) { // Staff
            email = generateAcctUsername(fname, lname) + emailStaffDomain.toLowerCase();
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid account type");
            return;
        }
        // SQL STATEMENT FOR INSERTION
        //--------------------------------------------------------------------------------------------------------------
        String sqlUserAcc = "INSERT INTO USER_ACC (FNAME, LNAME, EMAIL, USERNAME, PWORD, ACCTYPEID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        //When a new record is inserted into the USER_ACC table, the USERID is not provided directly by the client-side
        // form data. Instead, it is generated automatically by a server side database trigger. The trigger
        // (USER_ACC_TRIGGER) is defined to execute BEFORE a new row is inserted into the USER_ACC table. Its role is to
        // generate and assign aunique USERID to each new record. By using PreparedStatement with the getGeneratedKeys()
        // method, you can retrieve the generated USERID from the result set of the insert operation.

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmtUserAcc = conn.prepareStatement(sqlUserAcc, new String[] { "USERID" })) {

            // Set parameters
            stmtUserAcc.setString(1, fname);
            stmtUserAcc.setString(2, lname);
            stmtUserAcc.setString(3, email);
            stmtUserAcc.setString(4, generateAcctUsername(fname, lname));
            stmtUserAcc.setString(5, hashedPassword);
            stmtUserAcc.setInt(6, accType);

            // Execute the user account insertion
            int affectedRows = stmtUserAcc.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            // Retrieve the USERID
            long userid;
            try (ResultSet generatedKeys = stmtUserAcc.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userid = generatedKeys.getLong(1);
                    System.out.println("Generated USERID: " + userid);
                } else {
                    throw new SQLException("Creating user failed, no USERID obtained.");
                }
            }
            // STUDENT OR STAFF INPUT BASED ON ACCOUNT TYPE '1' OR '2'
            // --------------------------------------------------------
            if (accType == 1) { // Student
                String sqlInsertStudent = "INSERT INTO STUDENT (STUDENTID, FNAME, LNAME, EMAIL, PROGID, DEGREETYPEID, STUDYLEVELID, DEPTID, AWARDNAME, USERID) " +
                        "VALUES (TO_NUMBER('2' || student_id_seq.NEXTVAL), ?, ?,?,  ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement stmtStudent = conn.prepareStatement(sqlInsertStudent)) {
                    // Set the parameters for the student insert
                    stmtStudent.setString(1, fname);
                    stmtStudent.setString(2, lname);
                    stmtStudent.setString(3, email);
                    stmtStudent.setInt(4, progid);
                    stmtStudent.setInt(5, degreetypeid);
                    stmtStudent.setInt(6, studylevelid);
                    stmtStudent.setString(7, deptid);
                    stmtStudent.setString(8, awardname);
                    stmtStudent.setLong(9, userid );

                    stmtStudent.executeUpdate();
                }
            } else if (accType == 2) { // Staff
                String sqlInsertStaff = "INSERT INTO STAFF (STAFFID, STAFFROLEID, EMAIL, FNAME, LNAME, DEPTID, USERID) " +
                        "VALUES (generate_staff_id(?, ?), ?,?, ?, ?, ?, ?)"; // Server-Side generated. takes first initials of FNAME and LNAME

                try (PreparedStatement stmtInsertStaff = conn.prepareStatement(sqlInsertStaff)) {
                    // Set the parameters for the staff insert
                    stmtInsertStaff.setString(1, fname);
                    stmtInsertStaff.setString(2, lname);
                    stmtInsertStaff.setString(3, staffroleid);
                    stmtInsertStaff.setString(4, email);
                    stmtInsertStaff.setString(5, fname);
                    stmtInsertStaff.setString(6, lname);
                    stmtInsertStaff.setString(7, deptid);
                    stmtInsertStaff.setLong(8, userid);

                    stmtInsertStaff.executeUpdate();
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

    // Generates a unique account Username for login, based on the user's first and last names.
    // This is different from StudentID or StaffId.
    private String generateAcctUsername(String fname, String lname) {
        String firstInitial = !fname.isEmpty() ? String.valueOf(fname.charAt(0)) : "";
        String lastFour = lname.length() >= 5 ? lname.substring(0, 5) : lname;
        String randomNumbers;
        do {
            randomNumbers = String.format("%03d", random.nextInt(1000));
        } while (randomNumbers.startsWith("1") || randomNumbers.startsWith("0"));

        return (firstInitial.toLowerCase() + lastFour.toLowerCase() + randomNumbers);
    }
}

