package tn.sopra.continuix.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificationRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "notification_id")
    @JsonIgnore // 🛑 IMPORTANT : empêche boucle infinie
    @ManyToOne
    private Notification notification;
    @JoinColumn(name = "recipient_id")
    @JsonIgnore // ou utilise DTO si tu as besoin d'infos sur le recipient
    @ManyToOne
    private Users recipient;
    @Column(nullable = false)
    private Boolean accepted = false;

    private LocalDateTime acknowledgedAt;
    @PrePersist
    public void prePersist() {
        if (accepted == null) {
            accepted = false;
        }}
}
