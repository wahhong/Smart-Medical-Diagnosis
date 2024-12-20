<%@page import="model.Appointment"%>
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

    Appointment appointment = (Appointment) session.getAttribute("appointmentDB");
    PatientDao patientDao = new PatientDao(DbConn.getConnection());

    List<Patient> patientList = patientDao.getAllPatients();
    String errorEmail = (String) request.getAttribute("errorEmail");

    String errorMsg = (String) request.getAttribute("errorMsg");
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
                    <a href="pickDoctor.jsp?servicesID=<%= appointment.getServicesID()%>">Select Doctor</a>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="pickDoctor.jsp?doctorID=<%= appointment.getDoctorID()%>&servicesID=<%= appointment.getServicesID()%>">Select Date & Time</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="aiDiagnosis.jsp"> AI Diagnosis</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">Patient Details</a>
                </li>
            </ul>
        </div>
        <% if (errorMsg != null) {%>
        <div class="alert alert-danger"><%= errorMsg%></div>
        <% }%>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="CheckPatientDetails">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorEmail != null ? 'has-error' : ''}">
                                        <label for="email">Email Address</label>
                                        <select class="form-select symptom-box wide symptom" name="email">
                                            <option value="">Select patient email</option>
                                            <%
                                                if (patientList != null) {
                                                    for (Patient patient : patientList) {
                                            %>
                                            <option value="<%= patient.getPatientEmail()%>"><%= patient.getPatientEmail()%></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                        <% if (errorEmail != null) {%>
                                        <small class="form-text text-muted"><%= errorEmail%></small>
                                        <% }%>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                </div>
                            </div>
                        </div>
                        <div class="card-action">
                            <button class="btn btn-success">Submit</button>
                            <a class="btn btn-danger" href="appointment.jsp">Cancel</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<jsp:include page="adminFooter.jsp" />
