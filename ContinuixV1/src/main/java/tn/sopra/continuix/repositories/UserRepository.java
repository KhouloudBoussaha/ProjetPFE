package tn.sopra.continuix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.sopra.continuix.entities.Group;
import tn.sopra.continuix.entities.Role;
import tn.sopra.continuix.entities.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    List<Users> findBySupervisorId(Long supervisorId);
    List<Users> findBySupervisorIsNull();
    Users findByResetPasswordToken(String resetPasswordToken);
    Optional<Users> findByRole(Role role); //
    List<Users> findByGroup(Group group);
    Users findByUsername(String username);
}

