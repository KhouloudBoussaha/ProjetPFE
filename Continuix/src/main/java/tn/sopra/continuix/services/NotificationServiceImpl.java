package tn.sopra.continuix.services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import tn.sopra.continuix.dtos.NotificationDTO;
import tn.sopra.continuix.entities.Notification;
import tn.sopra.continuix.entities.NotificationRecipient;
import tn.sopra.continuix.entities.User;
import tn.sopra.continuix.repositories.NotificationRecipientRepository;
import tn.sopra.continuix.repositories.NotificationRepository;
import tn.sopra.continuix.repositories.UserRepository;
import tn.sopra.continuix.request.NotificationRequest;
import tn.sopra.continuix.services.EmailService;
import tn.sopra.continuix.services.NotificationService;


import java.time.LocalDateTime;
import java.util.List;
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  NotificationRepository notificationRepository;
    @Autowired
    private  NotificationRecipientRepository notificationRecipientRepository;
    @Autowired
    private  EmailService emailService;

    public NotificationServiceImpl(UserRepository userRepository,
                                   NotificationRepository notificationRepository,
                                   NotificationRecipientRepository notificationRecipientRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.notificationRecipientRepository = notificationRecipientRepository;
        this.emailService = emailService;
    }
    @Override
    @Transactional
    public void createNotificationForAllUsers(NotificationRequest request) {
        Long adminId = 26L;
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setIncidentType(request.getIncidentType());
        notification.setImpact(request.getImpact());
        notification.setCreatedBy(admin);
        notification = notificationRepository.save(notification);

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            if (user.getId().equals(admin.getId())) continue;

            NotificationRecipient recipient = new NotificationRecipient();
            recipient.setNotification(notification);
            recipient.setRecipient(user);
            recipient.setAccepted(Boolean.FALSE);
            recipient.setAcknowledgedAt(null);
            notificationRecipientRepository.save(recipient);

            try {
                emailService.sendNotificationEmail(
                        user.getEmail(),
                        notification.getTitle(),
                        notification.getMessage(),
                        admin.getUsername(),
                        notification.getId()
                );
            } catch (Exception e) {
                System.err.println("Échec d'envoi de mail à " + user.getEmail() + ": " + e.getMessage());
            }
        }
    }

    @Override
    public NotificationDTO viewNotification(Long notificationId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));

        NotificationRecipient recipient = notificationRecipientRepository
                .findByNotificationAndRecipient(notification, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not assigned to user"));

        // Ne change rien ici, juste retourner les données
        return new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getIncidentType(),
                notification.getImpact(),
                notification.getCreatedAt(),
                notification.getCreatedBy().getId(),
                user.getId(),
                recipient.getAccepted()
        );
    }

    @Override
    public void acknowledgeNotification(Long notificationId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));

        NotificationRecipient recipient = notificationRecipientRepository
                .findByNotificationAndRecipient(notification, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not assigned to user"));

        if (Boolean.TRUE.equals(recipient.getAccepted())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already acknowledged");
        }

        recipient.setAccepted(true);
        recipient.setAcknowledgedAt(LocalDateTime.now());
        notificationRecipientRepository.save(recipient);
    }
}
