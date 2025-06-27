package tn.sopra.continuix.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.sopra.continuix.entities.Alerte;

@Repository
public interface AlerteRepository extends CrudRepository<Alerte, Long> {

}
