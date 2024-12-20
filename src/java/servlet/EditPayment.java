package servlet;

import connection.DbConn;
import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mail.DrugPaymentInvoiceSender;
import mail.PaymentInvoiceSender;
import mail.RefundPaymentSender;
import model.*;

public class EditPayment extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String paymentID = request.getParameter("id");
        int status = Integer.parseInt(request.getParameter("status"));
        int aptID = Integer.parseInt(request.getParameter("aptID"));
        String priceParam = request.getParameter("price");
        String payMethod = request.getParameter("payMethod");
        String desc = request.getParameter("desc");
        double price = 0.0;
        RequestDispatcher dispatcher = null;
        HttpSession session = request.getSession();

        if (priceParam == null || priceParam.trim().isEmpty()) {
            request.setAttribute("errorPrice", "Price is required.");
            dispatcher = request.getRequestDispatcher("editPayment.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            price = Double.parseDouble(priceParam);

            PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());
            AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            PatientDao patientDao = new PatientDao(DbConn.getConnection());

            Appointment appointment = appointmentDao.getAppointmentById(aptID);
            Services service = servicesDao.getService(appointment.getServicesID());
            Patient patient = patientDao.getPatient(appointment.getPatientID());
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            boolean isUpdated = paymentDao.updatePayment(paymentID, price, status);
            Payment payment = paymentDao.getPaymentByID(paymentID);

            if (isUpdated) {

                if (payment.getPaymentDesc().equals("Services & Drug Payment") && status == 1) {
                    DrugPaymentInvoiceSender.sendEmail(patient.getPatientEmail(), "Your payment invoice(" + currentDate + ")", paymentID, aptID, payMethod);
                } else {
                    if (status == 1) {
                        PaymentInvoiceSender.sendEmail(patient.getPatientEmail(), "Your payment invoice(" + currentDate + ")", paymentID, service.getServicesName(), service.getServicesPrice(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), "Cash");
                    }

                    if (status == 2) {
                        RefundPaymentSender.sendEmail(patient.getPatientEmail(), "Refund Confirmation", patient.getPatientName(), paymentID, price, currentDate, payMethod);
                    }
                }
                session.setAttribute("successMsg", "Payment updated successfully.");
            } else {
                session.setAttribute("errorMsg", "Failed to update Payment.");
            }

            response.sendRedirect("payment.jsp");

        } catch (NumberFormatException e) {
            request.setAttribute("errorPrice", "Price must be a valid number.");
            dispatcher = request.getRequestDispatcher("editPayment.jsp");
            dispatcher.forward(request, response);
            return;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditPayment.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EditPayment.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
