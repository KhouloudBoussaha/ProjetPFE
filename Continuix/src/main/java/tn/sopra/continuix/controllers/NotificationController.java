package tn.sopra.continuix.controllers;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.dtos.NotificationDTO;
import tn.sopra.continuix.entities.Notification;
import tn.sopra.continuix.entities.NotificationRecipient;
import tn.sopra.continuix.request.NotificationRequest;
import tn.sopra.continuix.services.NotificationService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> createForAll(@RequestBody NotificationRequest request) {
        notificationService.createNotificationForAllUsers(request);
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

//    @GetMapping("/history")
//    public ResponseEntity<List<NotificationDTO>> getNotificationHistory() {
//        List<NotificationDTO> history = notificationService.getNotificationHistoryDTO();
//        return ResponseEntity.ok(history);
//    }


}
