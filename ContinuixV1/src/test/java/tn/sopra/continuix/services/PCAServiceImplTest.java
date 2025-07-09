package tn.sopra.continuix.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.*;
import tn.sopra.continuix.request.SimulationRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PCAServiceImplTest {

    @InjectMocks
    private PCAServiceImpl pcaService;

    @Mock
    private PCARepository pcaRepository;

    @Mock
    private GroupRepository groupRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AlerteRepository alerteRepository;
    @Mock
    private NotificationService  notificationService;
    @Mock
    private EmailService  emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPCA_Success() {
        String groupName = "Groupe A";
        Group group = new Group();
        group.setName(groupName);

        PCA pca = new PCA();
        pca.setIncidentType("Incident");
        pca.setImpact("Important");

        when(groupRepository.findByName(groupName)).thenReturn(group);
        when(pcaRepository.save(any(PCA.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PCA result = pcaService.addPCA(pca, groupName);

        assertNotNull(result);
        assertEquals("Incident_Important", result.getLabel());
        assertEquals(group, result.getGroupe());
        verify(pcaRepository).save(pca);
    }

    @Test
    void testGetPCAByIncidentTypeAndImpact_notFound() {
        when(pcaRepository.findByIncidentTypeAndImpact("A", "B")).thenReturn(Optional.empty());

        PCA result = pcaService.getPCAByIncidentTypeAndImpact("A", "B");
        assertNull(result);
    }

    @Test
    void testDeletePCA() {
        Long id = 1L;

        pcaService.deletePCA(id);

        verify(pcaRepository).deleteById(id);
    }
    @Test
    void testLaunchSimulation_success() {
        // Préparer les données d'entrée
        SimulationRequest request = new SimulationRequest();
        request.setIncidentType(IncidentType.AUTHENTICATION_ISSUE);
        request.setImpact(Impact.LOW);
        request.setCommentaire("Simulation test");
        request.setGroupeId(1L);

        PCA pca = new PCA();
        pca.setLabel("AUTHENTICATION_ISSUE_LOW");

        Group group = new Group();
        group.setId(1L);

        Users admin = new Users();
        admin.setEmail("admin@example.com");

        Alerte alerteSaved = new Alerte();
        alerteSaved.setId(100L);

        Notification notification = new Notification();
        notification.setId(999L);

        // Simuler le contexte de sécurité
        var authentication = mock(org.springframework.security.core.Authentication.class);
        when(authentication.getName()).thenReturn("admin@example.com");

        var securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        // Mocks des dépendances
        when(pcaRepository.findByLabel("AUTHENTICATION_ISSUE_LOW")).thenReturn(pca);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(alerteRepository.save(any(Alerte.class))).thenReturn(alerteSaved);
        when(notificationService.createNotificationForUsers(any(), any(), any(), any()))
                .thenReturn(notification);

        // Exécution
        assertDoesNotThrow(() -> pcaService.launchSimulation(request));

        // Vérifications
        verify(alerteRepository).save(any(Alerte.class));
        verify(notificationService).createNotificationForUsers(any(), any(), any(), any());
        verify(emailService).sendGroupNotificationAfterResolution(group, alerteSaved, pca, admin, 999L);
    }

}
