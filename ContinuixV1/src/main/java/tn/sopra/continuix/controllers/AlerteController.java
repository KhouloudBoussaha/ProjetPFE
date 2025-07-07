package tn.sopra.continuix.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import tn.sopra.continuix.dtos.GroupRequestDTO;
import tn.sopra.continuix.entities.Alerte;
import tn.sopra.continuix.entities.Group;
import tn.sopra.continuix.entities.PCA;
import tn.sopra.continuix.entities.Users;
import tn.sopra.continuix.repositories.AlerteRepository;
import tn.sopra.continuix.repositories.UserRepository;
import tn.sopra.continuix.services.AlerteService;
import tn.sopra.continuix.services.EmailService;
import tn.sopra.continuix.services.PCAService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/alertes")
@RequiredArgsConstructor
public class AlerteController {

    private final AlerteService alerteService;
    private final AlerteRepository alerteRepository;
    private final PCAService pcaService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Alerte> createAlerte(@RequestBody Alerte alerte) {
        Alerte createdAlerte = alerteService.createAlerte(alerte);
        return new ResponseEntity<>(createdAlerte, HttpStatus.CREATED);
    }
    @PutMapping("/{id}/resolve")
    public ResponseEntity<String> resolveAndNotify(@PathVariable Long id) {
        // Récupérer le nom de l'utilisateur connecté (admin)
        String adminName = SecurityContextHolder.getContext().getAuthentication().getName();

        // Appeler la méthode du service pour résoudre l'alerte et notifier
        try {
            alerteService.resolveAlerteAndNotify(id, adminName);
            return ResponseEntity.ok("Alerte résolue et notifications envoyées");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne lors de la résolution de l'alerte");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Alerte>> getAllAlertes() {
        List<Alerte> alertes = alerteService.getAllAlertes();
        return ResponseEntity.ok(alertes);
    }


    @GetMapping("/my-alertes")
    public ResponseEntity<List<Alerte>> getMyAlertes() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        List<Alerte> myAlertes = alerteService.getAlertesByEmail(email);
        return new ResponseEntity<>(myAlertes, HttpStatus.OK);
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
