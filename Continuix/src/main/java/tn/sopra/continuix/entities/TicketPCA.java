package tn.sopra.continuix.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor

@Entity
@Getter
@Setter
public class TicketPCA implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    private IncidentType incident_type;
    @Enumerated(EnumType.STRING)
    private Criticity criticity;
    private String services_impactes;
    private String date_incident;
    private String createdBy; // ID ou nom de l’utilisateur créateur
   @Enumerated(EnumType.STRING)
   private Status status;
}
