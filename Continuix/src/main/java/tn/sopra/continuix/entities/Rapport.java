package tn.sopra.continuix.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rapport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;





    @Lob
    private String contenu;

    private LocalDateTime dateCreation = LocalDateTime.now();
    private boolean exporte = false;
}
