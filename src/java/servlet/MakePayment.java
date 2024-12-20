package servlet;

import com.google.gson.*;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import connection.*;
import dao.*;
import model.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mail.DoctorAppointmentSender;
import mail.PatientAppointmentSender;
import static model.PayPalPayment.createPayment;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import java.util.HashMap;
import java.util.Map;
import mail.PaymentInvoiceSender;

@WebServlet(name = "MakePayment", urlPatterns = {"/MakePayment"})
public class MakePayment extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        String paymentMethod = request.getParameter("paymentMethod");
        String stripeToken = request.getParameter("stripeToken");
        int aiDiagnosisID = 0;

        AiDiagnosis aiDiagnosis = (AiDiagnosis) session.getAttribute("aiDiagnosis");
        Appointment appointment = (Appointment) session.getAttribute("appointment");
        Integer patientID = (Integer) session.getAttribute("userAuthID");

        if (appointment == null) {
            response.sendRedirect("appointment.jsp");
            return;
        }

        try {
            AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
            AiDiagnosisDao aiDiagnosisDao = new AiDiagnosisDao(DbConn.getConnection());
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
            PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());
            NotificationDao notificationDao = new NotificationDao(DbConn.getConnection());

            Services services = servicesDao.getService(appointment.getServicesID());
            Doctor doctor = doctorDao.getDoctor(appointment.getDoctorID());
            String patientEmail = patientDao.getPatientEmail(patientID);
            Patient patient = patientDao.getPatient(patientID);
            String paymentID = generateID();

            String notiDesc = patient.getPatientName() + " had make appointment at " + appointment.getAppointmentDate() + " (" + appointment.getAppointmentTime() + ")";
            String paymentDesc = "Make Appointment for " + services.getServicesName() + " Payment #" + paymentID;
            double totalPrice = services.getServicesPrice();
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (paymentMethod.equals("creditCard")) {
                try {
                    session.removeAttribute("aiDiagnosis");
                    session.removeAttribute("appointment");

                    if (aiDiagnosis != null) {
                        aiDiagnosisID = aiDiagnosisDao.insertAiDiagnosis(aiDiagnosis.getSymptom1(), aiDiagnosis.getSymptom2(), aiDiagnosis.getSymptom3(), aiDiagnosis.getSymptom4(), aiDiagnosis.getSymptom5(), aiDiagnosis.getPrimaryDiagnosis());
                    }

                    int appointmentID = appointmentDao.insertAppointment(appointment.getDoctorID(), appointment.getServicesID(), appointment.getPatientID(), aiDiagnosisID, appointment.getAppointmentDate(), appointment.getAppointmentTime(), 0);

                    if (appointmentID > 0) {
                        Stripe.apiKey = "sk_test_51PokIo008SpxbUMyv5f8dleFistDcJLFP3186zl5KwyAg2vXc6HVf6jxPaM8683iI4KmnLVpBh6aCiQ2G66Jiio200i1DsDHwG"; // Your Stripe Secret Key

                        Map<String, Object> params = new HashMap<>();
                        params.put("amount", (int) (totalPrice * 100));
                        params.put("currency", "myr");
                        params.put("payment_method", stripeToken);
                        params.put("confirmation_method", "automatic");
                        params.put("confirm", true);
                        params.put("return_url", "http://localhost:8080/Smart_Medical_Diagnostic_System/PaymentApprovalStripe");
                        params.put("description", paymentDesc);

                        PaymentIntent intent = PaymentIntent.create(params);

                        String paymentIntentId = intent.getId();

                        if ("requires_action".equals(intent.getStatus())) {
                            response.sendRedirect(intent.getNextAction().getRedirectToUrl().getUrl());
                        }

                        if ("succeeded".equals(intent.getStatus())) {
                            paymentDao.insertPayment(paymentID, paymentIntentId, paymentDesc, appointmentID, totalPrice, "creditCard", 1);
                            notificationDao.insertNotification(appointmentID, doctor.getDoctorID(), "New Appointment", notiDesc, 0);

                            PaymentInvoiceSender.sendEmail(patientEmail, "Your payment invoice(" + currentDate + ")", paymentID, services.getServicesName(), services.getServicesPrice(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), "Credit Card");
                            PatientAppointmentSender.sendEmail(patientEmail, "You have made an appointment successfully", doctor.getDoctorName(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), services.getServicesName());
                            DoctorAppointmentSender.sendEmail(doctor.getDoctorEmail(), "You have a new appointment", doctor.getDoctorName(), patient.getPatientName(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), services.getServicesName());

                            session.setAttribute("msgSuccess", "Payment successful!");
                            response.sendRedirect("home.jsp");
                        } else {
                            appointmentDao.updateAppointmentStatus(appointmentID, 2);

                            session.setAttribute("msgError", "Payment failed: " + intent.getStatus());
                            response.sendRedirect("payment.jsp");
                        }
                    } else {
                        session.setAttribute("msgError", "Payment failed: Due to the appointment had booked by other Please try again");
                        response.sendRedirect("home.jsp");
                    }

                } catch (StripeException e) {
                    session.removeAttribute("aiDiagnosis");
                    session.removeAttribute("appointment");

                    Logger.getLogger(MakePayment.class.getName()).log(Level.SEVERE, null, e);
                    session.setAttribute("msgError", "Stripe payment failed: " + e.getMessage());
                    response.sendRedirect("payment.jsp");
                } catch (Exception e) {
                    session.removeAttribute("aiDiagnosis");
                    session.removeAttribute("appointment");

                    Logger.getLogger(MakePayment.class.getName()).log(Level.SEVERE, null, e);
                    session.setAttribute("msgError", "An unexpected error occurred: " + e.getMessage());
                    response.sendRedirect("payment.jsp");
                }
            } else if (paymentMethod.equals("paypal")) {
                String cancelUrl = "http://localhost:8080/Smart_Medical_Diagnostic_System/PaymentApproval";
                String successUrl = "http://localhost:8080/Smart_Medical_Diagnostic_System/PaymentApproval";

                try {
                    if (aiDiagnosis != null) {
                        aiDiagnosisID = aiDiagnosisDao.insertAiDiagnosis(aiDiagnosis.getSymptom1(), aiDiagnosis.getSymptom2(), aiDiagnosis.getSymptom3(), aiDiagnosis.getSymptom4(), aiDiagnosis.getSymptom5(), aiDiagnosis.getPrimaryDiagnosis());
                    }

                    int appointmentID = appointmentDao.insertAppointment(appointment.getDoctorID(), appointment.getServicesID(), appointment.getPatientID(), aiDiagnosisID, appointment.getAppointmentDate(), appointment.getAppointmentTime(), 0);

                    if (appointmentID > 0) {

                        Payment payment = createPayment(totalPrice, "MYR", "paypal", "sale", paymentDesc, cancelUrl, successUrl);

                        for (Links link : payment.getLinks()) {
                            if (link.getRel().equals("approval_url")) {
                                session.setAttribute("appointmentID", appointmentID);
                                response.sendRedirect(link.getHref());
                                return;
                            }
                        }
                    } else {
                        session.setAttribute("msgError", "Payment failed: Due to the appointment had booked by other Please try again");
                        response.sendRedirect("home.jsp");
                    }
                } catch (PayPalRESTException e) {
                    session.setAttribute("msgError", "PayPal payment creation failed: " + e.getMessage());
                    response.sendRedirect("payment.jsp");
                    return;
                }
            } else {
                session.removeAttribute("aiDiagnosis");
                session.removeAttribute("appointment");

                if (aiDiagnosis != null) {
                    aiDiagnosisID = aiDiagnosisDao.insertAiDiagnosis(aiDiagnosis.getSymptom1(), aiDiagnosis.getSymptom2(), aiDiagnosis.getSymptom3(), aiDiagnosis.getSymptom4(), aiDiagnosis.getSymptom5(), aiDiagnosis.getPrimaryDiagnosis());
                }

                int appointmentID = appointmentDao.insertAppointment(appointment.getDoctorID(), appointment.getServicesID(), appointment.getPatientID(), aiDiagnosisID, appointment.getAppointmentDate(), appointment.getAppointmentTime(), 0);

                if (appointmentID > 0) {
                    paymentDao.insertPayment(paymentID, null, paymentDesc, appointmentID, totalPrice, paymentMethod, 0);
                    notificationDao.insertNotification(appointmentID, doctor.getDoctorID(), "New Appointment", notiDesc, 0);

                    PatientAppointmentSender.sendEmail(patientEmail, "You have make appointment successful", doctor.getDoctorName(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), services.getServicesName());
                    DoctorAppointmentSender.sendEmail(doctor.getDoctorEmail(), "You have make appointment successful", doctor.getDoctorName(), patient.getPatientName(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), services.getServicesName());

                    session.setAttribute("msgSuccess", "You have make appointment successful");
                    response.sendRedirect("home.jsp");
                } else {
                    out.println("appointmentID: " + appointmentID);
                    session.setAttribute("msgError", "You have make appointment unsuccessful");
                    response.sendRedirect("home.jsp");
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MakePayment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String generateID() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        return now.format(formatter);
    }
}
