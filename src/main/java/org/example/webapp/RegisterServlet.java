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
    private Random random = new Random();

    private final String emailStudDomain = "@std.techangelx.ac.uk";
    private final String emailStaffDomain = "@techangelx.ac.uk";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve parameters and handle null or empty values
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String pword = request.getParameter("pword");
        String pwordConfirm = request.getParameter("pwordConfirm");
        String accType = request.getParameter("accType"); // Holds in the Account table the type of user: 1 for student, 2 for staff
        String staffrole = request.getParameter("staffrole"); // 5 character VARCHAR for staff role
        String degreetypeid = request.getParameter("degreetypeid"); // Hardcoded value for student (BA, MSc etc)
        String progid = request.getParameter("progid"); // Hardcoded value for the programme (Marketing, Computer Science etc)
        String studylevelid = request.getParameter("studylevelid"); // Hardcoded study level id for students

        // Handle possible null values for parameters
        fname = fname != null ? fname.trim() : "";
        lname = lname != null ? lname.trim() : "";
        pword = pword != null ? pword.trim() : "";
        pwordConfirm = pwordConfirm != null ? pwordConfirm.trim() : "";
        accType = accType != null ? accType.trim() : "";
        staffrole = staffrole != null ? staffrole.trim() : "";
        degreetypeid = degreetypeid != null ? degreetypeid.trim() : "0";
        progid = progid != null ? progid.trim() : "0";
        studylevelid = studylevelid != null ? studylevelid.trim() : "0";

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

        // SQL STATEMENTS FOR INSERTION
        //-------------------------------------------------------------------------------------------------------------------------
        String sqlUserAcc = "INSERT INTO USER_ACC (FNAME, LNAME, EMAIL, USERNAME, PWORD, ACCTYPEID) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmtUserAcc = conn.prepareStatement(sqlUserAcc, PreparedStatement.RETURN_GENERATED_KEYS)) {

            // Insert into USER_ACC table
            stmtUserAcc.setString(1, fname);
            stmtUserAcc.setString(2, lname);
            stmtUserAcc.setString(3, email);
            stmtUserAcc.setString(4, username);
            stmtUserAcc.setString(5, hashedPassword);
            stmtUserAcc.setInt(6, Integer.parseInt(accType));

            // Execute the user account insertion
            stmtUserAcc.executeUpdate();

            // Retrieve the generated user ID
            ResultSet generatedKeys = stmtUserAcc.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            int userId = generatedKeys.getInt(1);

            if ("1".equals(accType)) { // Student
                // Prepare SQL for inserting a student
                String sqlInsertStudent = "INSERT INTO STUDENT (STUDENTID, FNAME, LNAME, EMAIL, PROGID, DEGREETYPEID, STUDYLEVELID, USERID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmtStudent = conn.prepareStatement(sqlInsertStudent)) {
                    stmtStudent.setInt(1, generateStudentNumber()); // Generate a student number
                    stmtStudent.setString(2, fname);
                    stmtStudent.setString(3, lname);
                    stmtStudent.setString(4, email);
                    stmtStudent.setInt(5, Integer.parseInt(progid));  // Use hardcoded PROGID
                    stmtStudent.setInt(6, Integer.parseInt(degreetypeid));  // Use hardcoded DEGREETYPEID
                    stmtStudent.setInt(7, Integer.parseInt(studylevelid));  // Use hardcoded STUDYLEVELID
                    stmtStudent.setInt(8, userId); // Link student record to the account user

                    stmtStudent.executeUpdate();
                }
            } else if ("2".equals(accType)) { // Staff
                // Prepare SQL for inserting a staff role
                String sqlInsertStaff = "INSERT INTO STAFF (STAFFID, USERID, FNAME, LNAME, EMAIL, STAFFROLEID, DEPTID) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmtInsertStaff = conn.prepareStatement(sqlInsertStaff)) {
                    stmtInsertStaff.setString(1, generateUsername(fname, lname)); // Generate a staff ID
                    stmtInsertStaff.setInt(2, userId); // Link staff record to the account user
                    stmtInsertStaff.setString(3, fname);
                    stmtInsertStaff.setString(4, lname);
                    stmtInsertStaff.setString(5, email);
                    stmtInsertStaff.setString(6, staffrole); // Insert the staff role ID

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

    private String generateUsername(String fname, String lname) {
        String firstInitial = !fname.isEmpty() ? String.valueOf(fname.charAt(0)) : "";
        String lastFour = lname.length() >= 4 ? lname.substring(0, 4) : lname;
        String randomNumbers;
        do {
            randomNumbers = String.format("%03d", random.nextInt(1000));
        } while (randomNumbers.startsWith("1") || randomNumbers.startsWith("0"));

        return (firstInitial + lastFour + randomNumbers).toLowerCase();
    }

    private int generateStudentNumber() {
        return random.nextInt((25168942 - 2001852) + 1) + 2001852;
    }
}
