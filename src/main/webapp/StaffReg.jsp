<!DOCTYPE html>
<html>
<head>
    <title>Staff Registration | Univ-DB</title>
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
        <h1 class="text-center">Staff Registration</h1>
        <p class="text-center">Please complete the form to create a staff account</p>
        <form action="${pageContext.request.contextPath}/StaffRegServlet" method="post" onsubmit="return validateForm()">
            <!-- First Name field -->
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="fname"><strong>First Name:</strong></label>
                    <input class="form-control" type="text" name="fname" id="fname" required placeholder="Enter your first name">
                </div>
                <!-- Last Name field -->
                <div class="form-group col-md-6">
                    <label for="lname"><strong>Last Name:</strong></label>
                    <input class="form-control" type="text" name="lname" id="lname" required placeholder="Enter your last name">
                </div>
            </div>

            <!-- Password field -->
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="pword"><strong>Password:</strong></label>
                    <input class="form-control" type="password" name="password" id="pword" required placeholder="Enter your password">
                </div>
                <div class="form-group col-md-6">
                    <label for="pwordConfirm"><strong>Confirm Password:</strong></label>
                    <input class="form-control" type="password" name="pwordConfirm" id="pwordConfirm" required placeholder="Confirm your password">
                </div>
            </div>

            <p id="password-mismatch-error" class="text-danger"></p>

            <!-- Email Address field -->
            <div class="form-group">
                <label for="email"><strong>Email:</strong></label>
                <input class="form-control" type="email" name="email" id="email" required placeholder="Enter your email address">
                <p id="email-validation-error" class="text-danger"></p>
            </div>

            <!-- Role field -->
            <div class="form-group">
                <label for="role"><strong>Role:</strong></label>
                <select class="form-control" name="role" id="role" required>
                    <option value="">Select a role</option>
                    <option value="ProfServ Staff">ProfServ Staff</option>
                    <option value="Academic Staff">Academic Staff</option>
                </select>
            </div>

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
        var password = $("#pword").val();
        var confirmPassword = $("#pwordConfirm").val();
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
