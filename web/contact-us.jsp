<%-- 
    Document   : contact-us.jsp
    Created on : Oct 5, 2024, 1:09:09 PM
    Author     : chanw
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Contact Us</title>
        <link rel="icon" href="img/favicon.png">
        <script src="js/jquery.min.js"></script>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <!-- Breadcrumbs -->
        <div class="breadcrumbs overlay">
            <div class="container">
                <div class="bread-inner">
                    <div class="row">
                        <div class="col-12">
                            <h2>Contact Us</h2>
                            <ul class="bread-list">
                                <li><a href="index.html">Home</a></li>
                                <li><i class="icofont-simple-right"></i></li>
                                <li class="active">Contact Us</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="container-xxl py-5" style="padding-bottom: 0px !important;">
            <div class="container">
                <div class="text-center mx-auto mb-5 wow fadeInUp" data-wow-delay="0.1s" style="max-width: 600px;">
                    <p class="d-inline-block border  py-1 px-4" style="border-radius: 16px; margin-bottom: 16px;">Contact Us</p>
                    <h1>Contact us for any inquiries or questions. We’re here to help!</h1>
                </div>
            </div>
        </div>
        <section class="contact-us mb-5">
            <div class="container">
                <div class="inner">
                    <div class="row"> 
                        <div class="col-lg-6">
                            <div class="contact-us-left">
                                <div id="myMap">
                                    <iframe src="https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d15934.127870130133!2d101.7277771!3d3.2167513!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31cc3843bfb6a031%3A0x2dc5e067aae3ab84!2sTunku%20Abdul%20Rahman%20University%20of%20Management%20and%20Technology%20(TAR%20UMT)!5e0!3m2!1sen!2smy!4v1728108676652!5m2!1sen!2smy" width="100%" height="100%" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="contact-us-form">
                                <h2>Contact With Us</h2>
                                <p>If you have any questions please fell free to contact with us.</p>
                                <div class="row">
                                    <div class="col-lg-12 mb-4">
                                        <div class="single-info">
                                            <i class="icofont icofont-ui-call"></i>
                                            <div class="content">
                                                <h3>012-3456789</h3>
                                                <p>chanwahhong827@gmail.com</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-12 mb-4">
                                        <div class="single-info">
                                            <i class="icofont-google-map"></i>
                                            <div class="content">
                                                <h3>Address</h3>
                                                <p>Tunku Abdul Rahman University of Management and Technology</p>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-12 mb-4">
                                        <div class="single-info">
                                            <i class="icofont icofont-wall-clock"></i>
                                            <div class="content">
                                                <h3>Monday - Sunday 8am - 8pm</h3>
                                                <p>Open Everyday</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <jsp:include page="footer.jsp" />
    </body>
</html>
