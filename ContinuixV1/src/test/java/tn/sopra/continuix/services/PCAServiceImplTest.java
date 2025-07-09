package tn.sopra.continuix.services;

import org.junit.jupiter.api.*;
import org.mockito.*;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PCAServiceImplTest {

    @InjectMocks private PCAServiceImpl pcaService;

    @Mock private PCARepository pcaRepository;
    @Mock private GroupRepository groupRepository;
    @Mock private AlerteRepository alerteRepository;
    @Mock private EmailService emailService;
    @Mock private NotificationService notificationService;
    @Mock private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddPCA_WithGroup_Success() {
        Group group = new Group();
        group.setName("IT");

        PCA pca = new PCA();
        pca.setIncidentType("Inc");
        pca.setImpact("High");

        when(groupRepository.findByName("IT")).thenReturn(group);
        when(pcaRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        PCA result = pcaService.addPCA(pca, "IT");

        assertNotNull(result.getLabel());
        assertEquals("Inc_High", result.getLabel());
        assertEquals(group, result.getGroupe());
    }

    @Test
    void testGetAllPCAs_ReturnsList() {
        List<PCA> list = List.of(new PCA(), new PCA());
        when(pcaRepository.findAll()).thenReturn(list);
        assertEquals(2, pcaService.getAllPCAs().size());
    }
}
