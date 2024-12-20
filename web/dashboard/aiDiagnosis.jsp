<%@page import="model.Appointment"%>
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

    Appointment appointment = (Appointment) session.getAttribute("appointmentDB");
    int doctorID = appointment.getDoctorID();
    int servicesID = appointment.getServicesID();
    String selectedDate = appointment.getAppointmentDate();
    String selectedTime = appointment.getAppointmentTime();

    String[] selectedSymptoms = new String[]{};

    String[] symptoms = {
        "itching", "skin_rash", "nodal_skin_eruptions", "continuous_sneezing", "shivering", "chills", "joint_pain",
        "stomach_pain", "acidity", "ulcers_on_tongue", "muscle_wasting", "vomiting", "burning_micturition",
        "spotting_urination", "fatigue", "weight_gain", "anxiety", "cold_hands_and_feets", "mood_swings",
        "weight_loss", "restlessness", "lethargy", "patches_in_throat", "irregular_sugar_level", "cough",
        "high_fever", "sunken_eyes", "breathlessness", "sweating", "dehydration", "indigestion", "headache",
        "yellowish_skin", "dark_urine", "nausea", "loss_of_appetite", "pain_behind_the_eyes", "back_pain",
        "constipation", "abdominal_pain", "diarrhoea", "mild_fever", "yellow_urine", "yellowing_of_eyes",
        "acute_liver_failure", "fluid_overload", "swelling_of_stomach", "swelled_lymph_nodes", "malaise",
        "blurred_and_distorted_vision", "phlegm", "throat_irritation", "redness_of_eyes", "sinus_pressure",
        "runny_nose", "congestion", "chest_pain", "weakness_in_limbs", "fast_heart_rate", "pain_during_bowel_movements",
        "pain_in_anal_region", "bloody_stool", "irritation_in_anus", "neck_pain", "dizziness", "cramps", "bruising",
        "obesity", "swollen_legs", "swollen_blood_vessels", "puffy_face_and_eyes", "enlarged_thyroid", "brittle_nails",
        "swollen_extremeties", "excessive_hunger", "extra_marital_contacts", "drying_and_tingling_lips", "slurred_speech",
        "knee_pain", "hip_joint_pain", "muscle_weakness", "stiff_neck", "swelling_joints", "movement_stiffness",
        "spinning_movements", "loss_of_balance", "unsteadiness", "weakness_of_one_body_side", "loss_of_smell",
        "bladder_discomfort", "foul_smell_of_urine", "continuous_feel_of_urine", "passage_of_gases", "internal_itching",
        "toxic_look_(typhos)", "depression", "irritability", "muscle_pain", "altered_sensorium", "red_spots_over_body",
        "belly_pain", "abnormal_menstruation", "dischromic_patches", "watering_from_eyes", "increased_appetite",
        "polyuria", "family_history", "mucoid_sputum", "rusty_sputum", "lack_of_concentration", "visual_disturbances",
        "receiving_blood_transfusion", "receiving_unsterile_injections", "coma", "stomach_bleeding", "distention_of_abdomen",
        "history_of_alcohol_consumption", "fluid_overload", "blood_in_sputum", "prominent_veins_on_calf", "palpitations",
        "painful_walking", "pus_filled_pimples", "blackheads", "scurring", "skin_peeling", "silver_like_dusting",
        "small_dents_in_nails", "inflammatory_nails", "blister", "red_sore_around_nose", "yellow_crust_ooze"
    };
%>
<div class="container">
    <div class="page-inner">
        <div class="page-header">
            <h3 class="fw-bold mb-3">Edit Service</h3>
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
                    <a href="pickDatetime.jsp?doctorID=<%= doctorID%>&servicesID=<%= servicesID%>">Select Date & Time</a>
                </li>
                <li class="separator">
                    <i class="icon-arrow-right"></i>
                </li>
                <li class="nav-item">
                    <a href="">AI Diagnosis</a>
                </li>
            </ul>
        </div>
        <div class="row">
            <div class="col-md-12">
                <form method="post" action="GenerateDiagnosis">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">Form</div>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="symptom1">Symptom 1</label>
                                        <select class="form-select symptom-box wide symptom" name="symptom[]" required>
                                            <option value="">Select your symptom</option>
                                            <%
                                                for (String symptom : symptoms) {
                                                    String optionText = symptom.replaceAll("_", " ");
                                                    String selected = "";
                                            %>
                                            <option value="<%= symptom%>" <%= selected%>><%= optionText%></option>
                                            <% } %>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="symptom4">Symptom 4</label>
                                        <select class="form-select symptom-box wide symptom" name="symptom[]">
                                            <option value="">Select your symptom</option>
                                            <%
                                                for (String symptom : symptoms) {
                                                    String optionText = symptom.replaceAll("_", " ");
                                            %>
                                            <option value="<%= symptom%>"><%= optionText%></option>
                                            <% }%>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="symptom2">Symptom 2</label>
                                        <select class="form-select symptom-box wide symptom" name="symptom[]">
                                            <option value="">Select your symptom</option>
                                            <%
                                                for (String symptom : symptoms) {
                                                    String optionText = symptom.replaceAll("_", " ");
                                            %>
                                            <option value="<%= symptom%>"><%= optionText%></option>
                                            <% }%>
                                        </select>
                                    </div>
                                    <div class="form-group">
                                        <label for="symptom5">Symptom 5</label>
                                        <select class="form-select symptom-box wide symptom" name="symptom[]">
                                            <option value="">Select your symptom</option>
                                            <%
                                                for (String symptom : symptoms) {
                                                    String optionText = symptom.replaceAll("_", " ");
                                            %>
                                            <option value="<%= symptom%>"><%= optionText%></option>
                                            <% }%>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6 col-lg-4">
                                    <div class="form-group">
                                        <label for="symptom3">Symptom 3</label>
                                        <select class="form-select symptom-box wide symptom" name="symptom[]">
                                            <option value="">Select your symptom</option>
                                            <%
                                                for (String symptom : symptoms) {
                                                    String optionText = symptom.replaceAll("_", " ");
                                            %>
                                            <option value="<%= symptom%>"><%= optionText%></option>
                                            <% }%>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="card-action">
                            <button class="btn btn-success">Make Appointment</button>
                            <a class="btn btn-danger" href="appointment.jsp">Cancel</a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<jsp:include page="adminFooter.jsp" />
