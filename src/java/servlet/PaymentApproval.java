package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import connection.DbConn;
import dao.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import mail.DoctorAppointmentSender;
import mail.PatientAppointmentSender;
import mail.PaymentInvoiceSender;
import model.*;
import static servlet.MakePayment.generateID;

public class PaymentApproval extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String paymentId = request.getParameter("paymentId");
        String payerId = request.getParameter("PayerID");
        int appointmentID = (Integer) session.getAttribute("appointmentID");
        Appointment appointment = (Appointment) session.getAttribute("appointment");
        Integer patientID = (Integer) session.getAttribute("userAuthID");
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String paymentID = generateID();
        
        session.removeAttribute("aiDiagnosis");
        session.removeAttribute("appointment");

        if (paymentId == null || payerId == null) {
            response.sendRedirect("payment.jsp");
            return;
        }

        try {
            APIContext apiContext = PayPalConfig.getAPIContext();

            Payment payment = new Payment();
            payment.setId(paymentId);

            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);

            Payment executedPayment = payment.execute(apiContext, paymentExecution);

            String paypalPaymentId = executedPayment.getId();

            AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
            PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());
            NotificationDao notificationDao = new NotificationDao(DbConn.getConnection());

            Services services = servicesDao.getService(appointment.getServicesID());
            Doctor doctor = doctorDao.getDoctor(appointment.getDoctorID());
            Patient patient = patientDao.getPatient(patientID);

            if ("approved".equals(executedPayment.getState())) {
                String notiDesc = patient.getPatientName() + " had make appointment at " + appointment.getAppointmentDate() + " (" + appointment.getAppointmentTime() + ")";
                String paymentDesc = "Make Appointment for " + services.getServicesName() + " Payment #" + paymentID;

                paymentDao.insertPayment(paymentID, paypalPaymentId, paymentDesc, appointmentID, services.getServicesPrice(), "paypal", 1);
                PaymentInvoiceSender.sendEmail(patient.getPatientEmail(), "Your payment invoice(" + currentDate + ")", paymentID, services.getServicesName(), services.getServicesPrice(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), "Paypal");

                notificationDao.insertNotification(appointmentID, doctor.getDoctorID(), "New Appointment", notiDesc, 0);

                PatientAppointmentSender.sendEmail(patient.getPatientEmail(), "You have make appointment successful", doctor.getDoctorName(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), services.getServicesName());
                DoctorAppointmentSender.sendEmail(doctor.getDoctorEmail(), "You have make appointment successful", doctor.getDoctorName(), patient.getPatientName(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), services.getServicesName());

                session.setAttribute("msgSuccess", "Payment was successful!");
                response.sendRedirect("home.jsp");
            } else {
                appointmentDao.updateAppointmentStatus(appointmentID, 2);

                session.setAttribute("msgError", "Payment failed. Please try again.");
                response.sendRedirect("payment.jsp");
            }
        } catch (PayPalRESTException e) {
            try {
                if (appointmentID > 0) {
                    AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
                    appointmentDao.updateAppointmentStatus(appointmentID, 2);
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(PaymentApproval.class.getName()).log(Level.SEVERE, null, ex);
            }

            session.setAttribute("msgError", "Error executing payment: " + e.getMessage());
            response.sendRedirect("payment.jsp");
        }  catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(PaymentApproval.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
