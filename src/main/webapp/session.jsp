<!DOCTYPE html>
<html>
<head>
    <title>Session Page</title>
</head>
<body>
<h1>Welcome, <%= session.getAttribute("user") %></h1>
<a href="index.jsp">Home</a>
<a href="logout.jsp">Logout</a>
</body>
</html>
