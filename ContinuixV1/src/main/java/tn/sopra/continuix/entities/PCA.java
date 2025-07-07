package tn.sopra.continuix.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class PCA implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String incidentType;
    private String impact;
    private String recommendedAction;
    private String additionalDetails;
    private String label;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupe_id")
    private Group groupe; // Groupe concerné par ce PCA
}
