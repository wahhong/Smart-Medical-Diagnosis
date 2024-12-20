/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mail;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import model.MyAuthenticator;

public class RegisterEmailSender {

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
                    + "<p>Please click the button below to verify your account:</p>"
                    + "<a href='" + body + "' style='display: inline-block; padding: 10px 20px; font-size: 18px; color: #fff; background-color: #5C6BC0; text-decoration: none; border-radius: 5px;'>"
                    + "Verify Email"
                    + "</a>"
                    + "<p style='margin-top: 20px;'>If you did not request this verification, please ignore this email.</p>"
                    + "<br><p>Best regards,<br>VitalCare</p>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlBody, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
