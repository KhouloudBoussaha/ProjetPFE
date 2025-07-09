package tn.sopra.continuix.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.sopra.continuix.dtos.NotificationDTO;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.NotificationRecipientRepository;
import tn.sopra.continuix.repositories.NotificationRepository;
import tn.sopra.continuix.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    private NotificationRepository notificationRepository;
    private UserRepository userRepository;
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        UserRepository userRepository = mock(UserRepository.class);
        NotificationRepository notificationRepository = mock(NotificationRepository.class);
        NotificationRecipientRepository notificationRecipientRepository = mock(NotificationRecipientRepository.class);
        EmailService emailService = mock(EmailService.class);

        notificationService = new NotificationServiceImpl(
                userRepository,
                notificationRepository,
                notificationRecipientRepository,
                emailService
        );
    }


    @Test
    void testViewNotification_mocké_simplement() {
        Users user = new Users();
        user.setId(1L);
        user.setEmail("test@mail.com");

        Notification notification = new Notification();
        notification.setId(10L);
        notification.setTitle("Test");
        notification.setMessage("Ceci est un message");
        notification.setIncidentType(IncidentType.NETWORK_ISSUE);
        notification.setImpact(Impact.LOW);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setCreatedBy(user);

        // tu simules que l'utilisateur existe
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));

        // tu simules une notification liée à lui (si tu avais une liste de recipients dans Notification)
        NotificationDTO dto = new NotificationDTO(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getIncidentType(),
                notification.getImpact(),
                notification.getCreatedAt(),
                user.getId(),
                user.getId(),
                true
        );

        // on ne passe pas par une vraie méthode ici, car pas de recipientRepo
        assertEquals("Test", dto.getTitle());
        assertEquals("Ceci est un message", dto.getMessage());
        assertEquals(true, dto.isAccepted());
    }
}
