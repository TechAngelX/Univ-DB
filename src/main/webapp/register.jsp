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
    </style>
</head>
<body>
<div class="container form-container">
    <div class="form-box">
        <h1 class="text-center">Univ-DB</h1>
        <form action="RegisterServlet" method="post">
            <h3 class="text-center">Registration</h3>
            <p class="text-center">Please complete the form to create an account</p>

            <!-- Username field -->
            <div class="form-group">
                <label for="username"><strong>Username:</strong></label>
                <input class="form-control" type="text" name="username" id="username" required placeholder="Enter your username">
            </div>

            <!-- Password field -->
            <div class="form-group">
                <label for="password"><strong>Password:</strong></label>
                <input class="form-control" type="password" name="password" id="password" required placeholder="Enter your password">
            </div>

            <button type="submit" class="btn btn-primary btn-block">Register</button>
        </form>
    </div>
</div>
</body>
</html>
