package tn.sopra.continuix.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.sopra.continuix.entities.Alerte;
import tn.sopra.continuix.repositories.NotificationRepository;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private NotificationRepository notificationRepository;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        logger.info("Attempting to send email to: {} with token: {}", to, token);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Reset Your Password");
            helper.setFrom(fromEmail,"SopraPCA ");

            // Build the reset URL
            String resetUrl = frontendUrl + "/reset-password?token=" + token;

            // HTML content with placeholder for resetUrl
            String htmlContent = "<div style=\"font-family:Arial,Helvetica,sans-serif; line-height: 1.5; font-weight: normal; font-size: 15px; color: #2F3044; min-height: 100%; margin:0; padding:0; width:100%; background-color:#edf2f7\">"
                    + "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse:collapse;margin:0 auto; padding:0; max-width:600px\">"
                    + "<tbody>"
                    + "<tr>"
                    + "<td align=\"center\" valign=\"center\" style=\"text-align:center; padding: 40px\">"
                    + "<a href=\"https://soprapca.com\" rel=\"noopener\" target=\"_blank\">"
                    + "<img alt=\"Logo\" src=\"https://soprapca.com/assets/logo.png\"/>"
                    + "</a>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td align=\"left\" valign=\"center\">"
                    + "<div style=\"text-align:left; margin: 0 20px; padding: 40px; background-color:#ffffff; border-radius: 6px\">"
                    + "<div style=\"padding-bottom: 30px; font-size: 17px;\">"
                    + "<strong>Hello!Welcome to SopraPCA </strong>"
                    + "</div>"
                    + "<div style=\"padding-bottom: 30px\">"
                    + "You are receiving this email because you have been successfully added to our application.\n" +
                    "\n" +
                    "To activate your account, please click the button below to create your own password::"
                    + "</div>"
                    + "<div style=\"padding-bottom: 40px; text-align:center;\">"
                    + "<a href=\"" + resetUrl + "\" rel=\"noopener\" target=\"_blank\" "
                    + "style=\"text-decoration:none;display:inline-block;text-align:center;padding:0.75575rem 1.3rem;font-size:0.925rem;line-height:1.5;border-radius:0.35rem;color:#ffffff;background-color:#eb4034;border:0px;margin-right:0.75rem!important;font-weight:600!important;outline:none!important;vertical-align:middle\">"
                    + "Reset Password"
                    + "</a>"
                    + "</div>"
                    + "<div style=\"padding-bottom: 30px\">"
                    + "This password reset link will expire in 24 hours. "
                    + "If you did not request a password reset, no further action is required."
                    + "</div>"
                    + "<div style=\"border-bottom: 1px solid #eeeeee; margin: 15px 0\"></div>"
                    + "<div style=\"padding-bottom: 50px; word-wrap: break-all;\">"
                    + "<p style=\"margin-bottom: 10px;\">"
                    + "Button not working? Try pasting this URL into your browser:"
                    + "</p>"
                    + "<a href=\"" + resetUrl + "\" rel=\"noopener\" target=\"_blank\" style=\"text-decoration:none;color: #009ef7\">"
                    + resetUrl
                    + "</a>"
                    + "</div>"
                    + "<div style=\"padding-bottom: 10px\">"
                    + "Kind regards,<br>"
                    + "The SopraPCA Team."
                    + "</div>"
                    + "</div>"
                    + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td align=\"center\" valign=\"center\" style=\"font-size: 13px; text-align:center;padding: 20px; color: #6d6e7c;\">"
                    + "<p>123 Avenue of Innovation, Tunis, Tunisia.</p>"
                    + "<p>Copyright © <a href=\"https://soprapca.com\" rel=\"noopener\" target=\"_blank\">SopraPCA</a>.</p>"
                    + "</td>"
                    + "</tr>"
                    + "</tbody>"
                    + "</table>"
                    + "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            logger.error("Error sending email to {}: {}", to, e.getMessage(), e);
            throw new MessagingException("Failed to send email", e);
        }
    }
    public void sendNotificationEmail(String to, String title, String message, String adminName , Long notificationId) throws MessagingException, UnsupportedEncodingException {
        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("🚨 Nouvelle Notification - " + title);
        helper.setFrom(fromEmail, "SopraPCA Notification");

        String link = frontendUrl + "/notificationConsult/" + notificationId + "/view";

        String htmlContent = "<div style='font-family:Arial,sans-serif;padding:20px;background:#f5f6fa;color:#333;'>"
                + "<h2 style='color:#d63031;'>🚨 Nouvelle Notification d'incident</h2>"
                + "<p><strong>Titre :</strong> " + title + "</p>"
                + "<p><strong>Description :</strong><br>" + message + "</p>"
                + "<p><strong>Envoyé par :</strong> " + adminName + "</p>"
                + "<p style='margin-top:20px;'>"
                + "👉 <a href='" + link + "' style='display:inline-block;padding:10px 20px;color:#fff;background:#0984e3;border-radius:5px;text-decoration:none;'>Accuser réception</a>"
                + "</p>"
                + "<hr style='margin-top:30px;'>"
                + "<p style='font-size:12px;color:#888;'>SopraPCA - Veuillez ne pas répondre à cet e-mail automatique.</p>"
                + "</div>";

        helper.setText(htmlContent, true);
        mailSender.send(email);

    }
    public void sendAlerteNotification(Alerte alerte) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("khouloud.boussaha@esprit.tn");
        message.setSubject("Nouvelle Alerte Créée");
        message.setText("Une nouvelle alerte a été créée:\n\n" +
                "ID: " + alerte.getId() + "\n" +
                "Description: " + alerte.getDescription());

        mailSender.send(message);
    }

}
