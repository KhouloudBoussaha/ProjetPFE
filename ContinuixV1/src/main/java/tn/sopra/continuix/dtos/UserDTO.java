package tn.sopra.continuix.dtos;

import lombok.Getter;
import lombok.Setter;
import tn.sopra.continuix.entities.Role;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private Long   id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private Long supervisor;         // ID du superviseur
    private String supervisorName;
    private List<Long> groupIds;

}
