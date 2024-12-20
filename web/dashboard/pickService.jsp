<%@page import="model.Services"%>
<%@page import="dao.ServicesDao"%>
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

    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    List<Services> servicesList = servicesDao.getAllServicesActive();

    String servicesImage;
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
                                for (Services services : servicesList) {
                                    if (services.getServicesImage() != null && services.getServicesImage().length > 0) {
                                        servicesImage = Base64.getEncoder().encodeToString(services.getServicesImage());
                                    } else {
                                        servicesImage = null;
                                    }

                            %>
                            <div class="col-md-6 col-sm-6 col-6">
                                <a href="pickDoctor.jsp?servicesID=<%= services.getServicesID()%>" class="services-btn">
                                    <div class="row services-category">
                                        <div class="col-4 col-md-3 services-img">
                                            <img src="<%= (servicesImage != null && !servicesImage.isEmpty()) ? "data:image/jpeg;base64," + servicesImage : "img/team-1.jpg"%>">
                                        </div>
                                        <div class="col-7 col-md-8">
                                            <p class="category-name mb-1" style="color: #000;"><%= services.getServicesName()%></p>
                                            <p><i class="fa fa-money"></i><%= (services.getServicesPrice() == 0) ? " Pay at Counter" : " RM " + String.format("%.2f", services.getServicesPrice())%></p>
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
