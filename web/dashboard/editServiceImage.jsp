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

    String errorName = (String) request.getAttribute("errorName");
    String errorDesc = (String) request.getAttribute("errorDesc");
    String errorPrice = (String) request.getAttribute("errorPrice");
    String errorImage = (String) request.getAttribute("errorImage");
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Edit Service Image</h3>
            <ul class="breadcrumbs mb-3">
                <li class="nav-item">
                    <a href="services.jsp">Service</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">Edit Service Image</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="EditServicesImage" enctype="multipart/form-data">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <input type="hidden" name="id" value="<%= servicesID%>" />
                                    <div class="form-group ${errorImage != null ? 'has-error' : ''}">
                                        <label for="image">Service Image</label>
                                        <input type="file" class="form-control-file" id="image" name="image">
                                        <% if (errorImage != null) {%>
                                        </br><small class="form-text text-muted"><%= errorImage%></small>
                                        <% }%>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
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
