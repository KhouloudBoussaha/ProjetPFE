package tn.sopra.continuix.services;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlerteServiceImplTest {

    @InjectMocks private AlerteServiceImpl alerteService;

    @Mock private AlerteRepository alerteRepository;
    @Mock private EmailService emailService;
    @Mock private UserRepository userRepository;
    @Mock private PCAService pcaService;
    @Mock private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAlerte_Success() {
        Alerte alerte = new Alerte();

        // Créer un UserDetails Spring Security avec email comme username
        UserDetails userDetails = new User("test@mail.com", "password", Collections.emptyList());

        // Mock Authentication qui retourne ce UserDetails en principal
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // Simuler userRepository pour retourner un Users avec le même email
        Users fakeUser = new Users();
        fakeUser.setEmail("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(fakeUser));

        when(alerteRepository.save(any())).thenReturn(alerte);

        Alerte result = alerteService.createAlerte(alerte);

        assertEquals(alerte, result);

        SecurityContextHolder.clearContext();
    }
}
