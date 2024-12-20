<%@page import="model.Doctor"%>
<%@page import="dao.DoctorDao"%>
<%@page import="model.Services"%>
<%@page import="connection.DbConn"%>
<%@page import="dao.ServicesDao"%>
<%@page import="model.Appointment"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Appointment appointment = (Appointment) session.getAttribute("appointment");
    if (appointment == null) {
        response.sendRedirect("appointment.jsp");
        return;
    }

    int doctorID = appointment.getDoctorID();
    int servicesID = appointment.getServicesID();
    String selectedDate = appointment.getAppointmentDate();
    String selectedTime = appointment.getAppointmentTime();

    String payment = session.getAttribute("paymentMethod") != null ? (String) session.getAttribute("paymentMethod") : "";
    String msgError = session.getAttribute("msgError") != null ? (String) session.getAttribute("msgError") : "";
    session.removeAttribute("msgError");

    ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
    Services services = servicesDao.getService(servicesID);
    DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
    Doctor doctor = doctorDao.getDoctor(doctorID);

    double price = services.getServicesPrice();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="icon" href="img/favicon.png">
        <script src="https://js.stripe.com/v3/"></script>
        <script src="js/jquery.min.js"></script>
        <link rel="stylesheet" href="css/calendar.css">
        <link rel="stylesheet" href="css/payment.css">
        <link rel="stylesheet" href="css/notification.css">
        <title>Payment</title>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <div class="container-xxl py-5" style="padding-bottom: 0px !important;">
            <div class="container">
                <div class="text-center mx-auto mb-5 wow fadeInUp" data-wow-delay="0.1s" style="max-width: 600px;">
                    <p class="d-inline-block border py-1 px-4" style="border-radius: 16px; margin-bottom: 16px;">Appointment</p>
                    <h1>Payment</h1>
                </div>
            </div>
        </div>

        <form method="post" action="MakePayment" id="payment-form">
            <div class="container-xxl datetime-border">
                <div class="container">
                    <div class="row g-4">
                        <div class="col-md-7">
                            <h4>Checkout</h4>
                            <p>All plans include 40+ advanced tools and features to boost your product. Choose the best plan to fit your needs.</p>
                            <div class="mb-4">
                                <div class="d-flex gap-3">
                                    <label class="payment-option" for="creditCard">
                                        <input type="radio" name="paymentMethod" id="creditCard" value="creditCard">
                                        <i class="fab fa-cc-visa"></i>
                                        <span>Credit Card</span>
                                    </label>
                                    <label class="payment-option" for="paypal">
                                        <input type="radio" name="paymentMethod" id="paypal" value="paypal">
                                        <i class="fab fa-paypal"></i>
                                        <span>Paypal</span>
                                    </label>
                                </div>
                            </div>

                            <!-- Stripe Elements -->
                            <div id="creditCardInfo" class="d-none">
                                <h4 class="mb-1">Credit Card Info</h4>
                                <div id="card-element" class="form-group">
                                </div>
                                <p id="card-errors" class="text-danger"></p>
                            </div>
                            <input type="hidden" name="stripeToken" id="stripeToken">
                        </div>

                        <div class="col-md-5">
                            <h4>Order Summary</h4>
                            <p>Places check the appointment before make payment.</p>

                            <div class="border p-3 mb-3 rounded-3 bg-light">
                                <h4><%= services.getServicesName()%></h4>
                                <h5 class="mt-3">Doctor</h5>
                                <p class="mt-1"><%= doctor.getDoctorName()%></p>
                                <h5 class="mt-3">Appointment Date & Time</h5>
                                <p class="mt-1"><%= selectedDate%>(<%= selectedTime%>)</p>
                                <h4 class="mt-4">RM <%= String.format("%.2f", services.getServicesPrice())%></h4>
                            </div>

                            <div class="d-flex justify-content-between">
                                <p class="mb-1">Subtotal</p>
                                <p class="mb-1">RM <%= String.format("%.2f", services.getServicesPrice())%></p>
                            </div>
                            <hr>
                            <div class="d-flex justify-content-between fw-bold">
                                <p class="mb-1">Total</p>
                                <p class="mb-1">RM <%= String.format("%.2f", services.getServicesPrice())%></p>
                            </div>

                            <button type="submit" class="btn btn-lg w-100 mt-3">Proceed With Payment</button>
                            <p class="mt-3 text-muted small">By continuing, you accept our <a href="#">Terms of Services</a> and <a href="#">Privacy Policy</a>. Please note that payment is only for a deposit.</p>
                        </div>
                    </div>
                </div>
            </div>
        </form>

        <div class="container-xxl py-5">
            <div class="container">
                <div class="row">
                </div>
            </div>
        </div>
        <%
            if (msgError != null) {
        %>
        <div id="toastUnsuccess" class="toastUnsuccess"></div>
        <%
            }
        %>

        <script>
            var msgError = "<%= msgError != null ? msgError : ""%>";

            if (msgError) {
                showErrorToast(msgError);
            }

            function showErrorToast(message) {
                const toastUnsuccess = document.getElementById('toastUnsuccess');
                toastUnsuccess.textContent = message;
                toastUnsuccess.classList.add('show');

                setTimeout(() => {
                    toastUnsuccess.classList.remove('show');
                }, 6000);
            }
        </script>
        <script>
            const stripe = Stripe('pk_test_51PokIo008SpxbUMygaxA4tjVn8K555a7ydM7lSxGojm4UQL30NOFRZuGvv59PVgdGnukSYxoJvJFsfagXTl8wvIr00mNfV6D9L'); // Replace with your Stripe Publishable Key
            const elements = stripe.elements();
            const cardElement = elements.create('card');
            cardElement.mount('#card-element');

            const form = document.getElementById('payment-form');
            form.addEventListener('submit', async (event) => {
                // Get the selected payment method
                const selectedPaymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;

                if (selectedPaymentMethod === 'creditCard') {
                    // Prevent default form submission for credit card payments
                    event.preventDefault();

                    // Process payment using Stripe
                    const {paymentMethod, error} = await stripe.createPaymentMethod({
                        type: 'card',
                        card: cardElement,
                    });

                    if (error) {
                        document.getElementById('card-errors').textContent = error.message;
                    } else {
                        // Add Stripe token to the form and submit
                        document.getElementById('stripeToken').value = paymentMethod.id;
                        form.submit();
                    }
                } else {
                    // For other payment methods, allow form to submit normally
                    form.submit();
                }
            });

            // Handle payment method selection
            document.querySelectorAll('.payment-option').forEach(option => {
                option.addEventListener('click', () => {
                    const paymentMethod = option.querySelector('input[type="radio"]').id;

                    document.getElementById('creditCardInfo').classList.add('d-none');
                    if (paymentMethod === 'creditCard') {
                        document.getElementById('creditCardInfo').classList.remove('d-none');
                    }
                });
            });
        </script>
        <script>
            document.querySelectorAll('.payment-option').forEach(option => {
                option.addEventListener('click', () => {
                    document.querySelectorAll('.payment-option').forEach(opt => opt.classList.remove('active'));
                    option.classList.add('active');
                    option.querySelector('input[type="radio"]').checked = true;

                    const paymentMethod = option.querySelector('input[type="radio"]').id;

                    document.getElementById('billingDetails').classList.add('d-none');
                    
                    if (paymentMethod === 'paypal') {
                        document.getElementById('billingDetails').classList.remove('d-none');
                    }
                });
            });

            document.addEventListener('DOMContentLoaded', () => {
                const payment = "<%= payment != null ? payment : ""%>";

                let selectedOption;
                if (payment === "paypal") {
                    selectedOption = document.getElementById('paypal');
                } else {
                    selectedOption = document.getElementById('creditCard');
                }

                selectedOption.checked = true;
                selectedOption.parentElement.click();
            });
        </script>
        <jsp:include page="footer.jsp" />
    </body>
</html>