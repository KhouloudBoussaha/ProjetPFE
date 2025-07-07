package tn.sopra.continuix.services;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sopra.continuix.dtos.NotificationDTO;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.NotificationRecipientRepository;
import tn.sopra.continuix.repositories.NotificationRepository;
import tn.sopra.continuix.repositories.UserRepository;


import java.time.LocalDateTime;
import java.util.List;

@Service

@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private  UserRepository userRepository;
    private  NotificationRepository notificationRepository;
    private  NotificationRecipientRepository notificationRecipientRepository;
    private  EmailService emailService;

    @Override
    @Transactional
    public Notification createNotificationForUsers(List<Users> users, Alerte alerte, PCA pca, Users admin) {
        Notification notification = new Notification();
        notification.setTitle("Incident résolu - " + alerte.getType());
        notification.setMessage(pca.getRecommendedAction());
        notification.setIncidentType(alerte.getType());
        notification.setImpact(alerte.getImpact());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setCreatedBy(admin);
        notificationRepository.save(notification);

        for (Users user : users) {
            NotificationRecipient recipient = new NotificationRecipient();
            recipient.setNotification(notification);
            recipient.setRecipient(user);
            recipient.setAccepted(false);
            notificationRecipientRepository.save(recipient);
        }

        System.out.println("🟢 Création de la notification pour le groupe");
        return notification;
    }

    @Override
    public NotificationDTO viewNotification(Long notificationId, String email) {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));

        NotificationRecipient recipient = notificationRecipientRepository
                .findByNotificationAndRecipient(notification, users)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not assigned to user"));

        return new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getIncidentType(),
                notification.getImpact(),
                notification.getCreatedAt(),
                notification.getCreatedBy().getId(),
                users.getId(),
                recipient.getAccepted()
        );
    }

    @Override
    public void acknowledgeNotification(Long notificationId, String email) {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));

        NotificationRecipient recipient = notificationRecipientRepository
                .findByNotificationAndRecipient(notification, users)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not assigned to user"));

        if (Boolean.TRUE.equals(recipient.getAccepted())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already acknowledged");
        }

        recipient.setAccepted(true);
        recipient.setAcknowledgedAt(LocalDateTime.now());
        notificationRecipientRepository.save(recipient);
    }
}
