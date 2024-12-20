<%@page import="dao.*"%>
<%@page import="connection.DbConn"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String msgError = session.getAttribute("msgError") != null ? (String) session.getAttribute("msgError") : "";
    String msgSuccess = session.getAttribute("msgSuccess") != null ? (String) session.getAttribute("msgSuccess") : "";
    session.removeAttribute("msgError");
    session.removeAttribute("msgSuccess");

    PatientDao patientDao = new PatientDao(DbConn.getConnection());
    int patientAmount = patientDao.getPatientCount();
    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    int doctorAmount = doctorDao.getDoctorCount();
    AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
    int appointmentAmount = appointmentDao.getAppointmentCount();

    int currentYear = java.time.Year.now().getValue();
    int yearDifference = currentYear - 2023;
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        <link rel="icon" href="img/favicon.png">
        <script src="js/jquery.min.js"></script>
        <link rel="stylesheet" href="css/notification.css">
    </head>
    <body>        
        <jsp:include page="header.jsp" />

        <!-- Slider Area -->
        <section class="slider">
            <div class="hero-slider">
                <!-- Start Single Slider -->
                <div class="single-slider" style="background-image:url('img/slider2.jpg')">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-7">
                                <div class="text">
                                    <h1>We Provide <span>Medical</span> Services That You Can <span>Trust!</span></h1>
                                    <p>Experience exceptional healthcare services designed to meet your needs. We combine expertise, compassion, and innovation to ensure your well-being is in safe hands.</p>
                                    <div class="button">
                                        <a href="appointment" class="btn">Get Appointment</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- End Single Slider -->
                <!-- Start Single Slider -->
                <div class="single-slider" style="background-image:url('img/slider.jpg')">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-7">
                                <div class="text">
                                    <h1>We Provide <span>Medical</span> Services That You Can <span>Trust!</span></h1>
                                    <p>Experience exceptional healthcare services designed to meet your needs. We combine expertise, compassion, and innovation to ensure your well-being is in safe hands.</p>
                                    <div class="button">
                                        <a href="appointment" class="btn">Get Appointment</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Start End Slider -->
                <!-- Start Single Slider -->
                <div class="single-slider" style="background-image:url('img/slider3.jpg')">
                    <div class="container">
                        <div class="row">
                            <div class="col-lg-7">
                                <div class="text">
                                    <h1>We Provide <span>Medical</span> Services That You Can <span>Trust!</span></h1>
                                    <p>Experience exceptional healthcare services designed to meet your needs. We combine expertise, compassion, and innovation to ensure your well-being is in safe hands.</p>
                                    <div class="button">
                                        <a href="appointment" class="btn">Get Appointment</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- End Single Slider -->
            </div>
        </section>
        <!--/ End Slider Area -->

        <!-- Start Schedule Area -->
        <section class="schedule">
            <div class="container">
                <div class="schedule-inner">
                    <div class="row">
                        <div class="col-lg-4 col-md-6 col-12 ">
                            <div class="single-schedule first">
                                <div class="inner">
                                    <div class="icon">
                                        <i class="fa fa-ambulance"></i>
                                    </div>
                                    <div class="single-content">
                                        <span>Emergency call</span>
                                        <h4>Emergency Cases</h4>
                                        <p>If you face a medical emergency, contact us immediately at <strong>012-3456789</strong>. Our team is here to help you.</p>
                                        <a href="contact-us.jsp">LEARN MORE<i class="fa fa-long-arrow-right"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="col-lg-4 col-md-6 col-12">
                            <div class="single-schedule middle">
                                <div class="inner">
                                    <div class="icon">
                                        <i class="icofont-calendar"></i>
                                    </div>
                                    <div class="single-content">
                                        <span>Book Your Appointment</span>
                                        <h4>Appointment Scheduling</h4>
                                        <p>Schedule your visit with our experienced healthcare professionals conveniently online.</p>
                                        <a href="appointment">LEARN MORE<i class="fa fa-long-arrow-right"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4 col-md-12 col-12">
                            <div class="single-schedule last">
                                <div class="inner">
                                    <div class="icon">
                                        <i class="icofont-ui-clock"></i>
                                    </div>
                                    <div class="single-content">
                                        <span>Open Time</span>
                                        <h4>Opening Hours</h4>
                                        <ul class="time-sidual">
                                            <li class="day">Monday-Friday <span>8.00am - 8.00pm</span></li>
                                            <li class="day">Saturday <span>8.00am - 8.00pm</span></li>
                                            <li class="day">Sunday <span>8.00am - 8.00pm</span></li>
                                        </ul>
                                        <a href="appointment">LEARN MORE<i class="fa fa-long-arrow-right"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!--/End Start schedule Area -->

        <!-- Start Feautes -->
        <section class="Feautes section">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section-title">
                            <h2>We Are Always Ready to Help You & Your Family</h2>
                            <img src="img/section-img.png" alt="#">
                            <p>Our team is committed to providing personalized care, ensuring the health and well-being of you and your family.</p>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-4 col-12">
                        <!-- Start Single features -->
                        <div class="single-features">
                            <div class="signle-icon">
                                <i class="icofont icofont-ambulance-cross"></i>
                            </div>
                            <h3>Emergency Help</h3>
                            <p>Immediate assistance for all medical emergencies, ensuring swift and effective care when you need it most.</p>
                        </div>
                        <!-- End Single features -->
                    </div>
                    <div class="col-lg-4 col-12">
                        <!-- Start Single features -->
                        <div class="single-features">
                            <div class="signle-icon">
                                <i class="icofont icofont-medical-sign-alt"></i>
                            </div>
                            <h3>Enriched Pharmecy</h3>
                            <p>Access a well-stocked pharmacy with quality medications to support your health and recovery.</p>
                        </div>
                        <!-- End Single features -->
                    </div>
                    <div class="col-lg-4 col-12">
                        <!-- Start Single features -->
                        <div class="single-features last">
                            <div class="signle-icon">
                                <i class="icofont icofont-stethoscope"></i>
                            </div>
                            <h3>Medical Treatment</h3>
                            <p>Comprehensive medical services tailored to address your unique healthcare needs with expertise and care.</p>
                        </div>
                        <!-- End Single features -->
                    </div>
                </div>
            </div>
        </section>
        <!--/ End Feautes -->

        <!-- Start Fun-facts -->
        <div id="fun-facts" class="fun-facts section overlay">
            <div class="container">
                <div class="row justify-content-center align-items-center">
                    <div class="col-lg-3 col-md-6 col-12">
                        <div class="single-fun">
                            <i class="icofont icofont-user-alt-3"></i>
                            <div class="content">
                                <span class="counter"><%= doctorAmount%></span>
                                <p>Specialist Doctors</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6 col-12">
                        <div class="single-fun">
                            <i class="icofont-simple-smile"></i>
                            <div class="content">
                                <span class="counter"><%= patientAmount%></span>
                                <p>Happy Patients</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-lg-3 col-md-6 col-12">
                        <div class="single-fun">
                            <i class="icofont icofont-table"></i>
                            <div class="content">
                                <span class="counter"><%= yearDifference%></span>
                                <p>Years of Experience</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!--/ End Fun-facts -->

        <!-- Start Why choose -->
        <section class="why-choose section" >
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section-title">
                            <h2>Comprehensive Services for Your Health and Well-Being</h2>
                            <img src="img/section-img.png" alt="Decoration">
                            <p>Discover a wide range of healthcare solutions designed to improve your quality of life and support your journey to better health.</p>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-6 col-12">
                        <!-- Start Choose Left -->
                        <div class="choose-left">
                            <h3>About Us</h3>
                            <p>We are committed to delivering exceptional healthcare services tailored to meet your unique needs. Our team combines compassion, expertise, and innovation to provide the best care possible.</p>
                            <p>Our approach emphasizes trust, collaboration, and excellence, ensuring that your health is always our top priority.</p>
                            <div class="row">
                                <div class="col-lg-6">
                                    <ul class="list">
                                        <li><i class="fa fa-caret-right"></i>Expert and caring staff.</li>
                                        <li><i class="fa fa-caret-right"></i>Advanced medical technology.</li>
                                        <li><i class="fa fa-caret-right"></i>Personalized treatment plans.</li>
                                    </ul>
                                </div>
                                <div class="col-lg-6">
                                    <ul class="list">
                                        <li><i class="fa fa-caret-right"></i>Full health services.</li>
                                        <li><i class="fa fa-caret-right"></i>Safe environment.</li>
                                        <li><i class="fa fa-caret-right"></i>Patient-focused care.</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <!-- End Choose Left -->
                    </div>
                    <div class="col-lg-6 col-12">
                        <!-- Start Choose Rights -->
                        <div class="choose-right">
                            <div class="video-image">
                                <iframe width="690" height="350" src="https://www.youtube.com/embed/DRN98lG8bGA?si=Dhs8707Tx0KEeOUp" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
                            </div>
                        </div>
                        <!-- End Choose Rights -->
                    </div>
                </div>
            </div>
        </section>


        <!-- Start Call to action -->
        <section class="call-action overlay" data-stellar-background-ratio="0.5">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 col-md-12 col-12">
                        <div class="content">
                            <h2>Do you need Emergency Medical Care? Call +6012-3456789</h2>
                            <p>We’re here to help you in emergencies. Contact us anytime for immediate medical care.</p>
                            <div class="button">
                                <a href="https://wa.me/+601136662982" class="btn">Contact Now</a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <section class="appointment" style="padding-top: 100px;">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="section-title">
                            <h2>We Are Always Ready to Help You. Book An Appointment</h2>
                            <img src="img/section-img.png" alt="#">
                            <p>Take the first step toward better health. Our dedicated team is here to provide compassionate and expert care for you and your family.</p>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-3 col-md-12 d-flex"></div>
                    <div class="col-lg-6 col-md-12 d-flex">
                        <div class="appointment-image">
                            <img src="img/contact-img.png" alt="#">
                        </div>
                    </div>
                </div>
                <div class="row mt-5 mb-5 d-flex justify-content-center align-items-center">
                    <a href="appointment" class="btn">Book Your Appointment Today</a>
                </div>
            </div>
        </section>

        <%
            if (msgError != null) {
        %>
        <div id="toastUnsuccess" class="toastUnsuccess"></div>
        <%
            }
            if (msgSuccess != null) {
        %>
        <div id="toastSuccess" class="toastSuccess"></div>
        <%
            }
        %>
        <jsp:include page="footer.jsp" />
        <script>
            var msgError = "<%= msgError != null ? msgError : ""%>";
            var msgSuccess = "<%= msgSuccess != null ? msgSuccess : ""%>";

            if (msgError) {
                showErrorToast(msgError);
            }
            if (msgSuccess) {
                showSuccessToast(msgSuccess);
            }

            function showErrorToast(message) {
                const toastUnsuccess = document.getElementById('toastUnsuccess');
                toastUnsuccess.textContent = message;
                toastUnsuccess.classList.add('show');

                setTimeout(() => {
                    toastUnsuccess.classList.remove('show');
                }, 3000);
            }

            function showSuccessToast(message) {
                const toastSuccess = document.getElementById('toastSuccess');
                toastSuccess.textContent = message;
                toastSuccess.classList.add('show');

                setTimeout(() => {
                    toastSuccess.classList.remove('show');
                }, 3000);
            }
        </script>
    </body>
</html>