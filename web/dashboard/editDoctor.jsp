<%@page import="model.Doctor"%>
<%@page import="model.Services"%>
<%@page import="dao.ServicesDao"%>
<%@page import="java.util.Base64"%>
<%@page import="model.Patient"%>
<%@page import="java.util.List"%>
<%@page import="dao.DoctorDao"%>
<%@page import="connection.DbConn"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="adminHeader.jsp" />
<%
    Integer adminAuthID = (Integer) session.getAttribute("adminAuthID");

    if (adminAuthID == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    
    int doctorID = Integer.parseInt(request.getParameter("id"));
    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    Doctor doctor = doctorDao.getDoctor(doctorID);

    if (doctor == null) {
        response.sendRedirect("doctor.jsp");
        return;
    }

    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    List<Services> services = servicesDao.getAllServicesActive();

    String errorName = (String) request.getAttribute("errorName");
    String errorEmail = (String) request.getAttribute("errorEmail");
    String errorPhone = (String) request.getAttribute("errorPhone");
    String errorDOB = (String) request.getAttribute("errorDOB");
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Edit Doctor</h3>
            <ul class="breadcrumbs mb-3">
                <li class="nav-item">
                    <a href="doctor.jsp">Doctor</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">Edit Doctor Form</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="EditDoctor">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="id">ID</label>
                                        <input type="text" class="form-control" name="id" placeholder="ID" value="<%= doctor.getDoctorID()%>" disabled/>
                                        <input type="hidden" name="id" value="<%= doctor.getDoctorID()%>"/>
                                    </div>

                                    <div class="form-group ${errorPhone != null ? 'has-error' : ''}">
                                        <label for="phone">Phone</label>
                                        <input type="text" class="form-control" name="phone" placeholder="Enter Phone Number" 
                                               value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : (doctor.getDoctorPhone() != null ? doctor.getDoctorPhone() : "")%>"/>
                                        <% if (errorPhone != null) {%>
                                        <small class="form-text text-muted"><%= errorPhone%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group">
                                        <label for="role">Role</label>
                                        <select class="form-select" id="role" name="role">
                                            <option value="Doctor" <%= "Doctor".equals(doctor.getDoctorRole()) ? "selected" : ""%>>Doctor</option>
                                            <option value="Nurse" <%= "Nurse".equals(doctor.getDoctorRole()) ? "selected" : ""%>>Nurse</option>
                                            <option value="Admin" <%= "Admin".equals(doctor.getDoctorRole()) ? "selected" : ""%>>Admin</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorName != null ? 'has-error' : ''}">
                                        <label for="name">Name</label>
                                        <input type="text" class="form-control" name="name" placeholder="Enter Name" 
                                               value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : (doctor.getDoctorName() != null ? doctor.getDoctorName() : "")%>"/>
                                        <% if (errorName != null) {%>
                                        <small class="form-text text-muted"><%= errorName%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group ${errorDOB != null ? 'has-error' : ''}">
                                        <label for="dob">Date of Birth</label>
                                        <input type="date" class="form-control" id="dob" name="dob" 
                                               value="<%= request.getAttribute("dob") != null ? request.getAttribute("dob") : (doctor.getDoctorDOB() != null ? doctor.getDoctorDOB() : "")%>" max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(System.currentTimeMillis() - (long) 18 * 365 * 24 * 60 * 60 * 1000))%>"/>
                                        <% if (errorDOB != null) {%>
                                        <small class="form-text text-muted"><%= errorDOB%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group">
                                        <label for="status">Status</label>
                                        <select class="form-select" id="status" name="status">
                                            <option value="1" <%= Integer.valueOf(1).equals(request.getAttribute("status") != null
                                                    ? Integer.parseInt(request.getAttribute("status").toString())
                                                    : doctor.getDoctorStatus()) ? "selected" : ""%>>Active</option>
                                            <option value="0" <%= Integer.valueOf(0).equals(request.getAttribute("status") != null
                                                    ? Integer.parseInt(request.getAttribute("status").toString())
                                                    : doctor.getDoctorStatus()) ? "selected" : ""%>>Inactive</option>
                                        </select>
                                    </div>

                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorEmail != null ? 'has-error' : ''}">
                                        <label for="email">Email Address</label>
                                        <input type="text" class="form-control" name="email" placeholder="Enter Email" 
                                               value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : (doctor.getDoctorEmail() != null ? doctor.getDoctorEmail() : "")%>"/>
                                        <% if (errorEmail != null) {%>
                                        <small class="form-text text-muted"><%= errorEmail%></small>
                                        <% } %>
                                    </div>

                                    <div class="form-group">
                                        <label for="service">Services</label>
                                        <select class="form-select" id="service" name="service">
                                            <option value="0">Select Service</option>
                                            <%
                                                Integer selectedService = (Integer) request.getAttribute("service") != null
                                                        ? Integer.parseInt(request.getAttribute("service").toString())
                                                        : doctor.getServicesID();

                                                for (Services service : services) {
                                            %>
                                            <option value="<%= service.getServicesID()%>" 
                                                    <%= selectedService != null && selectedService.equals(service.getServicesID()) ? "selected" : ""%>>
                                                <%= service.getServicesName()%>
                                            </option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label>Gender</label><br/>
                                        <div class="d-flex">
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="gender" value="male" 
                                                       <%= "male".equals(request.getAttribute("gender") != null
                                                               ? request.getAttribute("gender").toString()
                                                               : doctor.getDoctorGender()) ? "checked" : ""%> />
                                                <label class="form-check-label" for="flexRadioDefault1">Male</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="gender" value="female" 
                                                       <%= "female".equals(request.getAttribute("gender") != null
                                                               ? request.getAttribute("gender").toString()
                                                               : doctor.getDoctorGender()) ? "checked" : ""%> />
                                                <label class="form-check-label" for="flexRadioDefault2">Female</label>
                                            </div>
                                        </div>
                                    </div>
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
