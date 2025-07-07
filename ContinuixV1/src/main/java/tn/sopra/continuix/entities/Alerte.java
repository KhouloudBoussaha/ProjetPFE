package tn.sopra.continuix.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data

public class Alerte implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private IncidentType type;
    @Column(name = "impact")
    @Enumerated(EnumType.STRING)
    private Impact impact;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "user_id")
    private Users createdBy;
    private boolean resolved = false;
    private boolean simulation = false;
}

