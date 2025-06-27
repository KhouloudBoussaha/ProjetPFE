package tn.sopra.continuix.dtos;

import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.sopra.continuix.entities.Impact;
import tn.sopra.continuix.entities.IncidentType;

import java.time.LocalDateTime;
@Getter
@Setter

public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private IncidentType incidentType;
    private Impact impact;
    private LocalDateTime createdAt;
    private Long createdById;
    private Long recipientId;
    private boolean accepted;
    public NotificationDTO(Long id, String title, String message, IncidentType incidentType, Impact impact,
                           LocalDateTime createdAt, Long createdById, Long recipientId, boolean accepted) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.incidentType = incidentType;
        this.impact = impact;
        this.createdAt = createdAt;
        this.createdById = createdById;
        this.recipientId = recipientId;
        this.accepted = accepted;
    }

}
