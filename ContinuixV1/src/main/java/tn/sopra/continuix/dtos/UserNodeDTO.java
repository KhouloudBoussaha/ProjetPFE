package tn.sopra.continuix.dtos;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UserNodeDTO {
    private Long id;
    private String username;
    private String role;
    @Column(nullable = false)
    private Boolean accepted = false;
    private List<UserNodeDTO> subordinates;
}
