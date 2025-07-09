package tn.sopra.continuix.services;

import org.junit.jupiter.api.*;
import org.mockito.*;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceImplTest {

    @InjectMocks private NotificationServiceImpl notificationService;

    @Mock private NotificationRepository notificationRepository;
    @Mock private NotificationRecipientRepository recipientRepository;
    @Mock private UserRepository userRepository;
    @Mock private EmailService emailService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNotificationForUsers_Success() {
        Alerte alerte = new Alerte();
        alerte.setType(IncidentType.AUTHENTICATION_ISSUE);
        alerte.setImpact(Impact.LOW);

        PCA pca = new PCA();
        pca.setRecommendedAction("Do this");

        Users admin = new Users();
        admin.setId(99L);

        Users u1 = new Users(); u1.setId(1L);
        Users u2 = new Users(); u2.setId(2L);
        List<Users> users = List.of(u1, u2);

        Notification notif = new Notification();
        when(notificationRepository.save(any())).thenReturn(notif);

        Notification result = notificationService.createNotificationForUsers(users, alerte, pca, admin);

        assertNotNull(result);
        verify(recipientRepository, times(2)).save(any());
    }
}
