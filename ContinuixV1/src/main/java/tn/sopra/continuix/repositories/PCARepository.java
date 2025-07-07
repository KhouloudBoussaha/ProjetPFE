package tn.sopra.continuix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.sopra.continuix.entities.PCA;

import java.util.Optional;

@Repository
public interface PCARepository extends JpaRepository<PCA,Long> {
    Optional<PCA> findByIncidentTypeAndImpact(String incidentType, String impact);
    PCA findByLabel(String label);

}
