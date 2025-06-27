package tn.sopra.continuix.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.entities.Alerte;
import tn.sopra.continuix.services.AlerteService;

import java.util.List;

@RestController
@RequestMapping("/api/alertes")
@RequiredArgsConstructor
public class AlerteController {

    private final AlerteService alerteService;

    @PostMapping
    public ResponseEntity<Alerte> createAlerte(@RequestBody Alerte alerte) {
        Alerte createdAlerte = alerteService.createAlerte(alerte);
        return new ResponseEntity<>(createdAlerte, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<Alerte>> getAllAlertes() {
        List<Alerte> alertes = alerteService.getAllAlertes();
        return new ResponseEntity<>(alertes, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Alerte> getAlerteById(@PathVariable Long id) {
        Alerte alerte = alerteService.getAlerteById(id);
        return alerte != null ? new ResponseEntity<>(alerte, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Alerte> updateAlerte(@PathVariable Long id, @RequestBody Alerte alerte) {
        Alerte existingAlerte = alerteService.getAlerteById(id);
        if (existingAlerte != null) {
            alerte.setId(id);
            Alerte updatedAlerte = alerteService.updateAlerte(alerte);
            return new ResponseEntity<>(updatedAlerte, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlerte(@PathVariable Long id) {
        Alerte alerte = alerteService.getAlerteById(id);
        if (alerte != null) {
            alerteService.deleteAlerte(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
