<!DOCTYPE html>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
<h1>Login</h1>
<form action="login" method="post">
    Email: <input type="text" name="email" required /><br>
    Password: <input type="password" name="password" required /><br>
    <input type="submit" value="Login" />
</form>
<% if (request.getParameter("error") != null) { %>
<p style="color: red;">Invalid email or password</p>
<% } %>
<% if (request.getParameter("message") != null) { %>
<p style="color: green;">Registration successful. Please login.</p>
<% } %>
</body>
</html>
