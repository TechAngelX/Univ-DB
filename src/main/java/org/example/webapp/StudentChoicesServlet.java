package org.example.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/StudentChoicesServlet")
public class StudentChoicesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Load JSON file from the classpath
        InputStream inputStream = getServletContext().getResourceAsStream("/json/studentChoices.json");
        if (inputStream == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JSON file not found");
            return;
        }

        // Read the input stream
        StringBuilder jsonContent = new StringBuilder();
        try (java.util.Scanner scanner = new java.util.Scanner(inputStream, StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                jsonContent.append(scanner.nextLine());
            }
        }

        // Write JSON response
        PrintWriter out = response.getWriter();
        out.print(jsonContent.toString());
        out.flush();
    }
}
