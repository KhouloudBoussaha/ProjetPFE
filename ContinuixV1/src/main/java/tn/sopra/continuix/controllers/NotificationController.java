package tn.sopra.continuix.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.dtos.NotificationDTO;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.AlerteRepository;
import tn.sopra.continuix.repositories.PCARepository;
import tn.sopra.continuix.repositories.UserRepository;
import tn.sopra.continuix.request.NotificationRequest;
import tn.sopra.continuix.services.NotificationService;
import tn.sopra.continuix.services.PCAService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final AlerteRepository alerteRepository;
    private final PCAService pcaService;
    private final UserRepository userRepository;
    public NotificationController(NotificationService notificationService, AlerteRepository alerteRepository,PCAService pcaService,UserRepository userRepository) {
        this.notificationService = notificationService;
        this.alerteRepository = alerteRepository;
        this.pcaService = pcaService;
        this.userRepository = userRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> createNotificationForGroupAndNotify(@RequestBody NotificationRequest request) {
        // Récupérer l'alerte liée à la notification (par exemple via request.getAlerteId())
        Alerte alerte = alerteRepository.findById(request.getAlerteId())
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée"));

        // Récupérer le PCA associé (selon type + impact)
        PCA pca = pcaService.getPCAByLabel(alerte.getType() + "_" + alerte.getImpact());
        if (pca == null) {
            throw new RuntimeException("PCA non trouvé pour l'alerte");
        }

        // Récupérer l'utilisateur admin qui envoie (depuis le contexte de sécurité ou via request)
        String adminName = SecurityContextHolder.getContext().getAuthentication().getName();
        Users admin = userRepository.findByUsername(adminName);
        if (admin == null) {
            throw new RuntimeException("Admin non trouvé");
        }

        // Récupérer la liste des utilisateurs destinataires (par ex. les membres du groupe PCA)
        List<Users> recipients = pca.getGroupe().getMembers();

        // Appeler la méthode dans NotificationService pour créer la notification et destinataires
        notificationService.createNotificationForUsers(recipients, alerte, pca, admin);

        return ResponseEntity.ok().build();
    }



    @GetMapping("/{id}/view")
    public ResponseEntity<NotificationDTO> viewNotification(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        NotificationDTO dto = notificationService.viewNotification(id, email);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable("id") Long id, Principal principal) {
        String email = principal.getName();
        NotificationDTO dto = notificationService.viewNotification(id, email);
        return ResponseEntity.ok(dto);
    }

    // Endpoint pour accuser réception (POST)
    @PostMapping("/{id}/acknowledge")
    public ResponseEntity<Void> acknowledgeNotification(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        notificationService.acknowledgeNotification(id, email);
        return ResponseEntity.ok().build();
    }



}
