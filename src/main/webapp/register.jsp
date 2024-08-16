<!DOCTYPE html>
<html>
<head>
    <title>Registration | Univ-DB</title>
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
        <h1 class="text-center">Univ-DB</h1>
        <form id="registrationForm" action="RegisterServlet" method="POST">
            <h3 class="text-center">Registration</h3>
            <p class="text-center">Please complete the form to create an account</p>


            <!-- First Name field -->
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="fname"><strong>First Name:</strong></label>
                    <input class="form-control" type="text" name="fname" id="fname" placeholder="Enter your first name">
                    <p id="fname-error" class="text-danger"></p>
                </div>
                <div class="form-group col-md-6">
                    <label for="lname"><strong>Last Name:</strong></label>
                    <input class="form-control" type="text" name="lname" id="lname" placeholder="Enter your last name">
                    <p id="lname-error" class="text-danger"></p>
                </div>
            </div>

            <!-- Password field -->
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="pword"><strong>Password:</strong></label>
                    <input class="form-control" type="password" name="pword" id="pword" placeholder="Enter your password">
                    <p id="pword-error" class="text-danger"></p>
                </div>
                <div class="form-group col-md-6">
                    <label for="pwordConfirm"><strong>Confirm Password:</strong></label>
                    <input class="form-control" type="password" name="pwordConfirm" id="pwordConfirm" placeholder="Confirm your password">
                    <p id="pwordConfirm-error" class="text-danger"></p>
                </div>
            </div>

            <!-- Email Address field -->
            <div class="form-group">
                <label for="email"><strong>Email:</strong></label>
                <input class="form-control" type="email" name="email" id="email" placeholder="Enter your email address">
                <p id="email-error" class="text-danger"></p>
            </div>

            <!-- Student or Staff Choice -->
            <div class="form-group">
                <label for="accType">Register as:</label>
                <select class="form-control" id="accType" name="accType" required>
                    <option value="">Select User Type</option>
                    <option value="1">Student</option>
                    <option value="2">Staff</option>
                </select>
                <p id="accType-error" class="text-danger"></p>
            </div>

            <button type="submit" class="btn btn-primary btn-block">Register</button>
        </form>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const form = document.getElementById("registrationForm");

        form.addEventListener("submit", function(event) {
            const fname = document.getElementById("fname").value.trim();
            const lname = document.getElementById("lname").value.trim();
            const email = document.getElementById("email").value.trim();
            const password = document.getElementById("pword").value.trim();
            const passwordConfirm = document.getElementById("pwordConfirm").value.trim();

            let isValid = true;

            // Reset all error messages
            document.getElementById("fname-error").textContent = "";
            document.getElementById("lname-error").textContent = "";
            document.getElementById("email-error").textContent = "";
            document.getElementById("pword-error").textContent = "";
            document.getElementById("pwordConfirm-error").textContent = "";

            // First name validation
            if (fname === "" || containsSpaces(fname)) {
                document.getElementById("fname-error").textContent = "First name cannot be empty or contain spaces.";
                isValid = false;
            }

            // Last name validation
            if (lname === "" || containsSpaces(lname)) {
                document.getElementById("lname-error").textContent = "Last name cannot be empty or contain spaces.";
                isValid = false;
            }

            // Email validation
            if (email === "" || containsSpaces(email)) {
                document.getElementById("email-error").textContent = "Email cannot be empty or contain spaces.";
                isValid = false;
            }


            // Password validation
            if (password === "" || containsSpaces(password)) {
                document.getElementById("pword-error").textContent = "Password cannot be empty or contain spaces.";
                isValid = false;
            }

            // Password confirmation validation
            if (passwordConfirm === "" || containsSpaces(passwordConfirm)) {
                document.getElementById("pwordConfirm-error").textContent = "Password confirmation cannot be empty or contain spaces.";
                isValid = false;
            }

            // Check if passwords match
            if (password !== passwordConfirm) {
                document.getElementById("pwordConfirm-error").textContent = "Passwords do not match.";
                isValid = false;
            }

            // Prevent form submission if there are validation errors
            if (!isValid) {
                event.preventDefault();
            }
        });

        // Helper function to check if a string contains spaces
        function containsSpaces(value) {
            return /\s/.test(value);
        }
    });
</script>

</body>
</html>
