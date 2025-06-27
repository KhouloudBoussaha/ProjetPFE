package tn.sopra.continuix.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.sopra.continuix.entities.Alerte;
import tn.sopra.continuix.entities.PCA;
import tn.sopra.continuix.repositories.PCARepository;

import java.util.List;
import java.util.Optional;

@Service
public class PCAServiceImpl implements PCAService {
    private final PCARepository pcaRepository;

    @Autowired
    public PCAServiceImpl(PCARepository pcaRepository) {
        this.pcaRepository = pcaRepository;
    }

    @Override
    public PCA addPCA(PCA pca) {
        return pcaRepository.save(pca);
    }

    @Override
    public List<PCA> getAllPCAs() {
        return pcaRepository.findAll();
    }

    @Override
    public PCA getPCAByIncidentTypeAndImpact(String incidentType, String impact) {
        return pcaRepository.findByIncidentTypeAndImpact(incidentType, impact)
                .orElse(null);
    }
    @Override
    public void deletePCA(Long id) {
        pcaRepository.deleteById(id);
    }
    @Override
    public PCA updatePCA(PCA pca) {
        return pcaRepository.save(pca);
    }
    @Override
    public Optional<PCA> getPCAByLabel(String label) {
        return pcaRepository.findByLabel(label);
    }

}
