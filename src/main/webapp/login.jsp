<!DOCTYPE html>
<html>
<head>
    <title>Login | Univ-DB</title>
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
        .text-success {
            font-size: 0.875rem;
        }
    </style>
</head>
<body>
<div class="container form-container">
    <div class="form-box">
        <h1 class="text-center">Login</h1>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <!-- Username field -->
            <div class="form-group">
                <label for="uname"><strong>Username:</strong></label>
                <input class="form-control" type="text" name="uname" id="uname" required placeholder="Enter your username">
            </div>

            <!-- Password field -->
            <div class="form-group">
                <label for="pword"><strong>Password:</strong></label>
                <input class="form-control" type="password" name="pword" id="pword" required placeholder="Enter your password">
            </div>

            <!-- Error and Success Messages -->
            <div class="form-group">
                <% if (request.getParameter("error") != null) { %>
                <p class="text-danger">Invalid username or password</p>
                <% } %>
                <% if (request.getParameter("message") != null) { %>
                <p class="text-success">Registration successful. Please login.</p>
                <% } %>
            </div>

            <!-- Login button -->
            <button class="btn btn-primary btn-block" type="submit">Login</button>
        </form>

        <!-- Registration link -->
        <p class="text-center mt-3"><a href="register.jsp">Don't have an account? Register here</a></p>
    </div>
</div>

<!-- Include Bootstrap and jQuery JavaScript -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.9.3/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
