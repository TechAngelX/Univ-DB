<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Univ-DB</title>
    <!-- Include Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        body, html {
            height: 100%;
            margin: 0;
        }
        .container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .welcome-box {
            width: 100%;
            max-width: 600px;
            padding: 20px;
            background: #f8f9fa;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
<div class="container">
    <div class="welcome-box">
        <h1 class="text-center">Welcome to Uni-DB Application, the University login marking academic portal</h1>
        <p class="text-center">Please choose an option:</p>
        <div class="text-center">
            <a class="btn btn-primary" href="login.jsp">Login</a>
            <a class="btn btn-secondary" href="register.jsp">Register</a>
        </div>
    </div>
</div>

<!-- Include Bootstrap and jQuery JavaScript -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
