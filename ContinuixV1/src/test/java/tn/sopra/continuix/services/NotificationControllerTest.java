package tn.sopra.continuix.services;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.eq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.sopra.continuix.controllers.NotificationController;
import tn.sopra.continuix.dtos.NotificationDTO;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.AlerteRepository;
import tn.sopra.continuix.repositories.UserRepository;
import tn.sopra.continuix.request.NotificationRequest;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@SuppressWarnings("removal")
@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private AlerteRepository alerteRepository;

    @MockBean
    private PCAService pcaService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Principal mockPrincipal(String username) {
        return () -> username;
    }

    @Test
    void testCreateNotificationForGroupAndNotify() throws Exception {
        NotificationRequest request = new NotificationRequest();
        request.setAlerteId(1L);

        Alerte alerte = new Alerte();
        alerte.setId(1L);
        alerte.setType(IncidentType.AUTHENTICATION_ISSUE);
        alerte.setImpact(Impact.LOW);

        PCA pca = new PCA();
        Users admin = new Users();

        Mockito.when(alerteRepository.findById(1L)).thenReturn(Optional.of(alerte));
        Mockito.when(pcaService.getPCAByLabel("Incident_Critique")).thenReturn(pca);
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(admin);

        mockMvc.perform(post("/notifications/send")
                        .principal(mockPrincipal("admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetNotificationById() throws Exception {
        NotificationDTO dto = new NotificationDTO(
                1L, "Titre", "Message",
                null, null, LocalDateTime.now(),
                10L, 20L, false
        );

        Mockito.when(notificationService.viewNotification(eq(1L), eq("user@example.com"))).thenReturn(dto);

        mockMvc.perform(get("/notifications/1")
                        .principal(mockPrincipal("user@example.com")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Titre"))
                .andExpect(jsonPath("$.message").value("Message"));
    }
}
