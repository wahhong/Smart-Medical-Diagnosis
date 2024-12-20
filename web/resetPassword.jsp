<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Reset Password</title>
        <link rel="icon" href="img/favicon.png">
        <link href="https://fonts.googleapis.com/css?family=Poppins:200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/bootstrap.min.css">
        <link rel="stylesheet" href="css/nice-select.css">
        <link rel="stylesheet" href="css/font-awesome.min.css">
        <link rel="stylesheet" href="css/icofont.css">
        <link rel="stylesheet" href="css/slicknav.min.css">
        <link rel="stylesheet" href="css/owl-carousel.css">
        <link rel="stylesheet" href="css/datepicker.css">
        <link rel="stylesheet" href="css/animate.min.css">
        <link rel="stylesheet" href="css/magnific-popup.css">
        <link rel="stylesheet" href="css/normalize.css">
        <link rel="stylesheet" href="css/style.css">
        <link rel="stylesheet" href="css/responsive.css">
    </head>
    <body>
        <div class="d-flex justify-content-center align-items-center" style="height: 100vh;">
            <div class="container">
                <div class="row">
                    <div class="col-lg-3 col-md-12 col-12"></div>
                    <div class="col-lg-6 col-md-12 col-12">
                        <div class="register">
                            <div class="inner">
                                <div class="register-form">
                                    <h2>Reset Password</h2>
                                    <%
                                        String patientID = request.getParameter("id");
                                        if (patientID == null) {
                                            response.sendRedirect("home.jsp");
                                        }
                                    %>
                                    <form action="ResetPassword?id=<%= patientID%>" method="post" class="form">
                                        <div class="row">
                                            <div class="col-lg-12 col-md-6 col-12">
                                                <div class="form-group">
                                                    <input type="password" name="password" placeholder="Password" required>
                                                </div>
                                                <div class="form-group">
                                                    <input type="password" name="confirmPassword" placeholder="Confirm Password" required>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-lg-12 col-md-6 col-1">
                                                <% if (request.getAttribute("errorPassword") != null) {%>
                                                <div class="alert alert-danger">
                                                    <%= request.getAttribute("errorPassword")%>
                                                </div>
                                                <% }%>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-lg-12 col-md-6 col-12 form-group login-btn">
                                                <button type="submit" class="btn" style="background-color: #1A76D1;">Submit</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-6 col-md-12 col-12"></div>
                </div>
            </div>
        </div>

        <script src="js/jquery.min.js"></script>
        <script src="js/jquery-migrate-3.0.0.js"></script>
        <script src="js/jquery-ui.min.js"></script>
        <script src="js/easing.js"></script>
        <script src="js/colors.js"></script>
        <script src="js/popper.min.js"></script>
        <script src="js/bootstrap-datepicker.js"></script>
        <script src="js/jquery.nav.js"></script>
        <script src="js/slicknav.min.js"></script>
        <script src="js/jquery.scrollUp.min.js"></script>
        <script src="js/niceselect.js"></script>
        <script src="js/tilt.jquery.min.js"></script>
        <script src="js/owl-carousel.js"></script>
        <script src="js/jquery.counterup.min.js"></script>
        <script src="js/steller.js"></script>
        <script src="js/wow.min.js"></script>
        <script src="js/jquery.magnific-popup.min.js"></script>
        <script src="http://cdnjs.cloudflare.com/ajax/libs/waypoints/2.0.3/waypoints.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/main.js"></script>
    </body>
</html>
