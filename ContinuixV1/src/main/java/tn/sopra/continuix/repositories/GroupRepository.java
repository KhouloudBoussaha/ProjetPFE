package tn.sopra.continuix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.sopra.continuix.entities.Group;
import tn.sopra.continuix.entities.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByMembersContains(Users user);

    Group findByName(String name);
}
