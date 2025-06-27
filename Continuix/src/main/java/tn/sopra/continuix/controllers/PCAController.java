package tn.sopra.continuix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.entities.PCA;
import tn.sopra.continuix.services.PCAService;

import java.util.List;

@RestController
@RequestMapping("/api/pca")
public class PCAController {
    private final PCAService pcaService;

    @Autowired
    public PCAController(PCAService pcaService) {
        this.pcaService = pcaService;
    }

    @PostMapping
    public PCA addPCA(@RequestBody PCA pca) {
        return pcaService.addPCA(pca);
    }

    @GetMapping
    public List<PCA> getAllPCAs() {
        return pcaService.getAllPCAs();
    }

    @GetMapping("/recommend")
    public PCA getPCAByIncidentTypeAndImpact(
            @RequestParam String incidentType,
            @RequestParam String impact) {
        return pcaService.getPCAByIncidentTypeAndImpact(incidentType, impact);
    }
    @DeleteMapping("/{id}")
    public void deletePCA(@PathVariable Long id) {
        pcaService.deletePCA(id);
    }

    @PutMapping("/{id}")
    public PCA updatePCA(@PathVariable Long id, @RequestBody PCA pca) {

        pca.setId(id);
        return pcaService.updatePCA(pca);
    }
    @GetMapping("/pca/{label}")
    public ResponseEntity<PCA> getPcaByLabel(@PathVariable String label) {
        return pcaService.getPCAByLabel(label)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
