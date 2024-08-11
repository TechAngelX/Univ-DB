package org.example.webapp;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Handle POST requests for logout
        // Invalidate the session to log the user out
        request.getSession().invalidate();
        response.sendRedirect("login.jsp"); // Redirect to login page after logout
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Handle GET requests for logout
        // Invalidate the session to log the user out
        request.getSession().invalidate();
        response.sendRedirect("login.jsp"); // Redirect to login page after logout
    }
}
