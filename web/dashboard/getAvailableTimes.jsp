<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.time.*"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="model.Appointment"%>
<%@page import="java.util.List"%>
<%@page import="dao.AppointmentDao"%>
<%@page import="connection.DbConn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String selectedDate = request.getParameter("selectedDate");

    // Parse the selectedDate String into a Date object
    SimpleDateFormat simpleDateFormatInput = new SimpleDateFormat("yyyy-MM-dd");
    Date parsedDate = simpleDateFormatInput.parse(selectedDate);

    // Normalize the date to "yyyy-MM-dd" format
    SimpleDateFormat simpleDateFormatOutput = new SimpleDateFormat("yyyy-MM-dd");
    String normalizedSelectedDate = simpleDateFormatOutput.format(parsedDate);

    // Get the doctor ID from the request
    int doctorID = Integer.parseInt(request.getParameter("id"));

    // Fetch appointments using the normalized date
    AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
    List<Appointment> appointmentList = appointmentDao.getAppointmentTime(doctorID, normalizedSelectedDate);

    Set<String> bookedTimes = new HashSet<String>();
    for (Appointment appointment : appointmentList) {
        bookedTimes.add(appointment.getAppointmentTime());
    }

    String[] times = {
        "08:00:00", "08:30:00", "09:00:00", "09:30:00",
        "10:00:00", "10:30:00", "11:00:00", "11:30:00",
        "12:00:00", "12:30:00", "13:00:00", "13:30:00",
        "14:00:00", "14:30:00", "15:00:00", "15:30:00",
        "16:00:00", "16:30:00", "17:00:00", "17:30:00",
        "18:00:00", "18:30:00", "19:00:00", "19:30:00"
    };

    LocalDate currentDate = LocalDate.now();
    LocalTime currentTime = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    boolean firstAvailableSet = false;
//    out.println("<p>" + normalizedSelectedDate + " & " + currentDate + "</p>");
%>

<div class="container">
    <div class="time">
        <div class="row">
            <!-- AM Session -->
            <div class="col-12">
                <p class="fw-bold p-spacing">AM Session</p>
            </div>
            <%                for (String time : times) {
                    int hour = Integer.parseInt(time.substring(0, 2));
                    LocalTime slotTime = LocalTime.parse(time, formatter);
                    boolean isPast = normalizedSelectedDate.equals(currentDate.toString()) && slotTime.isBefore(currentTime);

                    if (hour < 12) {
                        boolean isBooked = bookedTimes.contains(time);
                        String period = "AM";
                        hour = (hour % 12 == 0) ? 12 : hour;
                        String formattedTime = String.format("%02d:%s %s", hour, time.substring(3, 5), period);
            %>
            <div class="col-md-3 col-3 col-sm-3 ps-0">
                <input type="radio" id="<%= time%>" name="appt_time" value="<%= time%>" 
                       <%= (isBooked || isPast) ? "disabled" : ""%> 
                       <%= !isBooked && !isPast && !firstAvailableSet ? "required" : ""%> >
                <label for="<%= time%>" class="time-label"><%= formattedTime%></label>
            </div>
            <%
                        if (!isBooked && !isPast && !firstAvailableSet) {
                            firstAvailableSet = true; // Mark the first available slot as required
                        }
                    }
                }
            %>

            <div class="col-12">
                <p class="fw-bold p-spacing">PM Session</p>
            </div>
            <%
                for (String time : times) {
                    int hour = Integer.parseInt(time.substring(0, 2));
                    LocalTime slotTime = LocalTime.parse(time, formatter);
                    boolean isPast = normalizedSelectedDate.equals(currentDate.toString()) && slotTime.isBefore(currentTime);

                    if (hour >= 12) {
                        boolean isBooked = bookedTimes.contains(time);
                        String period = "PM";
                        hour = (hour % 12 == 0) ? 12 : hour;
                        String formattedTime = String.format("%02d:%s %s", hour, time.substring(3, 5), period);
            %>
            <div class="col-md-3 col-3 col-sm-3 ps-0">
                <input type="radio" id="<%= time%>" name="appt_time" value="<%= time%>" 
                       <%= (isBooked || isPast) ? "disabled" : ""%> 
                       <%= !isBooked && !isPast && !firstAvailableSet ? "required" : ""%> >
                <label for="<%= time%>" class="time-label"><%= formattedTime%></label>
            </div>
            <%
                        if (!isBooked && !isPast && !firstAvailableSet) {
                            firstAvailableSet = true;
                        }
                    }
                }
            %>
        </div>
    </div>
</div>
