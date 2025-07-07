package tn.sopra.continuix.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tn.sopra.continuix.entities.Alerte;
import tn.sopra.continuix.entities.Group;
import tn.sopra.continuix.entities.PCA;
import tn.sopra.continuix.entities.Users;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        log.info("Attempting to send email to: {} with token: {}", to, token);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Set My Password");
            helper.setFrom(fromEmail, "SopraPCA");

            String resetUrl = frontendUrl + "/reset-password?token=" + token;

            String htmlContent = "<div style=\"font-family:Arial, sans-serif;\">" +
                    "<p>Bonjour,</p>" +
                    "<p>Veuillez cliquer sur le lien ci-dessous pour réinitialiser votre mot de passe :</p>" +
                    "<a href=\"" + resetUrl + "\" target=\"_blank\">Réinitialiser mon mot de passe</a>" +
                    "</div>";

            helper.setText(htmlContent, true);
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", to, e.getMessage(), e);
            throw new MessagingException("Failed to send email", e);
        }
    }

    public void sendNotificationEmail(String to, String subject, String htmlContent)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(fromEmail, "SopraPCA Notification");
        helper.setText(htmlContent, true);

        mailSender.send(email);
    }

    public void sendGroupNotificationAfterResolution(Group group, Alerte alerte, PCA pca, Users admin, Long notificationId) {
        String subject = "Incident Resolved - " + alerte.getType();

        String htmlContent = "<div style='font-family:Arial, sans-serif;'>" +
                "<p>Hello,</p>" +
                "<p>An alert of type <strong>" + alerte.getType() + "</strong> is currently <strong>being resolved</strong> by the incident management team.</p>" +
                "<p><strong>Alert details:</strong></p>" +
                "<ul>" +

                "<li><strong>Description:</strong> " + alerte.getDescription() + "</li>" +
                "<li><strong>Impact:</strong> " + alerte.getImpact() + "</li>" +
                "</ul>" +
                "<p><strong>Recommended action:</strong></p>" +
                "<p>" + pca.getRecommendedAction() + "</p>" +
                "<p>Sent by: <strong>" + admin.getUsername() + "</strong></p>" +
                "<p>We sincerely thank you for confirming receipt of this notification. Your response is important to help us manage incidents effectively.</p>" +
                "<p><a href='" + frontendUrl + "/notificationConsult/" + notificationId + "/view' target='_blank'>Acknowledge receipt</a></p>" +
                "</div>";

        for (Users user : group.getMembers()) {
            try {
                sendNotificationEmail(user.getEmail(), subject, htmlContent);
            } catch (Exception e) {
                log.error("Error sending group notification to {}: {}", user.getEmail(), e.getMessage());
            }
        }
    }

    public void sendGlobalNotificationAfterResolution(List<Users> allUsers, Alerte alerte, PCA pca, Users admin, Long notificationId) {
        String subject = "📢 General Notification - Incident Resolved";

        String htmlContent = "<div style='font-family:Arial, sans-serif;'>" +
                "<p><strong>Attention,</strong></p>" +
                "<p>An alert of type <strong>" + alerte.getType() + "</strong> was just <strong>resolved</strong>, but immediate attention is still required.</p>" +
                "<p><strong>Alert details:</strong></p>" +
                "<ul>" +
                "<li><strong>Description:</strong> " + alerte.getDescription() + "</li>" +
                "<li><strong>Impact:</strong> " + alerte.getImpact() + "</li>" +
                "</ul>" +
                "<p><strong>Recommended action:</strong></p>" +
                "<p>" + pca.getRecommendedAction() + "</p>" +
                "<p>Sent by: <strong>" + admin.getUsername() + "</strong></p>" +
                "<p>Please confirm receipt of this notification to help us ensure the issue is fully addressed.</p>" +
                "<p><a href='" + frontendUrl + "/notificationConsult/" + notificationId + "/view' target='_blank' " +
                "style='display:inline-block; padding:10px 15px; background-color:#d9534f; color:#ffffff; text-decoration:none; border-radius:5px;'>Confirm Receipt</a></p>" +
                "</div>";

        for (Users user : allUsers) {
            try {
                sendNotificationEmail(user.getEmail(), subject, htmlContent);
            } catch (Exception e) {
                log.error("Error sending global notification to {}: {}", user.getEmail(), e.getMessage());
            }
        }
    }

    public void sendAlerteNotification(Alerte alerte) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("khouloud.boussaha@esprit.tn");
        message.setSubject("New Incident Alert Created");

        StringBuilder text = new StringBuilder();
        text.append("Dear Team,Ramzi\n\n");
        text.append("Details:\n");
        text.append(" - Description: ").append(alerte.getDescription()).append("\n");
        text.append(" - Incident Type: ").append(alerte.getType()).append("\n");
        text.append(" - Impact Level: ").append(alerte.getImpact()).append("\n");
        text.append("Please review and take the necessary actions promptly.\n\n");
        text.append("Best regards,\n");
        text.append("Incident Management System");

        message.setText(text.toString());
        mailSender.send(message);
    }

}
