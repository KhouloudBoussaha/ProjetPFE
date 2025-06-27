package tn.sopra.continuix.services;

import tn.sopra.continuix.entities.Alerte;
import tn.sopra.continuix.entities.PCA;

import java.util.List;
import java.util.Optional;

public interface PCAService {
    PCA addPCA(PCA pca);
    List<PCA> getAllPCAs();
    PCA getPCAByIncidentTypeAndImpact(String incidentType, String impact);
     void deletePCA(Long id);
    PCA updatePCA(PCA pca);
    Optional<PCA> getPCAByLabel(String label);
}
