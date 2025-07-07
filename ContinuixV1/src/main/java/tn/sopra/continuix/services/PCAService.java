package tn.sopra.continuix.services;

import tn.sopra.continuix.entities.PCA;
import tn.sopra.continuix.request.SimulationRequest;

import java.util.List;

public interface PCAService {
    PCA addPCA(PCA pca, String groupName);
    List<PCA> getAllPCAs();
    PCA getPCAByIncidentTypeAndImpact(String incidentType, String impact);
     void deletePCA(Long id);
    PCA updatePCA(PCA pca, String groupName);
    PCA getPCAByLabel(String label);
    void launchSimulation(SimulationRequest request);

}
