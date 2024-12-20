package mail;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import model.MyAuthenticator;

public class CancelAppointmentDoctorSender {

    public static void sendEmail(String recipient, String subject, String doctorName, String patientName, String appointmentDate, String appointmentTime) {
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

            // HTML body content with dynamic placeholders replaced
            String htmlBody = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head><meta charset='UTF-8'><title>Appointment Cancellation Notice</title><style>"
                    + "body { font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f4f4f4; color: #333; }"
                    + ".container { max-width: 600px; margin: 0 auto; padding: 20px; background-color: #fff; border: 1px solid #ddd; border-radius: 8px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); }"
                    + ".header { text-align: center; padding: 10px 0; }"
                    + ".header img { width: 80px; margin-bottom: 10px; }"
                    + ".header h2 { margin: 0; font-size: 22px; color: #e74c3c; }"
                    + ".content p { font-size: 16px; line-height: 1.6; margin: 10px 0; }"
                    + ".footer { text-align: center; margin-top: 20px; font-size: 12px; color: #888; }"
                    + ".footer a { color: #e74c3c; text-decoration: none; }"
                    + ".footer p { margin: 5px 0; }"
                    + ".highlight { font-weight: bold; color: #e74c3c; }"
                    + "</style></head>"
                    + "<body>"
                    + "<div class='container'>"
                    + "<div class='header'>"
                    + "<img src='logo.png' alt='VitalCare Logo'>"
                    + "<h2>Appointment Cancellation Notice</h2>"
                    + "</div>"
                    + "<div class='content'>"
                    + "<p>Dear Dr. " + doctorName + ",</p>"
                    + "<p>We are sorry to inform you that the appointment scheduled with <strong>" + patientName + "</strong> on <strong>" + appointmentDate + "</strong> at <strong>" + appointmentTime + "</strong> has been canceled.</p>"
                    + "<p>Please update your schedule accordingly. If you have any questions, please do not hesitate to contact us.</p>"
                    + "<p>Thank you for your attention to this matter!</p>"
                    + "<p>Best regards,</p>"
                    + "<p><strong>The VitalCare Team</strong></p>"
                    + "</div>"
                    + "<div class='footer'>"
                    + "<p>If you have any questions, feel free to visit our <a href='http://localhost:8080/Smart_Medical_Diagnostic_System/home.jsp'>website</a>.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlBody, "text/html; charset=UTF-8");

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
