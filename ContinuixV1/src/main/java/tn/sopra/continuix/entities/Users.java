package tn.sopra.continuix.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@ToString(exclude = {"supervisor", "subordinates", "group"})
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    private String email;
    @Column
    private String resetPasswordToken;

    @Column
    private LocalDateTime tokenExpirationDate;
    @ManyToOne(fetch = FetchType.LAZY)  // Ajoute fetch lazy ici
    @JsonIgnore
    @JoinColumn(name = "supervisor_id")
    private Users supervisor;

    @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)  // Idem ici
    @JsonIgnore
    private List<Users> subordinates;

    @ManyToOne(fetch = FetchType.LAZY)  // Aussi ici
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private Group group;

}

