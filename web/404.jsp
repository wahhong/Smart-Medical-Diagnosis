<%-- 
    Document   : 404
    Created on : Oct 1, 2024, 12:17:33 PM
    Author     : chanw
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="icon" href="img/favicon.png">
        <script src="js/jquery.min.js"></script>
        <title>404 - Page Not Found</title>
        <!-- Bootstrap CSS -->
        <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                background-color: #f8f9fa;
            }
            .error-page {
                text-align: center;
                background-color: #fff;
                padding: 50px;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            }
            .error-page h1 {
                font-size: 150px;
                font-weight: bold;
                color: #dc3545;
            }
            .error-page p {
                font-size: 20px;
                color: #6c757d;
            }
            .btn-custom {
                font-size: 16px;
                padding: 10px 20px;
                background-color: #007bff;
                color: #fff;
                border-radius: 5px;
                text-decoration: none;
            }
            .btn-custom:hover {
                background-color: #000;
                color: #fff;
            }
        </style>
    </head>
    <body>

        <div class="error-page">
            <h1>404</h1>
            <p>Oops! The page you're looking for can't be found.</p>
        </div>

        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.2/dist/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    </body>
</html>
