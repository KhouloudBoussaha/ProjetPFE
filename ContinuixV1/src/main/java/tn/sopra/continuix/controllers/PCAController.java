package tn.sopra.continuix.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.entities.PCA;
import tn.sopra.continuix.request.SimulationRequest;
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
    public PCA addPCA(@RequestBody PCA pca, @RequestParam String groupName) {
        return pcaService.addPCA(pca, groupName);
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
    public PCA updatePCA(@PathVariable Long id, @RequestBody PCA pca, @RequestParam String groupName) {
        pca.setId(id);
        return pcaService.updatePCA(pca, groupName);
    }
    @GetMapping("/pca/{label}")
    public ResponseEntity<PCA> getPcaByLabel(@PathVariable String label) {
        try {
            PCA pca = pcaService.getPCAByLabel(label);
            return ResponseEntity.ok(pca);
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/simulate")
    public ResponseEntity<String> simulatePca(@RequestBody SimulationRequest request) {
        try {
            pcaService.launchSimulation(request);
            return ResponseEntity.ok("✅ Simulation PCA lancée !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Erreur : " + e.getMessage());
        }
    }

}
