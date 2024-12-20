<%@page import="model.Doctor"%>
<%@page import="dao.DoctorDao"%>
<%@page import="java.util.Base64"%>
<%@page import="model.Patient"%>
<%@page import="java.util.List"%>
<%@page import="dao.PatientDao"%>
<%@page import="connection.DbConn"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="adminHeader.jsp" />
<%
    Integer adminAuthID = (Integer) session.getAttribute("adminAuthID");

    if (adminAuthID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int serviceID = Integer.parseInt(request.getParameter("servicesID"));

    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    List<Doctor> doctorList = doctorDao.getAllDoctorsServices(serviceID);

    String doctorImage;
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Book Appointment</h3>
            <ul class="breadcrumbs mb-3">
                <li class="nav-item">
                    <a href="appointment.jsp">Appointment</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="pickService.jsp">Select Service</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">Select Doctor</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        <div class="card-title">Select Service</div>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <%
                                for (Doctor doctor : doctorList) {
                                    if (doctor.getDoctorImage() != null && doctor.getDoctorImage().length > 0) {
                                        doctorImage = Base64.getEncoder().encodeToString(doctor.getDoctorImage());
                                    } else {
                                        doctorImage = null;
                                    }

                            %>
                            <div class="col-md-6 col-sm-6 col-6 mb-2 mt-2">
                                <a href="pickDatetime.jsp?doctorID=<%= doctor.getDoctorID()%>&servicesID=<%= serviceID%>" class="services-btn">
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
            </div>
        </div>
    </div>
</div>


<jsp:include page="adminFooter.jsp" />
