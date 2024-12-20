package servlet;

import connection.DbConn;
import dao.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mail.CancelAppointmentDoctorSender;
import mail.CancelAppointmentSender;
import model.*;

public class EditDoctorMedical extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            RequestDispatcher dispatcher = null;
            PrintWriter out = response.getWriter();
            int appointmentID = Integer.parseInt(request.getParameter("id"));
            int status = Integer.parseInt(request.getParameter("status"));
            String diagnosis = request.getParameter("diagnosis");
            String medicalFeeStr = request.getParameter("medicalFee");
            String medicalFeeDesc = request.getParameter("medicalFeeDesc");
            String[] drugIds = request.getParameterValues("drug[]");
            String[] quantities = request.getParameterValues("qty[]");
            double paymentAmount = 0;
            boolean drugQuantity = false;
            double medicalFee = 0;
            boolean isValid = true;
            boolean drugValid = false;

            AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());
            MedicalDrugDao medicalDrugDao = new MedicalDrugDao(DbConn.getConnection());
            PaymentDao paymentDao = new PaymentDao(DbConn.getConnection());
            DrugDao drugDao = new DrugDao(DbConn.getConnection());
            MedicalDao medicalDao = new MedicalDao(DbConn.getConnection());
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            PatientDao patientDao = new PatientDao(DbConn.getConnection());
            DoctorDao doctorDao = new DoctorDao(DbConn.getConnection());
            NotificationDao notificationDao = new NotificationDao(DbConn.getConnection());

            if (status == 2) {
                appointmentDao.updateAppointmentStatus(appointmentID, status);
                Appointment appointment = appointmentDao.getAppointmentById(appointmentID);
                Services services = servicesDao.getService(appointment.getServicesID());
                Doctor doctor = doctorDao.getDoctor(appointment.getDoctorID());
                Patient patient = patientDao.getPatient(appointment.getPatientID());
                List<Payment> paymentList = paymentDao.getAllPaymentByID(appointmentID);

                CancelAppointmentSender.sendEmail(patient.getPatientEmail(), "Appointment Cancel successfully", patient.getPatientName(), services.getServicesName(), appointment.getAppointmentDate(), appointment.getAppointmentTime());
                CancelAppointmentDoctorSender.sendEmail(doctor.getDoctorEmail(), "Appointment Cancel By Patient", doctor.getDoctorName(), patient.getPatientName(), appointment.getAppointmentDate(), appointment.getAppointmentTime());
                String notiDesc = patient.getPatientName() + " had cancel appointment " + appointment.getAppointmentDate() + " (" + appointment.getAppointmentTime() + ")";
                notificationDao.insertNotification(appointmentID, doctor.getDoctorID(), "Cancel Appointment", notiDesc, 0);

                if (paymentList != null) {
                    for (Payment payment : paymentList) {
                        paymentDao.updateDeletePayment(payment.getPaymentID());
                    }
                }
            }

            if (diagnosis == null || diagnosis.trim().isEmpty()) {
                isValid = false;
                session.setAttribute("errorDiagnosis", "Diagnosis cannot be empty.");
            }
            
            if (medicalFeeDesc == null || medicalFeeDesc.trim().isEmpty()) {
                isValid = false;
                session.setAttribute("errorMedicalFeeDesc", "Description cannot be empty.");
            }

            if (medicalFeeStr == null || medicalFeeStr.trim().isEmpty()) {
                isValid = false;
                session.setAttribute("errorMedicalFee", "Medical Fee cannot be empty.");
            } else {
                try {
                    medicalFee = Double.parseDouble(medicalFeeStr);
                    if (medicalFee <= 0) {
                        isValid = false;
                        session.setAttribute("errorMedicalFee", "Medical Fee cannot less then 0.");
                    }
                } catch (NumberFormatException e) {
                    isValid = false;
                    session.setAttribute("errorMedicalFee", "Invalid Medical Fee format.");
                }
            }

            if ((drugIds == null || drugIds.length == 0) && (quantities == null || quantities.length == 0)) {
                drugValid = false;
            } else {
                drugValid = true;

                if (drugIds != null) {
                    for (String drugId : drugIds) {
                        if (drugId == null || drugId.trim().isEmpty()) {
                            isValid = false;
                            session.setAttribute("errorDrug", "Drug selection cannot be empty.");
                            break;
                        }
                    }
                }

                if (quantities != null) {
                    for (String quantity : quantities) {
                        try {
                            int qty = Integer.parseInt(quantity);
                            if (qty <= 0) {
                                isValid = false;
                                session.setAttribute("errorQuantity", "Quantities must be greater than zero.");
                                break;
                            }
                        } catch (NumberFormatException e) {
                            isValid = false;
                            session.setAttribute("errorQuantity", "Invalid quantity format.");
                            break;
                        }
                    }
                }
            }

            request.setAttribute("diagnosis", diagnosis);
            request.setAttribute("medicalFee", medicalFee);
            if (drugValid) {
                request.setAttribute("drugIds", drugIds);
                request.setAttribute("quantities", quantities);
            }

            if (!isValid) {
                dispatcher = request.getRequestDispatcher("editAppointment.jsp?id=" + appointmentID);
                dispatcher.forward(request, response);
                return;
            }

            boolean hasError = false;

            if (drugValid) {
                for (int i = 0; i < drugIds.length; i++) {
                    if (!drugIds[i].isEmpty() && !quantities[i].isEmpty()) {
                        int drugID = Integer.parseInt(drugIds[i]);
                        int quantity = Integer.parseInt(quantities[i]);

                        if (drugDao.isDrugStockSufficient(drugID, quantity)) {
                            drugQuantity = true;
                        } else {
                            drugQuantity = false;
                            Drug drug = drugDao.getDrugById(drugID);
                            session.setAttribute("errorMsg", "Insufficient stock for Drug: " + drug.getDrugName());
                            hasError = true;
                            break;
                        }
                    }
                }
            }
            
            if (hasError) {
                dispatcher = request.getRequestDispatcher("editAppointment.jsp?id=" + appointmentID);
                dispatcher.forward(request, response);
                return;
            }

            int medicalID = medicalDao.insertMedical(appointmentID, medicalFeeDesc, medicalFee, diagnosis);
            appointmentDao.updateAppointmentStatus(appointmentID, 1);

            if (medicalID > 0) {
                session.setAttribute("successMsg", "Appointment updated successfully.");
            }

            paymentAmount += medicalFee;

            if (drugValid) {
                if (drugQuantity) {
                    for (int i = 0; i < drugIds.length; i++) {
                        if (!drugIds[i].isEmpty() && !quantities[i].isEmpty()) {
                            int drugID = Integer.parseInt(drugIds[i]);
                            int quantity = Integer.parseInt(quantities[i]);

                            Drug drug = drugDao.getDrugById(drugID);

                            paymentAmount += drug.getDrugPrice() * quantity;
                            medicalDrugDao.insertMedicalDrug(medicalID, drugID, quantity);
                            drugDao.updateDrugQty(drugID, quantity);
                            session.setAttribute("successMsg", "Appointment updated successfully.");
                        }
                    }
                }
            }
            
            paymentDao.updateInsertPayment(appointmentID, paymentAmount);

            response.sendRedirect("appointment.jsp");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EditDoctorMedical.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
