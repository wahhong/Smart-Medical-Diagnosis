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
    List<Services> services = servicesDao.getAllServicesActive();

    String errorName = (String) request.getAttribute("errorName");
    String errorEmail = (String) request.getAttribute("errorEmail");
    String errorPhone = (String) request.getAttribute("errorPhone");
    String errorDOB = (String) request.getAttribute("errorDOB");
    String errorPassword = (String) request.getAttribute("errorPassword");
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Add Doctor</h3>
            <ul class="breadcrumbs mb-3">
                <li class="nav-item">
                    <a href="doctor.jsp">Doctor</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">Add Doctor Form</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="AddDoctor">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorName != null ? 'has-error' : ''}">
                                        <label for="name">Name</label>
                                        <input type="text" class="form-control" name="name" placeholder="Enter Name" value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : ""%>"/>
                                        <% if (errorName != null) {%>
                                        <small class="form-text text-muted"><%= errorName%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group">
                                        <label for="role">Role</label>
                                        <select class="form-select" id="role" name="role">
                                            <option value="Doctor" <%= "Doctor".equals(request.getAttribute("role")) ? "selected" : ""%>>Doctor</option>
                                            <option value="Nurse" <%= "Nurse".equals(request.getAttribute("role")) ? "selected" : ""%>>Nurse</option>
                                            <option value="Admin" <%= "Admin".equals(request.getAttribute("role")) ? "selected" : ""%>>Admin</option>
                                        </select>
                                    </div>
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
                                    <div class="form-group ${errorEmail != null ? 'has-error' : ''}">
                                        <label for="email">Email Address</label>
                                        <input type="text" class="form-control" name="email" placeholder="Enter Email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : ""%>"/>
                                        <% if (errorEmail != null) {%>
                                        <small class="form-text text-muted"><%= errorEmail%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group ${errorDOB != null ? 'has-error' : ''}">
                                        <label for="dob">Date of Birth</label>
                                        <input type="date" class="form-control" id="dob" name="dob" value="<%= request.getAttribute("dob") != null ? request.getAttribute("dob") : ""%>" max="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date(System.currentTimeMillis() - (long) 18 * 365 * 24 * 60 * 60 * 1000))%>"/>
                                        <% if (errorDOB != null) {%>
                                        <small class="form-text text-muted"><%= errorDOB%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group ">
                                        <label for="status">Status</label>
                                        <select class="form-select" id="status" name="status">
                                            <option value="1" <%= Integer.valueOf(1).equals(request.getAttribute("status")) ? "selected" : ""%>>Active</option>
                                            <option value="0" <%= Integer.valueOf(0).equals(request.getAttribute("status")) ? "selected" : ""%>>Inactive</option>
                                        </select>
                                    </div>

                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorPhone != null ? 'has-error' : ''}">
                                        <label for="phone">Phone</label>
                                        <input type="text" class="form-control" name="phone" placeholder="Enter Phone Number" value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : ""%>"/>
                                        <% if (errorPhone != null) {%>
                                        <small class="form-text text-muted"><%= errorPhone%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group">
                                        <label for="service">Services</label>
                                        <select class="form-select" id="service" name="service">
                                            <option value="0">Select Service</option>
                                            <%
                                                for (Services service : services) {
                                            %>
                                            <option value="<%= service.getServicesID()%>" 
                                                    <%= request.getAttribute("service") != null && request.getAttribute("service").toString().equals(String.valueOf(service.getServicesID())) ? "selected" : ""%>>
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
                                                       <%= ("male".equals(request.getAttribute("gender")) || request.getAttribute("gender") == null) ? "checked" : ""%> />
                                                <label class="form-check-label" for="flexRadioDefault1">Male</label>
                                            </div>
                                            <div class="form-check">
                                                <input class="form-check-input" type="radio" name="gender" value="female" 
                                                       <%= "female".equals(request.getAttribute("gender")) ? "checked" : ""%> />
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
