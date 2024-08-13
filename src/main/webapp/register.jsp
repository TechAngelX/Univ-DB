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
        <form action="RegisterServlet" method="post">
            <!-- Common fields -->
            <div class="form-box">
                <h3 class="text-center">Registration</h3>
                <p class="text-center">Please complete the form to create an account</p>

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
                        <label for="password"><strong>Password:</strong></label>
                        <input class="form-control" type="password" name="password" id="password" required placeholder="Enter your password">
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

                <!-- Role or Programme -->
                <div class="form-group">
                    <label for="userType">Register as:</label>
                    <select class="form-control" id="userType" name="userType" required>
                        <option value="">Select User Type</option>
                        <option value="student">Student</option>
                        <option value="staff">Staff</option>
                    </select>
                </div>

                <!-- Student-specific fields -->
                <div class="form-group" id="progDiv" style="display: none;">
                    <label for="prog_id">Programme:</label>
                    <select class="form-control" id="prog_id" name="prog_id">
                        <option value="">Select a programme</option>
                        <option value="MSc_Computer_Science">MSc Computer Science</option>
                        <option value="BSc_Mathematics">BSc Mathematics</option>
                        <!-- Add more options as needed -->
                    </select>
                </div>

                <!-- Staff-specific fields -->
                <div class="form-group" id="roleDiv" style="display: none;">
                    <label for="role">Role:</label>
                    <select class="form-control" id="role" name="role">
                        <option value="">Select a role</option>
                        <option value="Academic_Staff">Academic Staff</option>
                        <option value="Professional_Services">Professional Services</option>
                        <!-- Add more options as needed -->
                    </select>
                </div>

                <button type="submit" class="btn btn-primary btn-block">Register</button>
            </div>
        </form>
    </div>
</div>

<script>
    document.getElementById('userType').addEventListener('change', function() {
        var userType = this.value;
        document.getElementById('progDiv').style.display = userType === 'student' ? 'block' : 'none';
        document.getElementById('roleDiv').style.display = userType === 'staff' ? 'block' : 'none';
    });
</script>
</body>
</html>
