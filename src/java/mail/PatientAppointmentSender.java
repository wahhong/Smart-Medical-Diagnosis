package mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.MyAuthenticator;

public class PatientAppointmentSender {

    public static void sendEmail(String recipient, String subject, String doctorName, String appointmentDate, String appointmentTime, String servicesName) {
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
            String htmlBody = "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<style>"
                    + "body {"
                    + "font-family: Arial, sans-serif;"
                    + "margin: 0;"
                    + "padding: 0;"
                    + "background-color: #f9f9f9;"
                    + "}"
                    + ".email-container {"
                    + "max-width: 600px;"
                    + "margin: 20px auto;"
                    + "background-color: #ffffff;"
                    + "border: 1px solid #ddd;"
                    + "border-radius: 8px;"
                    + "box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);"
                    + "overflow: hidden;"
                    + "}"
                    + ".email-header {"
                    + "background-color: #1A76D1;"
                    + "color: white;"
                    + "text-align: center;"
                    + "padding: 20px;"
                    + "}"
                    + ".email-header h1 {"
                    + "margin: 0;"
                    + "font-size: 24px;"
                    + "}"
                    + ".email-body {"
                    + "padding: 20px;"
                    + "color: #333;"
                    + "}"
                    + ".email-body h2 {"
                    + "color: #1A76D1;"
                    + "font-size: 20px;"
                    + "margin-top: 0;"
                    + "}"
                    + ".email-body p {"
                    + "font-size: 16px;"
                    + "line-height: 1.6;"
                    + "}"
                    + ".appointment-details {"
                    + "background-color: #f3f3f3;"
                    + "padding: 15px;"
                    + "border-radius: 8px;"
                    + "margin-top: 20px;"
                    + "}"
                    + ".appointment-details p {"
                    + "margin: 8px 0;"
                    + "font-size: 16px;"
                    + "font-weight: bold;"
                    + "}"
                    + ".email-footer {"
                    + "background-color: #f3f3f3;"
                    + "text-align: center;"
                    + "padding: 15px;"
                    + "font-size: 14px;"
                    + "color: #555;"
                    + "}"
                    + ".email-footer a {"
                    + "color: #1A76D1;"
                    + "text-decoration: none;"
                    + "}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<div class=\"email-container\">"
                    + "<div class=\"email-header\">"
                    + "<h1>Appointment Confirmation</h1>"
                    + "</div>"
                    + "<div class=\"email-body\">"
                    + "<h2>Dear Patient,</h2>"
                    + "<p>We are pleased to inform you that your appointment has been successfully booked. Below are the details of your appointment:</p>"
                    + "<div class=\"appointment-details\">"
                    + "<p><strong>Doctor:</strong> " + doctorName + "</p>"
                    + "<p><strong>Services:</strong> " + servicesName + "</p>"
                    + "<p><strong>Date:</strong> " + appointmentDate + "</p>"
                    + "<p><strong>Time:</strong> " + appointmentTime + "</p>"
                    + "</div>"
                    + "<p>If you have any questions or need to reschedule, please feel free to contact us.</p>"
                    + "<p>Thank you for choosing our clinic!</p>"
                    + "</div>"
                    + "<div class=\"email-footer\">"
                    + "<p>This is an automated message. Please do not reply to this email.</p>"
                    + "<p>For assistance, visit our <a href=\"http://localhost:8080/Smart_Medical_Diagnostic_System/home.jsp\">website</a> or call us at +60123456789.</p>"
                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlBody, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
