import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/registerServlet")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve form parameters
        String fName = request.getParameter("fName");
        String lName = request.getParameter("lName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("password-confirm");

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Generate username
        String username = generateUserName(fName, lName);

        // Check if username exists
        if (checkUserName(username)) {
            username += "1"; // Modify username if it exists
        }

        // Insert user into the database
        try {
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String sql = "INSERT INTO USER_ACCOUNT (userName, fName, lName, email, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, fName);
            stmt.setString(3, lName);
            stmt.setString(4, email);
            stmt.setString(5, password);

            int result = stmt.executeUpdate();
            conn.close();

            if (result > 0) {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                response.sendRedirect("session.jsp"); // Redirect to success page
            } else {
                request.setAttribute("errorMessage", "Error: Unable to submit user.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database error: " + e.getMessage());
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }

    private String generateUserName(String fName, String lName) {
        return fName.toLowerCase().charAt(0) + lName.substring(Math.max(0, lName.length() - 7)).toLowerCase();
    }

    private boolean checkUserName(String username) {
        boolean exists = false;
        try {
            String dbUrl = System.getenv("DB_URL");
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");
            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            String query = "SELECT COUNT(*) FROM USER_ACCOUNT WHERE userName = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                exists = count > 0;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
}
