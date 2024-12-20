<%@page import="java.time.temporal.ChronoUnit"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="model.Payment"%>
<%@page import="dao.PaymentDao"%>
<%@page import="model.Drug"%>
<%@page import="dao.DrugDao"%>
<%@page import="model.MedicalDrug"%>
<%@page import="dao.MedicalDrugDao"%>
<%@page import="model.Medical"%>
<%@page import="dao.MedicalDao"%>
<%@page import="model.AiDiagnosis"%>
<%@page import="dao.AiDiagnosisDao"%>
<%@page import="model.Doctor"%>
<%@page import="model.Appointment"%>
<%@page import="dao.AppointmentDao"%>
<%@page import="dao.DoctorDao"%>
<%@page import="connection.DbConn"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Integer patientID = (Integer) session.getAttribute("userAuthID");
    if (patientID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Integer appointmentID = Integer.parseInt(request.getParameter("appointmentID"));

    AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    AiDiagnosisDao aiDiagnosisDao = new AiDiagnosisDao(DbConn.getConnection());
    MedicalDao medicalDao = new MedicalDao(DbConn.getConnection());
    MedicalDrugDao medicalDrugDao = new MedicalDrugDao(DbConn.getConnection());
    DrugDao drugDao = new DrugDao(DbConn.getConnection());
    PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());

    Appointment appointment = appointmentDao.getAppointmentById(appointmentID);
    Doctor doctor = doctorDao.getDoctor(appointment.getDoctorID());
    AiDiagnosis aiDiagnosis = aiDiagnosisDao.selectAiDiagnosisById(appointment.getDiagnosisID());
    Medical medical = medicalDao.getMedicalByAppointmentID(appointmentID);
    List<Payment> paymentList = paymentDao.getAllPaymentByID(appointmentID);

    StringBuilder symptomsDisplay = new StringBuilder();
    String paymentMethod = "";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Appointment</title>
        <link rel="icon" href="img/favicon.png">
        <link rel="stylesheet" href="css/services.css">
        <script src="js/jquery.min.js"></script>
        <style>
            label {
                font-weight: bold;
                font-size: 1rem;
            }
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="container py-5">
            <h2 class="text-center mb-4">Appointment Details</h2>
            <div class="card mb-4">
                <div class="card-header">
                    <h5>Appointment Information</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="id">Appointment ID</label>
                                <p class="form-control-static"><%= appointment.getAppointmentID()%></p>
                            </div>
                            <div class="form-group">
                                <label for="date">Appointment Date</label>
                                <p class="form-control-static"><%= appointment.getAppointmentDate()%></p>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="doctor">Doctor In Charge</label>
                                <p class="form-control-static"><%= doctor.getDoctorName()%></p>
                            </div>
                            <div class="form-group">
                                <label for="time">Appointment Time</label>
                                <p class="form-control-static"><%= appointment.getAppointmentTime()%></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%
                if (paymentList != null) {
            %>
            <div class="card mb-4">
                <div class="card-header">
                    <h5>Payment</h5>
                </div>
                <%
                    for (Payment payment : paymentList) {
                %>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="id">Payment ID</label>
                                <p class="form-control-static"><%= payment.getPaymentID()%></p>
                            </div>
                            <div class="form-group">
                                <label for="date">Payment Desc</label>
                                <p class="form-control-static"><%= payment.getPaymentDesc()%></p>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="doctor">Amount</label>
                                <p class="form-control-static"><%= String.format("%.2f", payment.getPaymentAmount())%></p>
                            </div>
                            <div class="form-group">
                                <label for="time">Payment Method</label>
                                <%
                                    if (payment.getPaymentMethod().equals("cash")) {
                                        paymentMethod = "Cash";
                                    } else if (payment.getPaymentMethod().equals("creditCard")) {
                                        paymentMethod = "Credit Card";
                                    } else if (payment.getPaymentMethod().equals("paypal")) {
                                        paymentMethod = "Paypal";
                                    }
                                %>
                                <p class="form-control-static"><%= paymentMethod%></p>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="doctor">Status</label>
                                <%
                                    int paymentStatus = payment.getPaymentStatus();
                                    String paymentStatusString = "";
                                    if (paymentStatus == 1) {
                                        paymentStatusString = "Paid";
                                    } else if (paymentStatus == 0) {
                                        paymentStatusString = "Unpaid";
                                    } else if (paymentStatus == 2) {
                                        paymentStatusString = "Refund";
                                    } else if (paymentStatus == 3) {
                                        paymentStatusString = "In progress";
                                    }
                                %>
                                <p class="form-control-static"><%= paymentStatusString%></p>
                            </div>
                        </div>
                    </div>
                </div>
                <%
                    }
                %>
            </div>
            <%
                }
            %>
            <%
                if (medical != null) {
                    List<MedicalDrug> medicalDrugList = medicalDrugDao.getMedicalDrugsByMedicalID(medical.getMedicalID());
            %>
            <div class="card mb-4">
                <div class="card-header">
                    <h5>Medical Report</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-12">
                            <label for="diagnosis">Diagnosis</label>
                            <p class="form-control-static"><%= medical != null ? medical.getDiagnosis() : "No diagnosis available"%></p>
                        </div>
                    </div>
                    <div class="row mt-3">
                        <div class="col-md-6">
                            <label for="drug">Prescribed Drugs</label>
                            <ul class="list-group">
                                <% if (medicalDrugList != null) {
                                        for (MedicalDrug medicalDrug : medicalDrugList) {
                                            Drug drug = drugDao.getDrugById(medicalDrug.getDrugID());
                                %>
                                <li class="list-group-item">
                                    <%= drug.getDrugName()%> - <%= medicalDrug.getQuantity()%> units
                                </li>
                                <%
                                    }
                                } else { %>
                                <li class="list-group-item">No drugs prescribed.</li>
                                    <%
                                        }
                                    %>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <%
                }
            %>
            <div class="d-flex justify-content-between">
                <a class="btn btn-secondary" href="appointmentList.jsp">Back to Appointments</a>
                <%
                    String appointmentDateStr = appointment.getAppointmentDate();
                    LocalDate appointmentDate = LocalDate.parse(appointmentDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    LocalDate currentDate = LocalDate.now();
                    long daysDifference = ChronoUnit.DAYS.between(currentDate, appointmentDate);

                    if (appointment.getAppointmentStatus() != 2 && appointment.getAppointmentStatus() != 1 && daysDifference > 1) {
                %>
                <a class="btn" style="background-color: #dc3545;" href="CancelAppointment?id=<%= appointmentID%>">Cancel Appointments</a>
                <%
                    }
                %>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>
