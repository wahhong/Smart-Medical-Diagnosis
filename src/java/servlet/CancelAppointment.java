package servlet;

import connection.DbConn;
import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mail.CancelAppointmentDoctorSender;
import mail.CancelAppointmentSender;
import model.*;

public class CancelAppointment extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            int appointmentID = Integer.parseInt(request.getParameter("id"));

            AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
            PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());
            List<Payment> paymentList = paymentDao.getAllPaymentByID(appointmentID);

            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
            NotificationDao notificationDao = new NotificationDao(DbConn.getConnection());

            Appointment appointment = appointmentDao.getAppointmentById(appointmentID);
            Services services = servicesDao.getService(appointment.getServicesID());
            Doctor doctor = doctorDao.getDoctor(appointment.getDoctorID());
            Patient patient = patientDao.getPatient(appointment.getPatientID());

            boolean isUpdate = appointmentDao.updateAppointmentStatus(appointmentID, 2);

            if (isUpdate) {
                if (paymentList != null) {
                    for (Payment payment : paymentList) {
                        paymentDao.updateDeletePayment(payment.getPaymentID());
                    }
                }

                CancelAppointmentSender.sendEmail(patient.getPatientEmail(), "Appointment Cancel successfully", patient.getPatientName(), services.getServicesName(), appointment.getAppointmentDate(), appointment.getAppointmentTime());
                CancelAppointmentDoctorSender.sendEmail(doctor.getDoctorEmail(), "Appointment Cancel By Patient", doctor.getDoctorName(), patient.getPatientName(), appointment.getAppointmentDate(), appointment.getAppointmentTime());
                String notiDesc = patient.getPatientName() + " had cancel appointment " + appointment.getAppointmentDate() + " (" + appointment.getAppointmentTime() + ")";
                notificationDao.insertNotification(appointmentID, doctor.getDoctorID(), "Cancel Appointment", notiDesc, 0);

                session.setAttribute("successMsg", "Appointment cancel successful.");
            } else {
                session.setAttribute("errorMsg", "Failed to cancel appointment. Please try again.");
            }

            response.sendRedirect("appointmentList.jsp");

        } catch (ClassNotFoundException ex) {
            session.setAttribute("msgError", "Error occur");
            response.sendRedirect("home.jsp");
        } catch (SQLException ex) {
            session.setAttribute("msgError", "Error occur");
            response.sendRedirect("home.jsp");
        }
    }
}
