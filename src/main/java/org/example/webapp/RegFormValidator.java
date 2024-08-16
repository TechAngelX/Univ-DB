package org.example.webapp;

import java.util.HashMap;
import java.util.Map;

public class RegFormValidator {

    // Validates the entire form and returns a map of errors
    public static Map<String, String> validateForm(String fname, String lname, String email, String username, String pword, String pwordConfirm) {
        Map<String, String> errors = new HashMap<>();

        // Validate first name
        if (!validateFirstName(fname)) {
            errors.put("fname", "First name cannot be empty or contain spaces. Press back button and start again.");
        }

        // Validate last name
        if (!validateLastName(lname)) {
            errors.put("lname", "Last name cannot be empty or contain spaces. Press back button and start again.");
        }

        // Validate email
        if (!validateEmail(email)) {
            errors.put("email", "Email cannot be empty or contain spaces. Press back button and start again.");
        }

        // Validate username
        if (!validateUsername(username)) {
            errors.put("username", "Username cannot be empty or contain spaces. Press back button and start again.");
        }

        // Validate password
        if (!validatePassword(pword)) {
            errors.put("pword", "Password cannot be empty or contain spaces. Press back button and start again.");
        }

        // Validate password confirmation
        if (!validatePasswordConfirmation(pword, pwordConfirm)) {
            errors.put("pwordConfirm", "Passwords do not match. Press back button and start again.");
        }

        return errors;
    }

    // Validates the first name
    public static boolean validateFirstName(String fname) {
        return fname != null && !fname.trim().isEmpty() && !containsSpaces(fname);
    }

    // Validates the last name
    public static boolean validateLastName(String lname) {
        return lname != null && !lname.trim().isEmpty() && !containsSpaces(lname);
    }

    // Validates the email
    public static boolean validateEmail(String email) {
        return email != null && !email.trim().isEmpty() && !containsSpaces(email);
    }

    // Validates the username
    public static boolean validateUsername(String username) {
        return username != null && !username.trim().isEmpty() && !containsSpaces(username);
    }

    // Validates the password
    public static boolean validatePassword(String pword) {
        return pword != null && !pword.trim().isEmpty() && !containsSpaces(pword);
    }

    // Validates that the password and confirmation match
    public static boolean validatePasswordConfirmation(String pword, String pwordConfirm) {
        return pword != null && pwordConfirm != null && pword.equals(pwordConfirm);
    }

    // Helper method to check if a string contains spaces
    private static boolean containsSpaces(String value) {
        return value.contains(" ");
    }
}
