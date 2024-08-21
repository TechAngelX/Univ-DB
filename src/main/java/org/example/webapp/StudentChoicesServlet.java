package org.example.webapp;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/StudentChoicesServlet")
public class StudentChoicesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Create JSON objects for degree types, departments, and programmes
        JSONObject json = new JSONObject();

        // Populate degree types
        JSONArray degreeTypes = new JSONArray();
        degreeTypes.put(new JSONObject().put("value", "BSc").put("text", "Bachelor of Science"));
        degreeTypes.put(new JSONObject().put("value", "BA").put("text", "Bachelor of Arts"));
        degreeTypes.put(new JSONObject().put("value", "MSc").put("text", "Master of Science"));
        degreeTypes.put(new JSONObject().put("value", "PhD").put("text", "Doctor of Philosophy"));

        // Populate departments
        JSONArray departments = new JSONArray();
        departments.put(new JSONObject().put("value", "CS").put("text", "Computer Science"));
        departments.put(new JSONObject().put("value", "ECE").put("text", "Electrical and Computer Engineering"));
        departments.put(new JSONObject().put("value", "BIO").put("text", "Biology"));

        // Populate programmes
        JSONObject programmes = new JSONObject();
        programmes.put("CS", new JSONArray().put("BSc Computer Science").put("MSc Computer Science"));
        programmes.put("ECE", new JSONArray().put("BSc Electrical Engineering").put("MSc Electrical Engineering"));
        programmes.put("BIO", new JSONArray().put("BSc Biology").put("PhD Biology"));

        // Add the arrays to the main JSON object
        json.put("degreeTypes", degreeTypes);
        json.put("departments", departments);
        json.put("programmes", programmes);

        // Write JSON response
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
