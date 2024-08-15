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
            <h3 class="text-center">Registration</h3>
            <p class="text-center">Please complete the form to create an account</p>

            <!-- Username field -->
            <div class="form-row">
                <div class="form-group col-md-6">
                    <label for="username"><strong>Username:</strong></label>
                    <input class="form-control" type="text" name="username" id="username" required placeholder="Enter your username">
                </div>


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
                <button type="submit" class="btn btn-primary btn-block">Register</button>
        </form>
    </div>
</div>


