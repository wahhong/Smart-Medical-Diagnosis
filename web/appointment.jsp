<%@page import="dao.*"%>
<%@page import="connection.DbConn"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.List"%>
<%@page import="model.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! String servicesImage;%>
<%
    Integer patientID = (Integer) session.getAttribute("userAuthID");
    if (patientID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    List<Services> servicesList = servicesDao.getAllServicesActive();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Appointment</title>
        <link rel="icon" href="img/favicon.png">
        <link rel="stylesheet" href="css/services.css">
        <script src="js/jquery.min.js"></script>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div class="breadcrumbs overlay">
            <div class="container">
                <div class="bread-inner">
                    <div class="row">
                        <div class="col-12">
                            <h2>Appointment</h2>
                            <ul class="bread-list">
                                <li><a href="home.jsp">Home</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="appointment.jsp">Appointment</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="container-xxl py-5" style="padding-bottom: 0px !important;">
            <div class="container">
                <div class="text-center mx-auto mb-5 wow fadeInUp" data-wow-delay="0.1s" style="max-width: 600px;">
                    <p class="d-inline-block border  py-1 px-4" style="border-radius: 16px; margin-bottom: 16px;">Appointment</p>
                    <h1>Choose service</h1>
                </div>
            </div>
        </div>

        <div class="container-xxl datetime-border">
            <div class="container">
                <div class="row g-4">
                    <%
                        if (servicesList != null) {
                            for (Services services : servicesList) {
                                if (services.getServicesImage() != null && services.getServicesImage().length > 0) {
                                    servicesImage = Base64.getEncoder().encodeToString(services.getServicesImage());
                                } else {
                                    servicesImage = null;
                                }

                    %>
                    <div class="col-md-6 col-sm-6 col-6 mb-2 mt-2">
                        <a href="pickDoctor.jsp?servicesID=<%= services.getServicesID()%>" class="services-btn">
                            <div class="row services-category">
                                <div class="col-4 col-md-3 services-img">
                                    <img src="<%= (servicesImage != null && !servicesImage.isEmpty()) ? "data:image/jpeg;base64," + servicesImage : "img/team-1.jpg"%>">
                                </div>
                                <div class="col-7 col-md-8">
                                    <p class="category-name mb-1" style="color: #000;"><%= services.getServicesName()%></p>
                                    <p><i class="fa fa-money"></i><%= " RM " + String.format("%.2f", services.getServicesPrice())%> (Deposit)</p>
                                </div>
                                <div class="col-1 col-md-1">
                                    <i class="fa fa-chevron-right"></i>
                                </div>
                            </div>
                        </a>
                    </div>
                    <%
                            }
                        }
                    %>
                </div>
            </div>
        </div>

        <div class="container-xxl py-5">
            <div class="container">
                <div class="row">
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
