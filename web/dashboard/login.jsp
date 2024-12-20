<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String errorLogin = (String) request.getAttribute("errorLogin");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Login</title>
        <link rel="icon" href="assets/img/kaiadmin/favicon.ico" type="image/x-icon"/>
        <script src="assets/js/plugin/webfont/webfont.min.js"></script>
        <script>
            WebFont.load({
                google: {"families": ["Public Sans:300,400,500,600,700"]},
                custom: {"families": ["Font Awesome 5 Solid", "Font Awesome 5 Regular", "Font Awesome 5 Brands", "simple-line-icons"], urls: ['assets/css/fonts.min.css']},
                active: function () {
                    sessionStorage.fonts = true;
                }
            });
        </script>
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/plugins.min.css">
        <link rel="stylesheet" href="assets/css/kaiadmin.min.css">
    </head>
    <body class="bg-light">
        <div class="container d-flex justify-content-center align-items-center" style="min-height: 100vh;">
            <div class="card p-4 shadow" style="width: 100%; max-width: 400px;">
                <img src="../img/logo.png" alt="navbar brand" class="mb-4">
                <h2 class="text-center mb-4">Admin Login</h2>
                <form action="AdminLogin" method="post">
                    <div class="mb-5">
                        <label for="email">Email</label>
                        <input type="text" class="form-control mb-2" name="email" placeholder="Enter Email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : ""%>"/>
                    
                        <label for="password">Password</label>
                        <input type="password" class="form-control" name="password" placeholder="Enter Password"/>
                    </div>
                    <% if (errorLogin != null) {%>
                    <div class="alert alert-danger"><%= errorLogin%></div>
                    <% }%>
                    <div class="mt-4">
                        <button type="submit" class="btn btn-primary w-100">Login</button>
                    </div>
                </form>
            </div>
        </div>
    </body>
</html>
