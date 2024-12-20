package mail;

import connection.DbConn;
import dao.*;
import java.sql.SQLException;
import model.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.MyAuthenticator;

public class DrugPaymentInvoiceSender {

    public static void sendEmail(String recipient, String subject, String paymentID, int aptID, String paymentMethod) {
        String username = "chanwahhong827@gmail.com";
        String password = "kkiqofthghhjedys";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new MyAuthenticator(username, password));

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            MedicalDao medicalDao = new MedicalDao(DbConn.getConnection());
            MedicalDrugDao medicalDrugDao = new MedicalDrugDao(DbConn.getConnection());
            DrugDao drugDao = new DrugDao(DbConn.getConnection());
            ServicesDao servicesDao = new ServicesDao(DbConn.getConnection());
            AppointmentDao appointmentDao = new AppointmentDao(DbConn.getConnection());

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            double drugPrice = 0;
            double totalPrice = 0;

            Appointment appointment = appointmentDao.getAppointmentById(aptID);
            Medical medical = medicalDao.getMedicalByAppointmentID(aptID);
            List<MedicalDrug> medicalDrugList = medicalDrugDao.getMedicalDrugsByMedicalID(medical.getMedicalID());
            Services service = servicesDao.getService(appointment.getServicesID());

            String htmlBody = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<style>"
                    + "body {font-family: Arial, sans-serif; margin: 20px; line-height: 1.5; color: #333;}"
                    + ".container {max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px;}"
                    + ".header {text-align: center; margin-bottom: 20px;}"
                    + ".header img {width: 100px; margin-bottom: 10px;}"
                    + ".header h2 {margin: 0; font-size: 18px; color: #4caf50;}"
                    + ".content p {margin: 5px 0;}"
                    + ".table {width: 100%; border-collapse: collapse; margin-top: 10px;}"
                    + ".table th, .table td {border: 1px solid #ddd; padding: 8px; text-align: left;}"
                    + ".table th {background-color: #f2f2f2;}"
                    + ".footer {text-align: center; margin-top: 20px; font-size: 12px; color: #666;}"
                    + ".footer a {color: #4caf50; text-decoration: none;}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<div class='header'>"
                    + "<img src='http://localhost:8080/Smart_Medical_Diagnostic_System/img/favicon.png' alt='Logo'>"
                    + "<h2>Thank You!</h2>"
                    + "</div>"
                    + "<div class='content'>"
                    + "<p>You booked an appointment at VitalCare.</p>"
                    + "<p><strong>Invoice ID:</strong> " + paymentID + "</p>"
                    + "<p><strong>Payment Time:</strong> " + now.format(formatter) + "</p>"
                    + "<table class='table'>"
                    + "<tr>"
                    + "<th></th>"
                    + "<th>QTY</th>"
                    + "<th>Price</th>"
                    + "</tr>"
                    + "<tr>"
                    + "<td>" + medical.getMedicalFeeDesc() + "</td>"
                    + "<td></td>"
                    + "<td>RM " + String.format("%.2f", medical.getMedicalFee()) + "</td>"
                    + "</tr>";

            totalPrice += medical.getMedicalFee();

            for (MedicalDrug medicalDrug : medicalDrugList) {
                Drug drug = drugDao.getDrugById(medicalDrug.getDrugID());
                drugPrice = medicalDrug.getQuantity() * drug.getDrugPrice();
                htmlBody += "<tr>"
                        + "<td>" + drug.getDrugName() + "</td>"
                        + "<td>" + medicalDrug.getQuantity() + "</td>"
                        + "<td>RM " + String.format("%.2f", drug.getDrugPrice()) + "</td>"
                        + "</tr>";
                totalPrice += drugPrice;
            }

            htmlBody += "<tr>"
                    + "<td></td>"
                    + "<td colspan='1' style='text-align: right;'><strong>Total:</strong></td>"
                    + "<td><strong>RM " + String.format("%.2f", totalPrice) + "</strong></td>"
                    + "</tr>"
                    + "</table>"
                    + "<p><strong>Payment Method:</strong> " + paymentMethod + "</p>"
                    + "</div>"
                    + "<div class='footer'>"
                    + "<p>If you have any questions, please visit <a href='http://localhost:8080/Smart_Medical_Diagnostic_System/contact-us.jsp'>VitalCare</a> website.</p>"
                    + "<p>VitalCare Clinic</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlBody, "text/html; charset=UTF-8");
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DrugPaymentInvoiceSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DrugPaymentInvoiceSender.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
