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
    
    int patientID = Integer.parseInt(request.getParameter("id"));
    
    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    List<Services> services = servicesDao.getAllServicesActive();

    String errorPassword = (String) request.getAttribute("errorPassword");
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Edit Doctor Password</h3>
            <ul class="breadcrumbs mb-3">
                <li class="nav-item">
                    <a href="doctor.jsp">Doctor</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="editDoctorPassword.jsp">Add Doctor Form</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="EditDoctorPassword">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                    <input type="hidden" class="form-control" name="id" value="<%= patientID %>"/>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorPassword != null ? 'has-error' : ''}">
                                        <label for="password">Password</label>
                                        <input type="password" class="form-control" name="password" placeholder="Enter Password" value=""/>
                                        <% if (errorPassword != null) {%>
                                        <small class="form-text text-muted"><%= errorPassword%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group ${errorPassword != null ? 'has-error' : ''}">
                                        <label for="repassword">Re-enter Password</label>
                                        <input type="password" class="form-control" name="repassword" placeholder="Re-enter Password" value=""/>
                                        <% if (errorPassword != null) {%>
                                        <small class="form-text text-muted"><%= errorPassword%></small>
                                        <% }%>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                </div>
                            </div>
                        </div>
                        <div class="card-action">
                            <button class="btn btn-success">Submit</button>
                            <a class="btn btn-danger" href="doctor.jsp">Cancel</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<jsp:include page="adminFooter.jsp" />
