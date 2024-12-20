<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="model.Appointment"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="dao.AppointmentDao"%>
<%@page import="connection.DbConn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String selectedDate = request.getParameter("selectedDate");
    String selectedTime = request.getParameter("selectedTime");
    String servicesIDParam = request.getParameter("servicesID");
    String doctorIDParam = request.getParameter("doctorID");
    Integer patientID = (Integer) session.getAttribute("userAuthID");

    if (patientID == null) {
        response.sendRedirect("login.jsp");
        return;
    } else if (servicesIDParam == null || servicesIDParam.isEmpty() || doctorIDParam == null || doctorIDParam.isEmpty()) {
        response.sendRedirect("appointment");
        return;
    }

    int servicesID = Integer.parseInt(servicesIDParam);
    int doctorID = Integer.parseInt(doctorIDParam);

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Date & Time</title>
        <link rel="icon" href="img/favicon.png">
        <link rel="stylesheet" href="css/calendar.css">
        <script src="js/jquery.min.js"></script>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div class="breadcrumbs overlay">
            <div class="container">
                <div class="bread-inner">
                    <div class="row">
                        <div class="col-12">
                            <h2>Appointment</h2>
                            <ul class="bread-list">
                                <li><a href="home.jsp">Home</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="appointment.jsp">Appointment</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="pickDoctor.jsp?servicesID=<%= servicesID%>">Pick Doctor</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="pickDatetime.jsp">Pick Date & Time</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="container-xxl py-5" style="padding-bottom: 0px !important;">
            <div class="container">
                <div class="text-center mx-auto mb-5 wow fadeInUp" data-wow-delay="0.1s" style="max-width: 600px;">
                    <p class="d-inline-block border  py-1 px-4" style="border-radius: 16px; margin-bottom: 16px;">Appointment</p>
                    <h1>Select Available Date & Time</h1>
                </div>
            </div>
        </div>
        <form action="SubmitAppointment" method="post" id="appointmentForm">
            <div class="container-xxl datetime-border">
                <div class="container">
                    <div class="row g-4">
                        <div class="col-lg-6 col-md-6 wow fadeInUp">
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
                                        <div class="col-2">
                                            <div class="btns">
                                                <div class="cal-btn prev-btn">
                                                    <i class="fa fa-caret-left"></i>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-8 d-flex justify-content-center align-items-center text-center">
                                            <div class="month"></div>
                                        </div>
                                        <div class="col-2">
                                            <div class="btns">
                                                <div class="cal-btn next-btn">
                                                    <i class="fa fa-caret-right"></i>
                                                </div>
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
                        <div class="col-lg-6 col-md-6 wow fadeInUp">
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
            </div>
            <div class="container-xxl py-5">
                <div class="container">
                    <div class="row">
                        <div class="col-6">
                            <a href="pickDoctor.jsp?servicesID=<%= servicesID%>" class="cancel-btn">Cancel</a>
                        </div> 
                        <div class="col-6 d-flex justify-content-end">
                            <button type="submit" class="continue-btn">Next</button>
                        </div> 
                    </div>
                </div>
            </div>
        </form>

        <script src="js/calendar.js"></script>
        <script>
                                            document.getElementById("appointmentForm").addEventListener("submit", function (event) {
                                                const radios = document.querySelectorAll("input[name='appt_time']");
                                                let isEnabled = Array.from(radios).some(radio => !radio.disabled);

                                                if (!isEnabled) {
                                                    alert("No available time slots to select.");
                                                    event.preventDefault();
                                                }
                                            });
        </script>
        <jsp:include page="footer.jsp" />
    </body>
</html>
