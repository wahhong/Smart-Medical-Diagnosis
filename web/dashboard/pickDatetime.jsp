<%@page import="model.Doctor"%>
<%@page import="dao.DoctorDao"%>
<%@page import="java.util.Base64"%>
<%@page import="model.Patient"%>
<%@page import="java.util.List"%>
<%@page import="dao.PatientDao"%>
<%@page import="connection.DbConn"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="adminHeader.jsp" />
<link rel="stylesheet" href="assets/css/calendar.css">

<%
    Integer adminAuthID = (Integer) session.getAttribute("adminAuthID");

    if (adminAuthID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String selectedDate = request.getParameter("selectedDate");
    String selectedTime = request.getParameter("selectedTime");
    int doctorID = Integer.parseInt(request.getParameter("doctorID"));
    int servicesID = Integer.parseInt(request.getParameter("servicesID"));
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
                    <a href="pickDoctor.jsp?servicesID=<%= servicesID%>">Select Doctor</a>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">Select Date & Time</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form action="MakeAppointment" method="post" id="appointmentForm">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Select Service</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-6">
                                    <div class="pick-head" style="padding-bottom: 20px;">
                                        <div class="col-6">
                                            <p class="fw-bold">
                                                <i class="fa fa-calendar required me-2"></i>
                                                Confirm Date
                                            </p>
                                        </div>
                                        <div class="col-6 text-end">
                                            <span class="date-selected select-sapcing">Selected</span>
                                            <span class="date-disabled ms-3">Not Available</span>
                                        </div>                  
                                    </div>
                                    <div class="container">
                                        <div class="calendar">
                                            <div class="header">
                                                <div class="btns">
                                                    <div class="cal-btn prev-btn">
                                                        <i class="fa fa-caret-left"></i>
                                                    </div>
                                                </div>
                                                <div class="month"></div>
                                                <div class="btns">
                                                    <div class="cal-btn next-btn">
                                                        <i class="fa fa-caret-right"></i>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="header">
                                                <input type="hidden" name="selectedDate" id="selectedDate" value="<%= selectedDate%>" onchange="loadAvailableTimes()" readonly>
                                                <input type="hidden" name="doctorID" id="doctorID" value="<%= doctorID%>" readonly>
                                                <input type="hidden" name="servicesID" id="servicesID" value="<%= servicesID%>" readonly>
                                            </div>
                                            <div class="weekdays">
                                                <div class="day">Sun</div>
                                                <div class="day">Mon</div>
                                                <div class="day">Tue</div>
                                                <div class="day">Wed</div>
                                                <div class="day">Thu</div>
                                                <div class="day">Fri</div>
                                                <div class="day">Sat</div>
                                            </div>
                                            <div class="days"></div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-6">
                                    <div class="pick-head" style="padding-bottom: 20px;">
                                        <div class="col-6">
                                            <p class="fw-bold">
                                                <i class="fa fa-clock-o required me-2"></i>
                                                Confirm Time
                                            </p>
                                        </div>
                                        <div class="col-6 text-end">
                                            <span class="time-selected select-sapcing">Selected</span>
                                            <span class="time-disabled ms-3">Not Available</span>
                                        </div>                  
                                    </div>
                                    <input type="hidden" name="selectedTime" id="selectedTime" value="<%= selectedTime%>" readonly>
                                    <div id="timeSlotsContainer"></div>
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

<script src="assets/js/calendar.js"></script>
<script>
    document.getElementById("appointmentForm").addEventListener("submit", function (event) {
        const radios = document.querySelectorAll("input[name='appt_time']");
        let isEnabled = Array.from(radios).some(radio => !radio.disabled);

        if (!isEnabled) {
            alert("No available time slots to select.");
            event.preventDefault(); // Prevent form submission
        }
    });
</script>
<jsp:include page="adminFooter.jsp" />
