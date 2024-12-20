package servlet;

import connection.DbConn;
import dao.AiDiagnosisDao;
import dao.AppointmentDao;
import dao.DoctorDao;
import dao.NotificationDao;
import dao.PatientDao;
import dao.PaymentDao;
import dao.ServicesDao;
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
import mail.DoctorAppointmentSender;
import mail.PatientAppointmentSender;
import mail.SetPasswordSender;
import model.AiDiagnosis;
import model.Appointment;
import model.Doctor;
import model.Patient;
import model.Services;

public class CheckPatientDetails extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        int aiDiagnosisID = 0;
        RequestDispatcher dispatcher = null;
        boolean isValid = true;
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();

        AiDiagnosis aiDiagnosis = (AiDiagnosis) session.getAttribute("aiDiagnosisDB");
        Appointment appointment = (Appointment) session.getAttribute("appointmentDB");

        if (email.isEmpty()) {
            request.setAttribute("errorEmail", "Please select email.");
            isValid = false;
        }

        if (!isValid) {
            dispatcher = request.getRequestDispatcher("patientDetails.jsp");
            dispatcher.forward(request, response);
            return;
        }

        try {
            PatientDao patientDao = new PatientDao(DbConn.getConnection());

            int patientID = patientDao.getPatientID(email);

            if (appointment == null) {
                response.sendRedirect("pickService.jsp");
                return;
            }

            AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
            AiDiagnosisDao aiDiagnosisDao = new AiDiagnosisDao(DbConn.getConnection());
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
            PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());
            NotificationDao notificationDao = new NotificationDao(DbConn.getConnection());

            Services services = servicesDao.getService(appointment.getServicesID());
            Doctor doctor = doctorDao.getDoctor(appointment.getDoctorID());
            String patientEmail = patientDao.getPatientEmail(patientID);
            Patient patient = patientDao.getPatient(patientID);
            String notiDesc = patient.getPatientName() + " had make appointment at " + appointment.getAppointmentDate() + " (" + appointment.getAppointmentTime() + ")";

            session.removeAttribute("aiDiagnosisDB");
            session.removeAttribute("appointmentDB");

            if (aiDiagnosis != null) {
                aiDiagnosisID = aiDiagnosisDao.insertAiDiagnosis(aiDiagnosis.getSymptom1(), aiDiagnosis.getSymptom2(), aiDiagnosis.getSymptom3(), aiDiagnosis.getSymptom4(), aiDiagnosis.getSymptom5(), aiDiagnosis.getPrimaryDiagnosis());
            }

            int appointmentID = appointmentDao.insertAppointment(appointment.getDoctorID(), appointment.getServicesID(), patientID, aiDiagnosisID, appointment.getAppointmentDate(), appointment.getAppointmentTime(), 0);

            if (appointmentID > 0) {
                paymentDao.insertPayment(generateID(), null, "Make Appointment for " + services.getServicesName(), appointmentID, services.getServicesPrice(), "cash", 0);
                notificationDao.insertNotification(appointmentID, doctor.getDoctorID(), "New Appointment", notiDesc, 0);

                PatientAppointmentSender.sendEmail(patientEmail, "You have make appointment successful", doctor.getDoctorName(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), services.getServicesName());
                DoctorAppointmentSender.sendEmail(doctor.getDoctorEmail(), "You have make appointment successful", doctor.getDoctorName(), patient.getPatientName(), appointment.getAppointmentDate(), appointment.getAppointmentTime(), services.getServicesName());

                session.setAttribute("successMsg", "Appointment has been added successfully.");
                response.sendRedirect("appointment.jsp");
            } else {
                session.setAttribute("errorMsg", "Appointment has been added unsuccessful");
                response.sendRedirect("appointment.jsp");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AddPatient.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred.");
        }
    }

    public static String generateID() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        return now.format(formatter);
    }
}
