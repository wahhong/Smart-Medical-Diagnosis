<%@page import="java.util.Base64"%>
<%@page import="model.*"%>
<%@page import="java.util.List"%>
<%@page import="dao.*"%>
<%@page import="connection.DbConn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="adminHeader.jsp" />

<%
    Integer adminAuthID = (Integer) session.getAttribute("adminAuthID");

    if (adminAuthID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String profileImage = null;
    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    Doctor doctor = doctorDao.getDoctor(adminAuthID);
    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    String serviceName = servicesDao.getServiceNameById(doctor.getServicesID());

    if (doctor.getDoctorImage() != null && doctor.getDoctorImage().length > 0) {
        profileImage = Base64.getEncoder().encodeToString(doctor.getDoctorImage());
    } else {
        profileImage = null;
    }

    String errorImage = (String) session.getAttribute("errorImage");
    String successMsg = (String) session.getAttribute("successMsg");
    String errorMsg = (String) session.getAttribute("errorMsg");
    String errorPassword = (String) request.getAttribute("errorPassword");
%>

<div class="container">
    <div class="page-inner">
        <h3 class="fw-bold mb-3">User Profile</h3>
        <div class="row">
            <div class="col-md-8">
                <% if (successMsg != null) {%>
                <div class="alert alert-success"><%= successMsg%></div>
                <%
                    session.removeAttribute("successMsg");
                } else if (errorMsg != null) {%>
                <div class="alert alert-danger"><%= errorMsg%></div>
                <%
                    session.removeAttribute("errorMsg");
                } else if (errorImage != null) {%>
                <div class="alert alert-danger"><%= errorImage%></div>
                <%
                        session.removeAttribute("errorImage");
                    }%>
                <form method="post" action="EditDrProfilePass">
                    <div class="card card-with-nav">
                        <div class="card-header">
                            <div class="row row-nav-line">
                                <ul class="nav nav-tabs nav-line nav-color-secondary w-100 ps-4" role="tablist">
                                    <li class="nav-item submenu" role="presentation"> <a class="nav-link show active" data-bs-toggle="tab" href="" role="tab" aria-selected="false" tabindex="-1">Profile</a> </li>
                                </ul>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="row mt-3">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Name</label>
                                        <input type="text" class="form-control-plaintext" name="name" placeholder="Name" value="<%= doctor.getDoctorName()%>" readonly>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Email</label>
                                        <input type="email" class="form-control-plaintext" name="email" placeholder="Name" value="<%= doctor.getDoctorEmail()%>" readonly>
                                    </div>
                                </div>
                            </div>
                            <div class="row mt-3">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label>Birth Date</label>
                                        <input type="date" class="form-control-plaintext" id="datepicker" name="datepicker" value="<%= doctor.getDoctorDOB()%>" readonly>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label>Gender</label>
                                        <input type="text" class="form-control-plaintext" value="<%= doctor.getDoctorGender()%>" name="gender" readonly>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label>Phone</label>
                                        <input type="text" class="form-control-plaintext" value="<%= doctor.getDoctorPhone()%>" name="phone" readonly>
                                    </div>
                                </div>
                            </div>
                            <div class="row mt-3">
                                <input type="hidden" class="form-control" name="id" value="<%= doctor.getDoctorID()%>"/>
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
                            <div class="text-end mt-3 mb-3">
                                <button class="btn btn-success">Save</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="col-md-4">
                <form method="post" action="EditDrProfileImg" enctype="multipart/form-data">
                    <div class="card card-profile">
                        <div class="card-header" style="background-image: url('assets/img/blogpost.jpg')">
                            <div class="profile-picture">
                                <div class="avatar avatar-xl">
                                    <img 
                                        id="profile-image" 
                                        src="<%= (profileImage != null && !profileImage.isEmpty()) ? "data:image/jpeg;base64," + profileImage : "img/author.jpg"%>" 
                                        class="avatar-img rounded-circle" 
                                        alt=""
                                        onclick="document.getElementById('profile-pic-input').click();"
                                        style="cursor: pointer;">
                                    <input type="hidden" name="id" value="<%= doctor.getDoctorID()%>">
                                    <input 
                                        type="file" 
                                        id="profile-pic-input" 
                                        name="image" 
                                        style="display:none;" 
                                        accept="image/*"
                                        onchange="previewImage(event)"
                                        >
                                </div>
                            </div>
                        </div>
                        <div class="card-body">
                            <div class="user-profile text-center">
                                <div class="name"><%= doctor.getDoctorName()%></div>
                                <div class="job"><%= doctor.getDoctorRole()%></div>
                                <div class="desc"><%= serviceName != null && !serviceName.isEmpty() ? serviceName : ""%></div>

                                <div class="social-media">
                                    <% if (doctor.getDoctorStatus() == 1) { %>
                                    <span class="badge badge-success">Active</span>
                                    <% } else { %>
                                    <span class="badge badge-danger">Inactive</span>
                                    <% }%></div>
                            </div>
                            <div class="view-profile">
                                <button class="btn btn-secondary w-100">Update Profile Picture</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    function previewImage(event) {
        const input = event.target;
        const reader = new FileReader();

        reader.onload = function () {
            const imgElement = document.getElementById("profile-image");
            imgElement.src = reader.result;
        };

        if (input.files && input.files[0]) {
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>

<jsp:include page="adminFooter.jsp" />
