<%@page import="java.time.LocalDate"%>
<%@page import="java.time.temporal.ChronoUnit"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="model.*"%>
<%@page import="dao.*"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.List"%>
<%@page import="connection.DbConn"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:include page="adminHeader.jsp" />
<%
    Integer adminAuthID = (Integer) session.getAttribute("adminAuthID");

    if (adminAuthID == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int appointmentID = Integer.parseInt(request.getParameter("id"));

    AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    PatientDao patientDao = new PatientDao(DbConn.getConnection());
    DrugDao drugDao = new DrugDao(DbConn.getConnection());
    MedicalDrugDao medicalDrugDao = new MedicalDrugDao(DbConn.getConnection());
    MedicalDao medicalDao = new MedicalDao(DbConn.getConnection());
    AiDiagnosisDao aiDiagnosisDao = new AiDiagnosisDao(DbConn.getConnection());

    Appointment appointment = appointmentDao.getAppointmentById(appointmentID);
    Doctor doctor = doctorDao.getDoctor(appointment.getDoctorID());
    Patient patient = patientDao.getPatient(appointment.getPatientID());
    List<Drug> drugList = drugDao.getAllDrugAvailable();
    AiDiagnosis aiDiagnosis = aiDiagnosisDao.selectAiDiagnosisById(appointment.getDiagnosisID());

    StringBuilder symptomsDisplay = new StringBuilder();

    String successMsg = (String) session.getAttribute("successMsg");
    String errorMsg = (String) session.getAttribute("errorMsg");
    String errorDiagnosis = (String) session.getAttribute("errorDiagnosis");
    String errorMedicalFeeDesc = (String) session.getAttribute("errorMedicalFeeDesc");
    String errorMedicalFee = (String) session.getAttribute("errorMedicalFee");
    String errorDrug = (String) session.getAttribute("errorDrug");
    String errorQuantity = (String) session.getAttribute("errorQuantity");
%>

<form method="post" action="EditDoctorMedical">
    <div class="container">
        <div class="page-inner">
            <div class="page-header">
                <h3 class="fw-bold mb-3">Edit Appointment</h3>
                <ul class="breadcrumbs mb-3">
                    <li class="nav-item">
                        <a href="appointment.jsp">Appointment</a>
                    </li>
                    <li class="separator">
                        <i class="icon-arrow-right"></i>
                    </li>
                    <li class="nav-item">
                        <a href="editAppointment.jsp">Edit Appointment</a>
                    </li>
                </ul>
            </div>

            <% if (successMsg != null) {%>
            <div class="alert alert-success"><%= successMsg%></div>
            <%
                session.removeAttribute("successMsg");
            } else if (errorMsg != null) {%>
            <div class="alert alert-danger"><%= errorMsg%></div>
            <%
                    session.removeAttribute("errorMsg");
                }%>
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Appointment Details</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="id">ID</label>
                                        <input type="text" class="form-control" name="id" value="<%= appointment.getAppointmentID()%>" readonly/>
                                    </div>
                                    <div class="form-group">
                                        <label for="date">Appointment Date</label>
                                        <input type="text" class="form-control" name="date" value="<%= appointment.getAppointmentDate()%>" readonly/>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="doctor">Doctor In Charge</label>
                                        <input type="text" class="form-control" name="doctor" value="<%= doctor.getDoctorName()%>" readonly/>
                                    </div>
                                    <div class="form-group">
                                        <label for="time">Appointment Time</label>
                                        <input type="text" class="form-control" name="time" value="<%= appointment.getAppointmentTime()%>" readonly/>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="patient">Patient Name</label>
                                        <input type="text" class="form-control" name="patient" value="<%= patient.getPatientName()%>" readonly/>
                                    </div>
                                    <div class="form-group">
                                        <label for="status">Status</label>
                                        <select class="form-select" id="status" name="status" <%= (appointment != null && (appointment.getAppointmentStatus() == 1 || appointment.getAppointmentStatus() == 2)) ? "disabled" : ""%>>
                                            <option value="1" <%= (appointment != null && appointment.getAppointmentStatus() == 1) ? "selected" : ""%>>Complete</option>
                                            <option value="0" <%= (appointment != null && appointment.getAppointmentStatus() == 0) ? "selected" : ""%>>Upcoming</option>
                                            <%
                                                String appointmentDateStr = appointment.getAppointmentDate();
                                                LocalDate appointmentDate = LocalDate.parse(appointmentDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                                                LocalDate currentDate = LocalDate.now();
                                                long daysDifference = ChronoUnit.DAYS.between(currentDate, appointmentDate);

                                                if (daysDifference > 1) {
                                            %>
                                            <option value="2" <%= (appointment != null && appointment.getAppointmentStatus() == 2) ? "selected" : ""%>>Cancel</option>
                                            <%
                                                }
                                            %>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%
                if (aiDiagnosis != null) {
            %>
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">AI Diagnosis</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-12 col-lg-12">
                                    <%
                                        if (aiDiagnosis != null) {
                                            if (!aiDiagnosis.getSymptom1().isEmpty()) {
                                                symptomsDisplay.append(aiDiagnosis.getSymptom1());
                                            }
                                            if (!aiDiagnosis.getSymptom2().isEmpty()) {
                                                symptomsDisplay.append(symptomsDisplay.length() > 0 ? ", " + aiDiagnosis.getSymptom2() : aiDiagnosis.getSymptom2());
                                            }
                                            if (!aiDiagnosis.getSymptom3().isEmpty()) {
                                                symptomsDisplay.append(symptomsDisplay.length() > 0 ? ", " + aiDiagnosis.getSymptom3() : aiDiagnosis.getSymptom3());
                                            }
                                            if (!aiDiagnosis.getSymptom4().isEmpty()) {
                                                symptomsDisplay.append(symptomsDisplay.length() > 0 ? ", " + aiDiagnosis.getSymptom4() : aiDiagnosis.getSymptom4());
                                            }
                                            if (!aiDiagnosis.getSymptom5().isEmpty()) {
                                                symptomsDisplay.append(symptomsDisplay.length() > 0 ? ", " + aiDiagnosis.getSymptom5() : aiDiagnosis.getSymptom5());
                                            }
                                        }
                                    %>  
                                    <div class="form-group">
                                        <label for="id">Symptom</label>
                                        <input type="text" class="form-control" name="symptom" value="<%= symptomsDisplay.toString()%>" readonly/>
                                    </div>
                                    <div class="form-group">
                                        <label for="date">Diagnosis</label>
                                        <input type="text" class="form-control" name="aiDiagno" value="<%= aiDiagnosis.getPrimaryDiagnosis()%>" readonly/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%
                }
            %>

            <%
                if (appointment.getAppointmentStatus() != 2) {
            %>
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Medical Report</div>
                        </div>
                        <%
                            Medical medical = medicalDao.getMedicalByAppointmentID(appointment.getAppointmentID());
                            if (medical == null) {
                        %>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-6">
                                    <input type="hidden" class="form-control" name="id" value="<%= appointment.getAppointmentID()%>"/>
                                    <div class="form-group ${errorDiagnosis != null ? 'has-error' : ''}">
                                        <label for="diagnosis">Diagnosis</label>
                                        <textarea class="form-control" name="diagnosis" rows="5"><%= request.getAttribute("diagnosis") != null ? request.getAttribute("diagnosis") : ""%></textarea>
                                        <% if (errorDiagnosis != null) {%>
                                        </br><small class="form-text text-muted"><%= errorDiagnosis%></small>
                                        <%
                                                session.removeAttribute("errorDiagnosis");
                                            }
                                        %>
                                    </div>
                                    <div class="form-group ${errorMedicalFeeDesc != null ? 'has-error' : ''}">
                                        <label for="medicalFeeDesc">Medical Fee Description</label>
                                        <input class="form-control" name="medicalFeeDesc" value="<%= request.getAttribute("medicalFeeDesc") != null ? request.getAttribute("medicalFeeDesc") : ""%>"/>
                                        <% if (errorMedicalFeeDesc != null) {%>
                                        </br><small class="form-text text-muted"><%= errorMedicalFeeDesc%></small>
                                        <%
                                                session.removeAttribute("errorMedicalFeeDesc");
                                            }
                                        %>
                                    </div>
                                    <div class="form-group ${errorMedicalFee != null ? 'has-error' : ''}">
                                        <label for="medicalFee">Medical Fee</label>
                                        <input class="form-control" name="medicalFee" value="<%= request.getAttribute("medicalFee") != null ? request.getAttribute("medicalFee") : ""%>"/>
                                        <% if (errorMedicalFee != null) {%>
                                        </br><small class="form-text text-muted"><%= errorMedicalFee%></small>
                                        <%
                                                session.removeAttribute("errorMedicalFee");
                                            }
                                        %>
                                    </div>
                                </div>
                            </div>
                            <div id="drugContainer">
                                <%
                                    // Retrieve drugIds and quantities from the request
                                    String[] drugIds = (String[]) request.getAttribute("drugIds");
                                    String[] quantities = (String[]) request.getAttribute("quantities");
                                    if (drugIds != null && quantities != null && drugIds.length == quantities.length) {
                                        for (int i = 0; i < drugIds.length; i++) {
                                %>
                                <div class="row mb-3 ${errorDrug != null ? 'has-error' : ''}">
                                    <div class="col-md-4 col-lg-4">
                                        <label for="drug">Drug</label>
                                        <select class="form-control" name="drug[]">
                                            <option value="">Select Drug</option>
                                            <% for (Drug drug : drugList) {%>
                                            <option value="<%= drug.getDrugID()%>" 
                                                    <%= (drugIds != null && i < drugIds.length && String.valueOf(drug.getDrugID()).equals(drugIds[i])) ? "selected" : ""%>>
                                                <%= drug.getDrugName()%>
                                            </option>
                                            <% } %>
                                        </select>
                                        <% if (errorDrug != null) {%>
                                        <br><small class="form-text text-muted"><%= errorDrug%></small>
                                        <% }%>
                                    </div>

                                    <div class="col-md-4 col-lg-4 ${errorQuantity != null ? 'has-error' : ''}">
                                        <label for="qty">Quantity</label>
                                        <input type="number" class="form-control" name="qty[]" value="<%= quantities != null && i < quantities.length ? quantities[i] : ""%>" placeholder="Enter quantity" />
                                        <% if (errorQuantity != null) {%>
                                        <br><small class="form-text text-muted"><%= errorQuantity%></small>
                                        <% } %>
                                    </div>

                                    <div class="col-md-4 col-lg-2 align-content-center">
                                        <button type="button" class="btn btn-danger btn-sm remove-row">Remove</button>
                                    </div>
                                </div>
                                <%
                                    }
                                } else {
                                %>
                                <!-- If no drugs/quantities are provided, render an empty row -->
                                <div class="row mb-3">
                                    <div class="col-md-4 col-lg-4">
                                        <label for="drug">Drug</label>
                                        <select class="form-control" name="drug[]">
                                            <option value="">Select Drug</option>
                                            <%
                                                for (Drug drug : drugList) {
                                            %>
                                            <option value="<%= drug.getDrugID()%>"><%= drug.getDrugName()%></option>
                                            <% } %>
                                        </select>
                                    </div>
                                    <div class="col-md-4 col-lg-4">
                                        <label for="qty">Quantity</label>
                                        <input type="number" class="form-control" name="qty[]" placeholder="Enter quantity" />
                                    </div>
                                    <div class="col-md-4 col-lg-2 d-flex align-items-end">
                                        <button type="button" class="btn btn-danger btn-sm remove-row">Remove</button>
                                    </div>
                                </div>
                                <% } %>
                            </div>
                            <button type="button" id="addDrugRow" class="btn btn-primary mt-3">Add Drug</button>
                        </div>
                        <div class="card-action">
                            <button data-id="" data-bs-toggle="tooltip" title="Remove" class="btn btn-success submit-btn">
                                Submit
                            </button>
                            <a class="btn btn-danger" href="appointment.jsp">Cancel</a>
                        </div>
                        <%
                        } else {
                            List<MedicalDrug> medicalDrugList = medicalDrugDao.getMedicalDrugsByMedicalID(medical.getMedicalID());
                        %>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-6">
                                    <input type="hidden" class="form-control" name="id" value="<%= appointment.getAppointmentID()%>"/>
                                    <div class="form-group">
                                        <label for="diagnosis">Diagnosis</label>
                                        <textarea class="form-control" name="diagnosis" rows="5" disabled><%= medical.getDiagnosis()%></textarea>                                    
                                    </div>
                                    <div class="form-group">
                                        <label for="medicalFeeDesc">Medical Fee Description</label>
                                        <input class="form-control" name="medicalFeeDesc" value="<%= medical.getMedicalFeeDesc()%>" disabled/>
                                    </div>
                                    <div class="form-group">
                                        <label for="medicalFee">Medical Fee</label>
                                        <input class="form-control" name="medicalFee" value="<%= medical.getMedicalFee()%>" disabled/>                                    
                                    </div>
                                </div>
                            </div>
                            <div id="drugContainer">
                                <%
                                    for (MedicalDrug medicalDrug : medicalDrugList) {
                                %>
                                <div class="row mb-3">
                                    <div class="col-md-4 col-lg-4">
                                        <label for="drug">Drug</label>
                                        <select class="form-control" id="role" name="drug[]" disabled>
                                            <% for (Drug drug : drugList) {%>
                                            <option value="<%= drug.getDrugID()%>"
                                                    <%= (drug != null && drug.getDrugID() == medicalDrug.getDrugID()) ? "selected" : ""%>>
                                                <%= drug.getDrugName()%>
                                            </option>
                                            <% }%>
                                        </select>
                                    </div>
                                    <div class="col-md-4 col-lg-4">
                                        <label for="qty">Quantity</label>
                                        <input type="number" class="form-control" name="qty[]" value="<%= medicalDrug.getQuantity()%>" disabled />
                                    </div>
                                </div>
                                <%
                                    }
                                %>
                            </div>
                        </div>
                        <%
                            }
                        %>
                    </div>
                </div>
            </div>
            <%
            } else {
            %>
            <div style="margin-bottom: 241px">
            </div>
            <%
                }
            %>
        </div>
    </div>
</form>

<jsp:include page="adminFooter.jsp" />
<script>
    var SweetAlert2Demo = function () {

        var initDemos = function () {
            $('.submit-btn').click(function (e) {
                e.preventDefault();

                swal({
                    title: 'Are you sure?',
                    text: "You won't be able to edit this!",
                    type: 'warning',
                    buttons: {
                        confirm: {
                            text: 'Yes, submit it!',
                            className: 'btn btn-success'
                        },
                        cancel: {
                            visible: true,
                            className: 'btn btn-danger'
                        }
                    }
                }).then((Submit) => {
                    if (Submit) {
                        // Create a form element dynamically
                        let form = document.createElement('form');
                        form.method = 'POST';
                        form.action = 'EditDoctorMedical'; // Update to your servlet endpoint

                        // Gather all inputs in the current form
                        const formInputs = document.querySelectorAll('input[name], select[name], textarea[name]');

                        // Add each input as a hidden field in the dynamically created form
                        formInputs.forEach(input => {
                            let hiddenInput = document.createElement('input');
                            hiddenInput.type = 'hidden';
                            hiddenInput.name = input.name;
                            hiddenInput.value = input.value; // Use .value for input fields
                            form.appendChild(hiddenInput);
                        });

                        // Append the dynamic form to the body and submit it
                        document.body.appendChild(form);
                        form.submit();
                    } else {
                        swal.close();
                    }
                });
            });
        };

        return {
            init: function () {
                initDemos();
            },
        };
    }();

    jQuery(document).ready(function () {
        SweetAlert2Demo.init();
    });
</script>

<script>
    document.getElementById('addDrugRow').addEventListener('click', function () {
        const newRow = document.createElement('div');
        newRow.className = 'row mb-3';
        newRow.innerHTML = `
        <div class="col-md-4 col-lg-4">
            <label for="drug">Drug</label>
            <select class="form-control" id="role" name="drug[]">
                        <option value="">Select Drug</option>
    <% for (Drug drug : drugList) {%>
                <option value="<%= drug.getDrugID()%>"><%= drug.getDrugName()%></option>
    <%}%>
                </select>
        </div>
        <div class="col-md-4 col-lg-4">
            <label for="qty">Quantity</label>
            <input type="number" class="form-control" name="qty[]" placeholder="Enter quantity" />
        </div>
        <div class="col-md-4 col-lg-2 align-content-center">
            <button type="button" class="btn btn-danger btn-sm remove-row">Remove</button>
        </div>
    `;
        document.getElementById('drugContainer').appendChild(newRow);
    });

    document.addEventListener('click', function (event) {
        if (event.target && event.target.classList.contains('remove-row')) {
            event.target.closest('.row').remove();
        }
    });
</script>
