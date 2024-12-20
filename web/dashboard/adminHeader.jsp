<%@page import="model.Notification"%>
<%@page import="java.util.List"%>
<%@page import="dao.NotificationDao"%>
<%@page import="java.util.Base64"%>
<%@page import="model.Doctor"%>
<%@page import="dao.DoctorDao"%>
<%@page import="connection.DbConn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Integer adminAuthID = (Integer) session.getAttribute("adminAuthID");

    if (adminAuthID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String adminProfile = null;

    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    NotificationDao notificationDao = new NotificationDao(DbConn.getConnection());
    Doctor doctor = doctorDao.getDoctor(adminAuthID);
    List<Notification> notificationList = notificationDao.getNotificationsByDoctorID(adminAuthID);
    int count = 0;

    if (doctor.getDoctorImage() != null && doctor.getDoctorImage().length > 0) {
        adminProfile = Base64.getEncoder().encodeToString(doctor.getDoctorImage());
    } else {
        adminProfile = null;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Dashboard</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="icon" href="assets/img/kaiadmin/favicon.ico" type="image/x-icon"/>
        <script src="assets/js/plugin/webfont/webfont.min.js"></script>
        <script>
            WebFont.load({
                google: {"families": ["Public Sans:300,400,500,600,700"]},
                custom: {"families": ["Font Awesome 5 Solid", "Font Awesome 5 Regular", "Font Awesome 5 Brands", "simple-line-icons"], urls: ['assets/css/fonts.min.css']},
                active: function () {
                    sessionStorage.fonts = true;
                }
            });
        </script>
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/plugins.min.css">
        <link rel="stylesheet" href="assets/css/kaiadmin.min.css">
        <link rel="stylesheet" href="assets/css/appointment.css">
        <script src="assets/js/core/jquery-3.7.1.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        <script>
            $(document).ready(function () {
                $('.symptom').select2();
            });
        </script>
    </head>
    <body>
        <div class="wrapper">
            <!-- Sidebar -->
            <div class="sidebar" data-background-color="dark">
                <div class="sidebar-logo">
                    <!-- Logo Header -->
                    <div class="logo-header" data-background-color="dark">

                        <a href="index.html" class="logo">
                            <img src="../img/dark_logo.png" alt="navbar brand" class="navbar-brand" height="40">
                        </a>
                        <div class="nav-toggle">
                            <button class="btn btn-toggle toggle-sidebar">
                                <i class="gg-menu-right"></i>
                            </button>
                            <button class="btn btn-toggle sidenav-toggler">
                                <i class="gg-menu-left"></i>
                            </button>
                        </div>
                        <button class="topbar-toggler more">
                            <i class="gg-more-vertical-alt"></i>
                        </button>
                    </div>
                </div>
                <%
                    String currentPage = request.getRequestURI();
                %>
                <div class="sidebar-wrapper scrollbar scrollbar-inner">
                    <div class="sidebar-content">
                        <ul class="nav nav-secondary">
                            <li class="nav-item <%= currentPage.contains("dashboard.jsp") ? "active" : ""%>">
                                <a href="dashboard.jsp">
                                    <i class="fas fa-desktop"></i>
                                    <p>Dashboard</p>
                                </a>
                            </li>
                            <li class="nav-item <%= currentPage.contains("patient.jsp") || currentPage.contains("addPatient.jsp") || currentPage.contains("editPatient.jsp") || currentPage.contains("editPatientImage.jsp") ? "active" : ""%>">
                                <a href="patient.jsp">
                                    <i class="fas fa-user"></i>
                                    <p>Patient</p>
                                </a>
                            </li>
                            <%
                                if (doctor.getDoctorRole().equals("Admin")) {
                            %>
                            <li class="nav-item <%= currentPage.contains("doctor.jsp") || currentPage.contains("addDoctor.jsp") || currentPage.contains("editDoctor.jsp") || currentPage.contains("editDoctorPassword") || currentPage.contains("editDoctorImage") ? "active" : ""%>">
                                <a href="doctor.jsp">
                                    <i class="fas fa-user-md"></i>
                                    <p>Doctor</p>
                                </a>
                            </li>
                            <li class="nav-item <%= currentPage.contains("services.jsp") || currentPage.contains("addServices.jsp") || currentPage.contains("editServices.jsp") || currentPage.contains("editServiceImage") ? "active" : ""%>">
                                <a href="services.jsp">
                                    <i class="fas fa-briefcase-medical"></i>
                                    <p>Services</p>
                                </a>
                            </li>
                            <%
                                }
                            %>
                            <li class="nav-item <%= currentPage.contains("appointment.jsp") || currentPage.contains("pickService.jsp") || currentPage.contains("pickDoctor.jsp") || currentPage.contains("pickDatetime.jsp") || currentPage.contains("aiDiagnosis.jsp") || currentPage.contains("patientDetails.jsp") || currentPage.contains("editAppointment.jsp") ? "active" : ""%>">
                                <a href="appointment.jsp">
                                    <i class="fas fa-calendar-check"></i>
                                    <p>Appointment</p>
                                </a>
                            </li>
                            <%
                                if (doctor.getDoctorRole().equals("Admin")) {
                            %>
                            <li class="nav-item <%= currentPage.contains("payment.jsp") || currentPage.contains("addPayment.jsp") || currentPage.contains("editPayment.jsp") ? "active" : ""%>">
                                <a href="payment.jsp">
                                    <i class="fas fa-money-bill"></i>
                                    <p>Payment</p>
                                </a>
                            </li>
                            <li class="nav-item <%= currentPage.contains("drug.jsp") || currentPage.contains("addDrug.jsp") || currentPage.contains("editDrug.jsp") ? "active" : ""%>">
                                <a href="drug.jsp">
                                    <i class="fas fa-pills"></i>
                                    <p>Drug</p>
                                </a>
                            </li>
                            <%
                                }
                            %>
                        </ul>
                    </div>
                </div>
            </div>
            <!-- End Sidebar -->

            <div class="main-panel">
                <div class="main-header">
                    <div class="main-header-logo">
                        <!-- Logo Header -->
                        <div class="logo-header" data-background-color="dark">

                            <a href="index.html" class="logo">
                                <img src="assets/img/kaiadmin/logo_light.svg" alt="navbar brand" class="navbar-brand" height="20">
                            </a>
                            <div class="nav-toggle">
                                <button class="btn btn-toggle toggle-sidebar">
                                    <i class="gg-menu-right"></i>
                                </button>
                                <button class="btn btn-toggle sidenav-toggler">
                                    <i class="gg-menu-left"></i>
                                </button>
                            </div>
                            <button class="topbar-toggler more">
                                <i class="gg-more-vertical-alt"></i>
                            </button>
                        </div>
                    </div>

                    <nav class="navbar navbar-header navbar-header-transparent navbar-expand-lg border-bottom">

                        <div class="container-fluid">

                            <ul class="navbar-nav topbar-nav ms-md-auto align-items-center">
                                <li class="nav-item topbar-icon dropdown hidden-caret d-flex d-lg-none">
                                    <a class="nav-link dropdown-toggle" data-bs-toggle="dropdown" href="#" role="button" aria-expanded="false" aria-haspopup="true">
                                        <i class="fa fa-search"></i>
                                    </a>
                                    <ul class="dropdown-menu dropdown-search animated fadeIn">
                                        <form class="navbar-left navbar-form nav-search">
                                            <div class="input-group">
                                                <input type="text" placeholder="Search ..." class="form-control">
                                            </div>
                                        </form>
                                    </ul>
                                </li>
                                <%
                                    if (notificationList != null && !notificationList.isEmpty()) {
                                        for (Notification notification : notificationList) {
                                            if (notification.getNotificationStatus() == 0) {
                                                count++;
                                            }
                                        }
                                    }
                                %>
                                <li class="nav-item topbar-icon dropdown hidden-caret">
                                    <a class="nav-link dropdown-toggle" href="#" id="notifDropdown" role="button" data-bs-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <i class="fa fa-bell"></i>
                                        <span class="notification"><%= count%></span>
                                    </a>
                                    <ul class="dropdown-menu notif-box animated fadeIn" aria-labelledby="notifDropdown">
                                        <li>
                                            <div class="notif-scroll scrollbar-outer">
                                                <%
                                                    if (notificationList != null && !notificationList.isEmpty()) {
                                                        for (Notification notification : notificationList) {
                                                %>
                                                <div class="notif-center" style="background-color: <%= notification.getNotificationStatus() == 0 ? "#e1e1e152" : "#fff"%>">
                                                    <a href="UpdateNotification?id=<%= notification.getNotificationID()%>&aptID=<%= notification.getAppointmentID()%>">
                                                        <div class="notif-icon notif-primary" style="width: 70px; height: 40px; display: flex; justify-content: center; align-items: center;">
                                                            <i class="fa fa-calendar"></i>
                                                        </div>
                                                        <div class="notif-content">
                                                            <strong><span class="block">
                                                                    <%= notification.getNotificationTitle()%>
                                                                </span></strong>
                                                            <span class="block">
                                                                <%= notification.getNotificationDesc()%>
                                                            </span>
                                                        </div>
                                                    </a>
                                                </div>
                                                <%
                                                    }
                                                } else {
                                                %>
                                                <div class="notif-center text-center d-flex justify-content-center align-items-center" style="height: 257px;">
                                                    <div class="notif-content">
                                                        <span class="block">
                                                            No notification
                                                        </span>
                                                    </div>
                                                </div>
                                                <% }%>
                                            </div>
                                        </li>
                                        <li>
                                            <div class="dropdown-title">You have <%= count%> new notification</div>
                                        </li>
                                    </ul>
                                </li>

                                <li class="nav-item topbar-user dropdown hidden-caret">
                                    <a class="dropdown-toggle profile-pic" data-bs-toggle="dropdown" href="#" aria-expanded="false">
                                        <div class="avatar-sm">
                                            <img src="<%= (adminProfile != null && !adminProfile.isEmpty()) ? "data:image/jpeg;base64," + adminProfile : "assets/img/profile.jpg"%>" alt="..." class="avatar-img rounded-circle">
                                        </div>
                                        <span class="profile-username">
                                            <span class="op-7">Hi,</span> <span class="fw-bold"><%= doctor.getDoctorName()%></span>
                                        </span>
                                    </a>
                                    <ul class="dropdown-menu dropdown-user animated fadeIn">
                                        <div class="dropdown-user-scroll scrollbar-outer">
                                            <li>
                                                <div class="user-box">
                                                    <div class="avatar-lg"><img src="<%= (adminProfile != null && !adminProfile.isEmpty()) ? "data:image/jpeg;base64," + adminProfile : "assets/img/profile.jpg"%>" alt="image profile" class="avatar-img rounded"></div>
                                                    <div class="u-text">
                                                        <h4><%= doctor.getDoctorName()%></h4>
                                                    </div>
                                                </div>
                                            </li>
                                            <li>
                                                <div class="dropdown-divider"></div>
                                                <a class="dropdown-item" href="profile.jsp">My Profile</a>
                                                <a class="dropdown-item" href="AdminLogout">Logout</a>
                                            </li>
                                        </div>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </nav>

                </div>
