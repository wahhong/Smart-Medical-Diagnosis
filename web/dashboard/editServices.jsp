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

    int servicesID = Integer.parseInt(request.getParameter("id"));

    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    Services service = servicesDao.getService(servicesID);

    String errorName = (String) request.getAttribute("errorName");
    String errorDesc = (String) request.getAttribute("errorDesc");
    String errorPrice = (String) request.getAttribute("errorPrice");
    String errorImage = (String) request.getAttribute("errorImage");
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Edit Service</h3>
            <ul class="breadcrumbs mb-3">
                <li class="nav-item">
                    <a href="services.jsp">Service</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">Edit Service Form</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="EditServices">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="id">ID</label>
                                        <input type="text" class="form-control" name="id" placeholder="ID" value="<%= service.getServicesID()%>" disabled/>
                                        <input type="hidden" name="id" value="<%= service.getServicesID()%>"/>
                                    </div>
                                    <div class="form-group ${errorPrice != null ? 'has-error' : ''}">
                                        <label for="price">Deposit Fee</label>
                                        <input type="text" class="form-control" name="price" placeholder="Enter price" value="<%= request.getAttribute("price") != null ? request.getAttribute("price") : (service.getServicesPrice() != null ? service.getServicesPrice() : "")%>"/>
                                        <% if (errorPrice != null) {%>
                                        <small class="form-text text-muted"><%= errorPrice%></small>
                                        <% }%>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorName != null ? 'has-error' : ''}">
                                        <label for="name">Service Name</label>
                                        <input type="text" class="form-control" name="name" placeholder="Enter Name" value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : (service.getServicesName() != null ? service.getServicesName() : "")%>"/>
                                        <% if (errorName != null) {%>
                                        <small class="form-text text-muted"><%= errorName%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group">
                                        <label for="aiAccess">AI Access</label>
                                        <select class="form-select" id="aiAccess" name="aiAccess">
                                            <option value="0" <%= Integer.valueOf(0).equals(request.getAttribute("aiAccess")) || (service.getServicesAi() == 0) ? "selected" : ""%>>Disclose</option>
                                            <option value="1" <%= Integer.valueOf(1).equals(request.getAttribute("aiAccess")) || (service.getServicesAi() == 1) ? "selected" : ""%>>Allow</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group ${errorDesc != null ? 'has-error' : ''}">
                                        <label for="desc">Service Description</label>
                                        <input type="text" class="form-control" name="desc" placeholder="Enter Description" value="<%= request.getAttribute("desc") != null ? request.getAttribute("desc") : (service.getServicesDesc() != null ? service.getServicesDesc() : "")%>"/>
                                        <% if (errorDesc != null) {%>
                                        <small class="form-text text-muted"><%= errorDesc%></small>
                                        <% }%>
                                    </div>
                                    <div class="form-group ">
                                        <label for="status">Status</label>
                                        <select class="form-select" id="status" name="status">
                                            <option value="1" <%= Integer.valueOf(1).equals(request.getAttribute("status")) || (service.getServicesStatus() == 1) ? "selected" : ""%>>Active</option>
                                            <option value="0" <%= Integer.valueOf(0).equals(request.getAttribute("status")) || (service.getServicesStatus() == 0) ? "selected" : ""%>>Inactive</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-action">
                            <button class="btn btn-success">Submit</button>
                            <a class="btn btn-danger" href="services.jsp">Cancel</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<jsp:include page="adminFooter.jsp" />
