<%@page import="model.Appointment"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.List"%>
<%@page import="model.Services"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Appointment appointment = (Appointment) session.getAttribute("appointment");
    int doctorID = appointment.getDoctorID();
    int servicesID = appointment.getServicesID();
    String selectedDate = appointment.getAppointmentDate();
    String selectedTime = appointment.getAppointmentTime();

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
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Appointment</title>
        <link rel="icon" href="img/favicon.png">
        <link rel="stylesheet" href="css/diagnosis.css">
        <link rel="stylesheet" href="css/calendar.css">
        <script src="js/jquery.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.1/dist/css/bootstrap.min.css" rel="stylesheet">
        <script>
            $(document).ready(function () {
                $('.symptom').select2();
            });
        </script>
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
                                <li class="active"><a href="appointment">Appointment</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="PickDoctor?servicesID=<%= servicesID%>">Pick Doctor</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="pickDatetime.jsp">Pick Date & Time</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active"><a href="aiDiagnosis.jsp">Symptom</a></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="container-xxl py-5" style="padding-bottom: 0px !important;">
            <div class="container">
                <div class="text-center mx-auto mb-5 wow fadeInUp" data-wow-delay="0.1s" style="max-width: 600px;">
                    <p class="d-inline-block border py-1 px-4" style="border-radius: 16px; margin-bottom: 16px;">Appointment</p>
                    <h1>Enter symptom</h1>
                </div>
            </div>
        </div>
        <form class="form" method="post" action="ListAiDiagnosis">
            <div class="container datetime-border">
                <div class="row g-4">
                    <div class="container">
                        <h6 style="padding: 25px 25px;">
                            This AI Diagnosis tool allows you to select symptoms based on the your current condition. Please note that this is only a assessment, designed to assist in gathering initial information.
                        </h6>
                    </div>
                    <div class="col-md-4 col-sm-4 col-4 mb-2 mt-2">
                        <div class="form-group">
                            <label for="symptom1" style="margin-left: 40px;">Symptom 1</label>
                            <select class="symptom-box wide symptom ml-5" name="symptom[]" required>
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
                    <div class="col-md-4 col-sm-4 col-4 mb-2 mt-2">
                        <div class="form-group">
                            <label for="symptom2" style="margin-left: 40px;">Symptom 2</label>
                            <select class="symptom-box wide symptom" name="symptom[]">
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
                    <div class="col-md-4 col-sm-4 col-4 mb-2 mt-2">
                        <div class="form-group">
                            <label for="symptom3" style="margin-left: 40px;">Symptom 3</label>
                            <select class="symptom-box wide symptom" name="symptom[]">
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
                    <div class="col-md-4 col-sm-4 col-4 mb-2 mt-2">
                        <div class="form-group">
                            <label for="symptom4" style="margin-left: 40px;">Symptom 4</label>
                            <select class="symptom-box wide symptom" name="symptom[]">
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
                    <div class="col-md-4 col-sm-4 col-4 mb-2 mt-2">
                        <div class="form-group">
                            <label for="symptom5" style="margin-left: 40px;">Symptom 5</label>
                            <select class="symptom-box wide symptom" name="symptom[]">
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
            <div class="container-xxl py-5">
                <div class="container">
                    <div class="row">
                        <div class="col-6">
                            <a href="appointment" class="cancel-btn">Cancel</a>
                        </div> 
                        <div class="col-6 d-flex justify-content-end">
                            <button type="submit" class="continue-btn">Make Payment</button>
                        </div> 
                    </div>
                </div>
            </div>
        </form>
        <jsp:include page="footer.jsp" />
    </body>
</html>
