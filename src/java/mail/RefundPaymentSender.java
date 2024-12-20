package mail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.MyAuthenticator;

public class RefundPaymentSender {
    public static void sendEmail(String recipient, String subject, String customerName, String paymentID, double refundAmount, String refundDate, String paymentMethod) {
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

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // HTML content for the email body
            String htmlBody = "<!DOCTYPE html>"
                + "<html>"
                + "<head><meta charset='UTF-8'><title>Refund Confirmation</title>"
                + "<style>body {font-family: Arial, sans-serif; margin: 20px; line-height: 1.5; color: #333;} .container {max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px; border-radius: 8px; background-color: #f9f9f9;} .header {text-align: center; margin-bottom: 20px;} .header img {width: 100px; margin-bottom: 10px;} .header h2 {margin: 0; font-size: 18px; color: #f44336;} .content p {margin: 10px 0;} .details-table {width: 100%; border-collapse: collapse; margin-top: 10px;} .details-table th, .details-table td {border: 1px solid #ddd; padding: 8px; text-align: left;} .details-table th {background-color: #f2f2f2;} .footer {text-align: center; margin-top: 20px; font-size: 12px; color: #666;} .footer a {color: #f44336; text-decoration: none;}</style></head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<img src='http://localhost:8080/Smart_Medical_Diagnostic_System/img/favicon.png' alt='Logo'>"
                + "<h2>Refund Confirmation</h2>"
                + "</div>"
                + "<div class='content'>"
                + "<p>Dear <strong>" + customerName + "</strong>,</p>"
                + "<p>We would like to inform you that your refund request has been successfully processed.</p>"
                + "<table class='details-table'>"
                + "<tr><th>Original Payment ID</th><td>" + paymentID + "</td></tr>"
                + "<tr><th>Refund Amount</th><td>RM " + String.format("%.2f", refundAmount) + "</td></tr>"
                + "<tr><th>Refund Processed On</th><td>" + now.format(formatter) + "</td></tr>"
                + "<tr><th>Payment Method</th><td>" + paymentMethod + "</td></tr>"
                + "</table>"
                + "<p>The refund amount will be credited back to your account within 3-5 business days, depending on your payment provider.</p>"
                + "<p>If you have any questions or concerns, please contact us at <a href='mailto:chanwahhong827@gmail.com'>chanwahhong827@gmail.com</a> or visit our <a href='http://localhost:8080/Smart_Medical_Diagnostic_System/contact-us'>Contact Us</a> page.</p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>Thank you for choosing <strong>VitalCare</strong>.</p>"
                + "<p><a href='http://localhost:8080/Smart_Medical_Diagnostic_System/home.jsp'>Visit our website</a></p>"
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
