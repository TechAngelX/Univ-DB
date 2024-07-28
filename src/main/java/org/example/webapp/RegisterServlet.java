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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        try (Connection conn = DatabaseUtils.getConnection()) {
            String sql = "INSERT INTO USER_ACCOUNT (userName, hashedPword, email) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // Hash password in real applications
                stmt.setString(3, email);
                int rowsInserted = stmt.executeUpdate();
                if (rowsInserted > 0) {
                    response.sendRedirect("login.jsp");
                } else {
                    response.sendRedirect("register.jsp?error=Registration failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("register.jsp?error=Registration failed");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}
