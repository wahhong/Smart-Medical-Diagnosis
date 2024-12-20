<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Integer userAuthID = (Integer) session.getAttribute("userAuthID");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="https://fonts.googleapis.com/css?family=Poppins:200i,300,300i,400,400i,500,500i,600,600i,700,700i,800,800i,900,900i&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
        <div class="preloader">
            <div class="loader">
                <div class="loader-outter"></div>
                <div class="loader-inner"></div>
                <div class="indicator"> 
                    <svg width="16px" height="12px">
                    <polyline id="back" points="1 6 4 6 6 11 10 1 12 6 15 6"></polyline>
                    <polyline id="front" points="1 6 4 6 6 11 10 1 12 6 15 6"></polyline>
                    </svg>
                </div>
            </div>
        </div>

        <header class="header" >
            <div class="topbar">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-6 col-md-5 col-12">
                            <ul class="top-link"> 
                                <li><a href="doctors.jsp">Doctors</a></li>
                                <li><a href="services">Services</a></li>
                                <li><a href="contact-us.jsp">Contact</a></li>
                            </ul>
                        </div>
                        <div class="col-lg-6 col-md-7 col-12">
                            <ul class="top-contact">
                                <li><i class="fa fa-phone"></i><a href="https://wa.me/+601136662982">+60123456789</a></li>
                                <li><i class="fa fa-envelope"></i><a href="mailto:support@yourmail.com">chanwahhong827@gmail.com</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="header-inner">
                <div class="container">
                    <div class="inner">
                        <div class="row">
                            <div class="col-lg-3 col-md-3 col-12">
                                <!-- Start Logo -->
                                <div class="logo">
                                    <a href="index.html"><img src="img/logo.png" alt="#"></a>
                                </div>
                                <div class="mobile-nav"></div>
                            </div>
                            <%
                                String currentPage = request.getRequestURI();
                            %>

                            <div class="col-lg-7 col-md-7 col-12">
                                <div class="main-menu">
                                    <nav class="navigation">
                                        <ul class="nav menu">
                                            <li class="<%= currentPage.contains("home.jsp") ? "active" : ""%>">
                                                <a href="home.jsp">Home</a>
                                            </li>
                                            <li class="<%= currentPage.contains("doctors.jsp") ? "active" : ""%>">
                                                <a href="doctors.jsp">Doctors</a>
                                            </li>
                                            <li class="<%= currentPage.contains("services.jsp") ? "active" : ""%>">
                                                <a href="services.jsp">Services</a>
                                            </li>
                                            <li class="<%= currentPage.contains("appointment.jsp") || currentPage.contains("pickDoctor.jsp") || currentPage.contains("pickDatetime.jsp") || currentPage.contains("aiDiagnosis.jsp") || currentPage.contains("listAiDiagnosis.jsp") || currentPage.contains("payment.jsp") ? "active" : ""%>">
                                                <a href="appointment.jsp">Appointment</a>
                                            </li>
                                            <li class="<%= currentPage.contains("contact-us.jsp") ? "active" : ""%>">
                                                <a href="contact-us.jsp">Contact Us</a>
                                            </li>

                                        </ul>
                                    </nav>
                                </div>
                            </div>
                            <div class="col-lg-2 col-md-2 col-12">
                                <% if (userAuthID == null) { %>
                                <div class="get-quote">
                                    <a href="login.jsp" class="btn">Login</a>
                                </div>
                                <% } else {%>
                                <div class="parent-container">
                                    <div class="main-menu">
                                        <nav class="navigation">
                                            <ul class="nav">
                                                <li><a><i class="fa fa-user fa-class"></i></a>
                                                    <ul class="dropdown">
                                                        <li>
                                                            <a href="appointmentList.jsp" style="display: flex; align-items: center; justify-content: space-between;">
                                                                <i class="fa fa-calendar-o fa-class" style="margin-right: 10px;"></i>
                                                                <p style="margin: 0; flex-grow: 1;">Appointment</p>
                                                                <i class="icofont-rounded-right"></i>
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a href="profile.jsp" style="display: flex; align-items: center; justify-content: space-between;">
                                                                <i class="fa fa-user fa-class" style="margin-right: 10px;"></i>
                                                                <p style="margin: 0; flex-grow: 1;">Profile</p>
                                                                <i class="icofont-rounded-right"></i>
                                                            </a>
                                                        </li>
                                                        <li>
                                                            <a href="PatientSignout" style="display: flex; align-items: center; justify-content: space-between;">
                                                                <i class="fa fa-sign-out fa-class" style="margin-right: 10px;"></i>
                                                                <p style="margin: 0; flex-grow: 1;">Logout</p>
                                                                <i class="icofont-rounded-right"></i>
                                                            </a>
                                                        </li>
                                                    </ul>

                                                </li>
                                            </ul>
                                        </nav>
                                    </div>
                                </div>
                                <% }%>
                            </div>
                        </div>
                    </div>
                </div>
        </header>
    </body>
</html>
