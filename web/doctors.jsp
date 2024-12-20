<%@page import="dao.DoctorDao"%>
<%@page import="connection.DbConn"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.List"%>
<%@page import="model.Doctor"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String doctorImage;

    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    List<Doctor> doctorList = doctorDao.getAllDoctors();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Doctors</title>
        <link rel="icon" href="img/favicon.png">
        <script src="js/jquery.min.js"></script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="breadcrumbs overlay">
            <div class="container">
                <div class="bread-inner">
                    <div class="row">
                        <div class="col-12">
                            <h2>Doctors</h2>
                            <ul class="bread-list">
                                <li><a href="home.jsp">Home</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="doctors.jsp">Doctors</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="container-xxl py-5">
            <div class="container">
                <div class="text-center mx-auto mb-5 wow fadeInUp" data-wow-delay="0.1s" style="max-width: 600px;">
                    <p class="d-inline-block border  py-1 px-4" style="border-radius: 16px;margin-bottom: 16px;">Doctors</p>
                    <h1>Our Experience Doctors</h1>
                </div>
                <div class="row g-4">
                    <%
                        if (doctorList != null) {
                            for (Doctor doctor : doctorList) {
                                if (doctor.getDoctorImage() != null && doctor.getDoctorImage().length > 0) {
                                    doctorImage = Base64.getEncoder().encodeToString(doctor.getDoctorImage());
                                } else {
                                    doctorImage = null;
                                }

                    %>
                    <div class="col-lg-3 col-md-3 wow fadeInUp mb-4">
                        <div class="overflow-hidden">
                            <img class="img-fluid" src="<%= (doctorImage != null && !doctorImage.isEmpty()) ? "data:image/jpeg;base64," + doctorImage : "img/team-1.jpg"%>" alt="Doctor Image" style="border-radius: 8px 8px 0 0;width: 300px; height: 350px;">
                        </div>
                        <div class="team-text text-center p-4" style="border-radius: 0 0 8px 8px;background-color: #EFF5FF;">
                            <h5><%= doctor.getDoctorName()%></h5>
                            <p class="text-primary"><%= doctor.getDoctorRole()%></p>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
