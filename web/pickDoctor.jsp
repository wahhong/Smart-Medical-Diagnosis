<%@page import="dao.*"%>
<%@page import="connection.DbConn"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.List"%>
<%@page import="model.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! String doctorImage;%>
<%
    Integer servicesID = null;
    Integer patientID = (Integer) session.getAttribute("userAuthID");

    if (patientID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    try {
        servicesID = Integer.parseInt(request.getParameter("servicesID"));
    } catch (NumberFormatException e) {
        response.sendRedirect("appointment.jsp");
        return;
    }

    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    List<Doctor> pickDoctorList = doctorDao.getAllDoctorsServices(servicesID);
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
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="">Pick Doctor</a></li>
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
                    <h1>Choose Doctor</h1>
                </div>
            </div>
        </div>

        <%
            if (pickDoctorList != null && !pickDoctorList.isEmpty()) {
        %>
        <div class="container-xxl datetime-border">
            <div class="container">
                <div class="row g-4">
                    <%
                        for (Doctor doctor : pickDoctorList) {
                            if (doctor.getDoctorImage() != null && doctor.getDoctorImage().length > 0) {
                                doctorImage = Base64.getEncoder().encodeToString(doctor.getDoctorImage());
                            } else {
                                doctorImage = null;
                            }
                    %>
                    <div class="col-md-6 col-sm-12 col-12 mb-2 mt-2">
                        <a href="pickDatetime.jsp?servicesID=<%= servicesID%>&doctorID=<%= doctor.getDoctorID()%>" class="services-btn">
                            <div class="row services-category">
                                <div class="col-4 col-md-3 services-img">
                                    <img src="<%= (doctorImage != null && !doctorImage.isEmpty()) ? "data:image/jpeg;base64," + doctorImage : "img/team-1.jpg"%>">
                                </div>
                                <div class="col-7 col-md-8">
                                    <p class="category-name mb-1" style="color: #000;"><%= doctor.getDoctorName()%></p>
                                    <p><%= doctor.getDoctorRole()%></p>
                                </div>
                                <div class="col-1 col-md-1">
                                    <i class="fa fa-chevron-right"></i>
                                </div>
                            </div>
                        </a>
                    </div>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
        <%
        } else {
        %>
        <div class="container-xxl datetime-border">
            <div class="container">
                <div class="row g-4">
                    <div class="container text-center py-5">
                        <h3>No doctors are available for this service at the moment.</h3>
                    </div>
                </div>
            </div>
        </div>
        <%
            }
        %>
        <div class="container-xxl py-5">
            <div class="container">
                <div class="row">
                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp" />
    </body>
</html>
