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
    PatientDao patientDao = new PatientDao(DbConn.getConnection());
    Patient patient = patientDao.getPatient(patientID);

    if (patient == null) {
        response.sendRedirect("patient.jsp");
        return;
    }

    String errorName = (String) request.getAttribute("errorName");
    String errorEmail = (String) request.getAttribute("errorEmail");
    String errorPhone = (String) request.getAttribute("errorPhone");
    String errorDOB = (String) request.getAttribute("errorDOB");
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Edit Patient</h3>
            <ul class="breadcrumbs mb-3">
                <li class="nav-item">
                    <a href="patient.jsp">Patient</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">Edit Patient Form</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="EditPatient">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="id">ID</label>
                                        <input type="text" class="form-control" name="id" placeholder="ID" value="<%= patient.getPatientID()%>" disabled/>
                                        <input type="hidden" name="id" value="<%= patient.getPatientID()%>"/>
                                    </div>
                                    <div class="form-group ${errorName != null ? 'has-error' : ''}">
                                        <label for="name">Name</label>
                                        <input type="text" class="form-control" name="name" placeholder="Enter Name" 
                                               value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : (patient.getPatientName() != null ? patient.getPatientName() : "")%>"/>
                                        <% if (errorName != null) {%>
                                        <small class="form-text text-muted"><%= errorName%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group">
                                        <label for="status">Status</label>
                                        <select class="form-select" id="status" name="status">
                                            <option value="1" <%= "1".equals(request.getAttribute("status")) || patient.getPatientStatus() == 1 ? "selected" : ""%>>Active</option>
                                            <option value="0" <%= "0".equals(request.getAttribute("status")) || patient.getPatientStatus() == 0 ? "selected" : ""%>>Inactive</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorEmail != null ? 'has-error' : ''}">
                                        <label for="email">Email Address</label>
                                        <input type="text" class="form-control" name="email" placeholder="Enter Email" 
                                               value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : (patient.getPatientEmail() != null ? patient.getPatientEmail() : "")%>"/>
                                        <% if (errorEmail != null) {%>
                                        <small class="form-text text-muted"><%= errorEmail%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group ${errorDOB != null ? 'has-error' : ''}">
                                        <label for="dob">Date of Birth</label>
                                        <input type="date" class="form-control" id="dob" name="dob" 
                                               value="<%= request.getAttribute("dob") != null ? request.getAttribute("dob") : (patient.getPatientDOB() != null ? patient.getPatientDOB() : "")%>" max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"/>
                                        <% if (errorDOB != null) {%>
                                        <small class="form-text text-muted"><%= errorDOB%></small>
                                        <% }%>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorPhone != null ? 'has-error' : ''}">
                                        <label for="phone">Phone</label>
                                        <input type="text" class="form-control" name="phone" placeholder="Enter Phone Number" 
                                               value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : (patient.getPatientPhone() != null ? patient.getPatientPhone() : "")%>"/>
                                        <% if (errorPhone != null) {%>
                                        <small class="form-text text-muted"><%= errorPhone%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group">
                                        <label>Gender</label><br/>
                                        <div class="d-flex">
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="gender" value="male" 
                                                       <%= "male".equals(request.getAttribute("gender")) || "male".equals(patient.getPatientGender()) ? "checked" : ""%> />
                                                <label class="form-check-label" for="flexRadioDefault1">Male</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="gender" value="female" 
                                                       <%= "female".equals(request.getAttribute("gender")) || "female".equals(patient.getPatientGender()) ? "checked" : ""%> />
                                                <label class="form-check-label" for="flexRadioDefault2">Female</label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-action">
                            <button class="btn btn-success">Submit</button>
                            <a class="btn btn-danger" href="patient.jsp">Cancel</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<jsp:include page="adminFooter.jsp" />
