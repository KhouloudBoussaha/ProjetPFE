package tn.sopra.continuix.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ToString
@NoArgsConstructor

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
    @ManyToOne
    @JsonIgnore // 🛑 pour casser la récursion
    @JoinColumn(name = "supervisor_id")
    private Users supervisor;
    @JsonIgnore
   @OneToMany(mappedBy = "supervisor", cascade = CascadeType.ALL)
   private List<Users> subordinates;


}

