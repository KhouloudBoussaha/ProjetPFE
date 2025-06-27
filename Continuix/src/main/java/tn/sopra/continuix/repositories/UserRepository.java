package tn.sopra.continuix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.sopra.continuix.entities.Role;
import tn.sopra.continuix.entities.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findBySupervisorId(Long supervisorId);
    List<User> findBySupervisorIsNull();
    User findByResetPasswordToken(String resetPasswordToken);
    Optional<User> findByRole(Role role); //
}

