package tn.sopra.continuix.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Alerte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private TypeAlerte type;

    private String description;
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatutAlerte statut = StatutAlerte.NON_TRAITE;


    private LocalDateTime dateResolution;
}
enum TypeAlerte {
    PHYSIQUE, INFORMATIQUE
}

enum StatutAlerte {
    NON_TRAITE, EN_COURS, RESOLU
}
