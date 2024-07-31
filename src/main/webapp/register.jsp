<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>User Registration | Uni-DB</title>
    <!-- Include Bootstrap CSS for styling -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body, html {
            height: 100%;
            margin: 0;
        }
        .form-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .form-box {
            width: 100%;
            max-width: 500px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        .text-danger {
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
<div class="container form-container">
    <div class="form-box">
        <h1 class="text-center">Register</h1>
        <p class="text-center">Please complete the form to create an account</p>
        <form action="registerServlet" method="post" onsubmit="return validateForm()">
            <!-- First Name field -->
            <div class="form-group">
                <label for="fName"><strong>First Name:</strong></label>
                <input class="form-control" type="text" name="fName" id="fName" required placeholder="Enter your first name">
            </div>

            <!-- Last Name field -->
            <div class="form-group">
                <label for="lName"><strong>Last Name:</strong></label>
                <input class="form-control" type="text" name="lName" id="lName" required placeholder="Enter your last name">
            </div>

            <!-- Email Address field -->
            <div class="form-group">
                <label for="email"><strong>Email:</strong></label>
                <input class="form-control" type="email" name="email" id="email" required placeholder="Enter your email address">
                <p id="email-validation-error" class="text-danger"></p>
            </div>

            <!-- Password field -->
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="password"><strong>Password:</strong></label>
                    <input class="form-control" type="password" name="password" id="password" required placeholder="Enter your password">
                </div>
                <div class="form-group col-md-6">
                    <label for="password-confirm"><strong>Confirm Password:</strong></label>
                    <input class="form-control" type="password" name="password-confirm" id="password-confirm" required placeholder="Confirm your password">
                </div>
            </div>
            <p id="password-mismatch-error" class="text-danger"></p>

            <!-- Sign Up button -->
            <button class="btn btn-primary btn-block" type="submit">Sign Up</button>
        </form>
        <!-- Already registered link -->
        <p class="text-center mt-3"><a href="login.jsp">Already have an account? Login here</a></p>
    </div>
</div>

<!-- Include Bootstrap and jQuery JavaScript -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

<script>
    function validateForm() {
        var password = $("#password").val();
        var confirmPassword = $("#password-confirm").val();
        var email = $("#email").val();
        var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

        // Check if passwords match
        if (password !== confirmPassword) {
            $("#password-mismatch-error").text("Passwords do not match");
            return false; // Prevent form submission
        } else {
            $("#password-mismatch-error").text("");
        }

        // Check if email is valid
        if (!emailPattern.test(email)) {
            $("#email-validation-error").text("Invalid email format");
            return false; // Prevent form submission
        } else {
            $("#email-validation-error").text("");
        }

        // If everything is valid
        return true;
    }
</script>
</body>
</html>
