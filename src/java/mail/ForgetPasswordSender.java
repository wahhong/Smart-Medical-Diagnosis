package mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import model.MyAuthenticator;

public class ForgetPasswordSender {
    public static void sendEmail(String recipient, String subject, String body) {
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
            String htmlBody = "<html>"
                    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<h2 style='color: #5C6BC0;'>Verify Your Email Address</h2>"
                    + "<p>Hello,</p>"
                    + "<p>Please use the following verification code to complete your registration:</p>"
                    + "<div style='font-size: 24px; font-weight: bold; margin: 20px 0; padding: 10px; background-color: #f0f0f0; border-radius: 4px; width:80px;'>"
                    + body
                    + "</div>"
                    + "<p>If you did not request this code, please ignore this email.</p>"
                    + "<br><p>Best regards,<br>Your Website Team</p>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlBody, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
