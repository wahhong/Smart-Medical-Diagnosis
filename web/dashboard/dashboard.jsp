<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
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

    PatientDao patientDao = new PatientDao(DbConn.getConnection());
    int patientAmount = patientDao.getPatientCount();
    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    int doctorAmount = doctorDao.getDoctorCount();
    AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
    int appointmentAmount = appointmentDao.getAppointmentCount();
    PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());
    double toatlSales = paymentDao.getTotalPaymentsMadeToday();
    List<Payment> paymentList = paymentDao.getRecentPaidPayments();
    DrugDao drugDao = new DrugDao(DbConn.getConnection());

    List<Payment> paymentAllList = paymentDao.getAllPaymentPaid();
    Calendar calendar = java.util.Calendar.getInstance();
    int currentYear = calendar.get(java.util.Calendar.YEAR);

    double[] monthlyTotals = new double[12];
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    for (Payment payment : paymentAllList) {
        String paymentDateString = payment.getPaymentDate();

        if (paymentDateString != null && !paymentDateString.isEmpty()) {
            try {
                Date paymentDate = dateFormat.parse(paymentDateString);
                calendar.setTime(paymentDate);

                int paymentYear = calendar.get(java.util.Calendar.YEAR);
                int paymentMonth = calendar.get(java.util.Calendar.MONTH);

                if (paymentYear == currentYear) {
                    monthlyTotals[paymentMonth] += payment.getPaymentAmount();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                out.println("<p>Error parsing date: " + paymentDateString + "</p>");
            }
        } else {
            out.println("<p>Invalid or null payment date: " + paymentDateString + "</p>");
        }
    }

    StringBuilder monthlyData = new StringBuilder();
    for (double total : monthlyTotals) {
        monthlyData.append(total).append(",");
    }

    if (monthlyData.length() > 0) {
        monthlyData.setLength(monthlyData.length() - 1);  // Remove trailing comma
    }

    List<Drug> drugList = drugDao.getAllDrugAvailable();

    StringBuilder labels = new StringBuilder();
    StringBuilder data = new StringBuilder();

    for (Drug drug : drugList) {
        labels.append("'").append(drug.getDrugName()).append("',");
        data.append(drug.getDrugQuantity()).append(",");
    }

    if (labels.length() > 0) {
        labels.setLength(labels.length() - 1);
    }
    if (data.length() > 0)
        data.setLength(data.length() - 1);
%>

<div class="container">
    <div class="page-inner">
        <div class="d-flex align-items-left align-items-md-center flex-column flex-md-row pt-2 pb-4">
            <div>
                <h3 class="fw-bold mb-3">Dashboard</h3>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-6 col-md-3">
                <div class="card card-stats card-round">
                    <div class="card-body">
                        <div class="row align-items-center">
                            <div class="col-icon">
                                <div class="icon-big text-center icon-primary bubble-shadow-small">
                                    <i class="fas fa-users"></i>
                                </div>
                            </div>
                            <div class="col col-stats ms-3 ms-sm-0">
                                <div class="numbers">
                                    <p class="card-category">Patient</p>
                                    <h4 class="card-title"><%= patientAmount%></h4>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-6 col-md-3">
                <div class="card card-stats card-round">
                    <div class="card-body">
                        <div class="row align-items-center">
                            <div class="col-icon">
                                <div class="icon-big text-center icon-info bubble-shadow-small">
                                    <i class="fas fa-user-check"></i>
                                </div>
                            </div>
                            <div class="col col-stats ms-3 ms-sm-0">
                                <div class="numbers">
                                    <p class="card-category">Doctor</p>
                                    <h4 class="card-title"><%= doctorAmount%></h4>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-6 col-md-3">
                <div class="card card-stats card-round">
                    <div class="card-body">
                        <div class="row align-items-center">
                            <div class="col-icon">
                                <div class="icon-big text-center icon-success bubble-shadow-small">
                                    <i class="fas fa-luggage-cart"></i>
                                </div>
                            </div>
                            <div class="col col-stats ms-3 ms-sm-0">
                                <div class="numbers">
                                    <p class="card-category">Sales</p>
                                    <h4 class="card-title"><%= String.format("%.2f", toatlSales)%></h4>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-sm-6 col-md-3">
                <div class="card card-stats card-round">
                    <div class="card-body">
                        <div class="row align-items-center">
                            <div class="col-icon">
                                <div class="icon-big text-center icon-secondary bubble-shadow-small">
                                    <i class="far fa-check-circle"></i>
                                </div>
                            </div>
                            <div class="col col-stats ms-3 ms-sm-0">
                                <div class="numbers">
                                    <p class="card-category">Appointment</p>
                                    <h4 class="card-title"><%= appointmentAmount%></h4>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="card card-round">
                    <div class="card-header">
                        <div class="card-head-row card-tools-still-right">
                            <div class="card-title">Transaction History</div>
                        </div>
                    </div>
                    <div class="card-body p-0">
                        <div class="table-responsive">
                            <!-- Projects table -->
                            <table class="table align-items-center mb-0">
                                <thead class="thead-light">
                                    <tr>
                                        <th scope="col">Payment Number</th>
                                        <th scope="col" class="text-start">Date & Time</th>
                                        <th scope="col" class="text-start">Amount</th>
                                        <th scope="col" class="text-start">Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        for (Payment payment : paymentList) {
                                    %>

                                    <tr>
                                        <th scope="row">
                                            <button class="btn btn-icon btn-round btn-success btn-sm me-2">
                                                <i class="fa fa-check"></i>
                                            </button>
                                            Payment from #<%= payment.getPaymentID()%>
                                        </th>
                                        <td class="text-start">
                                            <%= payment.getPaymentDate()%>
                                        </td>
                                        <td class="text-start">
                                            RM<%= String.format("%.2f", payment.getPaymentAmount())%>
                                        </td>
                                        <td class="text-start">
                                            <span class="badge badge-success">Completed</span>
                                        </td>
                                    </tr>
                                    <%
                                        }
                                    %>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="form-group">
                        <label for="yearSelect">Select Year</label>
                        <select id="yearSelect" class="form-control" onchange="updateYearData()">
                            <option value="<%= currentYear%>" selected><%= currentYear%></option>
                            <option value="<%= currentYear - 1%>"><%= currentYear - 1%></option>
                            <option value="<%= currentYear - 2%>"><%= currentYear - 2%></option>
                            <option value="<%= currentYear - 3%>"><%= currentYear - 3%></option>
                        </select>
                    </div>
                    <div class="card-header">
                        <div class="card-title">Sales Report</div>
                    </div>
                    <div class="card-body">
                        <div class="chart-container">
                            <canvas id="lineChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <div class="card">
                    <div class="card-header">
                        <div class="card-title">Drug</div>
                    </div>
                    <div class="card-body">
                        <div class="chart-container">
                            <canvas id="doughnutChart" style="width: 500px; height: 500px"></canvas>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="adminFooter.jsp" />
<script>
    var chartLabels = [<%= labels.toString()%>];
    var chartData = [<%= data.toString()%>];

    function getRandomColor() {
        return '#' + Math.floor(Math.random() * 16777215).toString(16).padStart(6, '0');
    }

    var backgroundColors = chartData.map(() => getRandomColor());

    var doughnutChart = document.getElementById('doughnutChart').getContext('2d');

    var myDoughnutChart = new Chart(doughnutChart, {
        type: 'doughnut',
        data: {
            datasets: [{
                    data: chartData,
                    backgroundColor: backgroundColors,
                }],
            labels: chartLabels
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                },
            },
            layout: {
                padding: {
                    left: 20,
                    right: 20,
                    top: 20,
                    bottom: 20
                }
            }
        }
    });

    var salesData = [<%= monthlyData.toString()%>];

    var lineChart = document.getElementById('lineChart').getContext('2d');

    var myLineChart = new Chart(lineChart, {
        type: 'line',
        data: {
            labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
            datasets: [{
                    label: "Monthly Sales (RM)",
                    borderColor: "#1d7af3",
                    pointBorderColor: "#FFF",
                    pointBackgroundColor: "#1d7af3",
                    pointBorderWidth: 2,
                    pointHoverRadius: 4,
                    pointHoverBorderWidth: 1,
                    pointRadius: 4,
                    backgroundColor: 'rgba(29, 122, 243, 0.1)',
                    fill: true,
                    borderWidth: 2,
                    data: salesData
                }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                },
            },
            layout: {
                padding: {
                    left: 20,
                    right: 20,
                    top: 20,
                    bottom: 20
                }
            },
            tooltips: {
                bodySpacing: 4,
                mode: "nearest",
                intersect: 0,
                position: "nearest",
                xPadding: 10,
                yPadding: 10,
                caretPadding: 10
            },
        }
    });

    function updateYearData() {
        var selectedYear = document.getElementById('yearSelect').value;

        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'getSalesData.jsp?year=' + selectedYear, true);
        xhr.onreadystatechange = function () {
            if (xhr.readyState == 4 && xhr.status == 200) {
                var response = JSON.parse(xhr.responseText);
                var newSalesData = response.salesData;
                var newLabels = response.labels;

                myLineChart.data.labels = newLabels;
                myLineChart.data.datasets[0].data = newSalesData;
                myLineChart.update();
            }
        };
        xhr.send();
    }
</script>
