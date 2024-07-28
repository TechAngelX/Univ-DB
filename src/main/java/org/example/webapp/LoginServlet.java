package org.example.webapp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DatabaseUtils.getConnection()) {
            String sql = "SELECT * FROM USER_ACCOUNT WHERE userName = ? AND hashedPword = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // Hash password in real applications
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("username", username);
                        response.sendRedirect("welcome.jsp");
                    } else {
                        response.sendRedirect("login.jsp?error=Invalid username or password");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Login failed");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
