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
public class Notification {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;






    @Enumerated(EnumType.STRING)
    private TypeNotification type;

    private String contenu;
    private LocalDateTime dateEnvoi = LocalDateTime.now();
    private boolean estRecu = false;
}
enum TypeNotification {
    ACTIVATION_PCA, ALERTE, INFORMATION
}