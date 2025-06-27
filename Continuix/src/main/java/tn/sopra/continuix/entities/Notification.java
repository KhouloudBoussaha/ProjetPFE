package tn.sopra.continuix.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Notification implements Serializable {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String message;
    @Enumerated(EnumType.STRING)
    private IncidentType incidentType;
    @Enumerated(EnumType.STRING)
    private Impact impact;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy; // Admin qui a acréé la notification
    @JsonManagedReference
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL)
    private List<NotificationRecipient> recipients;



}
