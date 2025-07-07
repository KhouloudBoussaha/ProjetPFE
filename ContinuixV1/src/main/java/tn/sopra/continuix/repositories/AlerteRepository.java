package tn.sopra.continuix.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.sopra.continuix.entities.Alerte;

import java.util.List;

@Repository
public interface AlerteRepository extends CrudRepository<Alerte, Long> {
    List<Alerte> findByCreatedByEmail(String email);
    List<Alerte> findAll();

}
