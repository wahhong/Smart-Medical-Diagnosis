<%@page import="model.Doctor"%>
<%@page import="dao.DoctorDao"%>
<%@page import="model.Appointment"%>
<%@page import="dao.AppointmentDao"%>
<%@page import="connection.DbConn"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.List"%>
<%@page import="model.Services"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%! String servicesImage;%>
<%
    Integer patientID = (Integer) session.getAttribute("userAuthID");
    if (patientID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    List<Appointment> appointmentList = appointmentDao.getAllAppointmentPatient(patientID);

    String successMsg = (String) session.getAttribute("successMsg");
    String errorMsg = (String) session.getAttribute("errorMsg");
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Appointment</title>
        <link rel="icon" href="img/favicon.png">
        <link rel="stylesheet" href="css/services.css">
        <link rel="stylesheet" href="css/pagination.css">
        <script src="js/jquery.min.js"></script>
        <style>
            #appointmentFilter {
                width: 300px;
                padding: 10px;
                border-radius: 5px;
                border: 1px solid #ced4da;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                font-size: 16px;
            }

            #appointmentFilter:focus {
                border-color: #007bff;
                box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
            }
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="container py-5">
            <h2 class="text-center mb-4">Your Appointments</h2>

            <div class="mb-4 d-flex justify-content-end">
                <input type="text" id="appointmentFilter" class="form-control" placeholder="Search">
            </div>

            <div class="table-responsive">
                <table class="table table-hover" id="appointmentTable">
                    <thead class="table-bordered-bd-black">
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Doctor</th>
                            <th scope="col">Date</th>
                            <th scope="col">Time</th>
                            <th scope="col">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            int index = 1;
                            for (Appointment appointment : appointmentList) {
                                Doctor doctor = doctorDao.getDoctor(appointment.getDoctorID());
                                String appointmentDetailsURL = "appointmentDetails.jsp?appointmentID=" + appointment.getAppointmentID();
                        %>
                        <tr style="cursor: pointer;" onclick="location.href = '<%= appointmentDetailsURL%>'" class="appointmentRow">
                            <th scope="row"><%= index++%></th>
                            <td><%= doctor.getDoctorName()%></td>
                            <td><%= appointment.getAppointmentDate()%></td>
                            <td><%= appointment.getAppointmentTime()%></td>
                            <td>
                                <% if (appointment.getAppointmentStatus() == 1) { %>
                                <span class="badge badge-success">Completed</span>
                                <% } else if (appointment.getAppointmentStatus() == 0) { %>
                                <span class="badge badge-warning">Upcoming</span>
                                <% } else if (appointment.getAppointmentStatus() == 2) { %>
                                <span class="badge badge-danger">Cancel</span>
                                <% }%>
                            </td>
                        </tr>
                        <% }%>
                    </tbody>
                </table>
            </div>

            <div id="paginationControls" class="d-flex justify-content-end my-4">
                <button id="prevPage" class="custom-btn custom-prev">Previous</button>
                <div id="pageNumbers" class="btn-group"></div>
                <button id="nextPage" class="custom-btn custom-next">Next</button>
            </div>

        </div>

        <jsp:include page="footer.jsp" />

        <script>
            const rowsPerPage = 10;
            let currentPage = 1;
            let filteredRows = []; // To hold the filtered rows
            let rows = document.querySelectorAll('.appointmentRow'); // All rows
            let totalRows = rows.length;
            let totalPages = Math.ceil(totalRows / rowsPerPage);

            function displayRows() {
                const tableBody = document.querySelector('#appointmentTable tbody');
                const noResultsMessage = document.getElementById('noResultsMessage');

                if (filteredRows.length === 0 && document.getElementById('appointmentFilter').value !== '') {
                    tableBody.style.display = 'none';
                    if (noResultsMessage) {
                        noResultsMessage.style.display = '';
                    }
                } else {
                    tableBody.style.display = '';
                    if (noResultsMessage) {
                        noResultsMessage.style.display = 'none';
                    }

                    const rowsToDisplay = filteredRows.length > 0 ? filteredRows : Array.from(rows);

                    rows.forEach((row) => {
                        row.style.display = 'none';
                    });

                    rowsToDisplay.forEach((row, index) => {
                        if (index >= (currentPage - 1) * rowsPerPage && index < currentPage * rowsPerPage) {
                            row.style.display = '';
                        }
                    });

                    updatePaginationButtons(rowsToDisplay);
                }
            }

            function updatePaginationButtons(rowsToDisplay) {
                const pageNumbersDiv = document.getElementById('pageNumbers');
                pageNumbersDiv.innerHTML = '';
                totalRows = rowsToDisplay.length;
                totalPages = Math.ceil(totalRows / rowsPerPage);

                for (let i = 1; i <= totalPages; i++) {
                    const pageButton = document.createElement('button');
                    pageButton.classList.add('custom-btn', 'pagination-btn');
                    pageButton.textContent = i;

                    if (i === currentPage) {
                        pageButton.classList.add('active-btn');
                    }

                    pageButton.addEventListener('click', () => {
                        currentPage = i;
                        displayRows();
                    });
                    pageNumbersDiv.appendChild(pageButton);
                }
            }

            function filterAppointments() {
                const filterValue = document.getElementById('appointmentFilter').value.toLowerCase();

                // If the filter value is empty, show all rows
                if (filterValue === '') {
                    filteredRows = Array.from(rows); // Reset filtered rows to all rows
                } else {
                    filteredRows = Array.from(rows).filter(row => {
                        const doctorName = row.cells[1].textContent.toLowerCase();
                        const appointmentDate = row.cells[2].textContent.toLowerCase();
                        const appointmentTime = row.cells[3].textContent.toLowerCase();
                        const status = row.cells[4].textContent.toLowerCase();

                        return doctorName.includes(filterValue) ||
                                appointmentDate.includes(filterValue) ||
                                appointmentTime.includes(filterValue) ||
                                status.includes(filterValue);
                    });
                }

                currentPage = 1;
                displayRows();
            }

            document.getElementById('prevPage').addEventListener('click', () => {
                if (currentPage > 1) {
                    currentPage--;
                    displayRows();
                }
            });

            document.getElementById('nextPage').addEventListener('click', () => {
                if (currentPage < totalPages) {
                    currentPage++;
                    displayRows();
                }
            });

            document.getElementById('appointmentFilter').addEventListener('input', filterAppointments);

            displayRows();
        </script>
    </body>
</html>

