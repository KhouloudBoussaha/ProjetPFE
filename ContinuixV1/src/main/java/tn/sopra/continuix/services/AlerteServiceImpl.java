package tn.sopra.continuix.services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.AlerteRepository;
import tn.sopra.continuix.repositories.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AlerteServiceImpl implements AlerteService {
    @Autowired
    private AlerteRepository alerteRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PCAService pcaService;
    @Autowired
    private NotificationService notificationService;

    @Override
    public Alerte createAlerte(Alerte alerte) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = (principal instanceof UserDetails)
                ? ((UserDetails) principal).getUsername()
                : principal.toString();

        Users currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        alerte.setCreatedBy(currentUser);
        alerte.setResolved(false);
        Alerte savedAlerte = alerteRepository.save(alerte);

        emailService.sendAlerteNotification(savedAlerte);
        return savedAlerte;
    }

    @Override
    public Alerte getAlerteById(Long id) {
        return alerteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Alerte> getAllAlertes() {
        return alerteRepository.findAll();
    }

    @Override
    @Transactional
    public void resolveAlerteAndNotify(Long alerteId, String adminEmail) {
        Alerte alerte = alerteRepository.findById(alerteId)
                .orElseThrow(() -> new RuntimeException("Alerte non trouvée"));

        alerte.setResolved(true);
        alerteRepository.save(alerte);

        PCA pca = pcaService.getPCAByLabel(alerte.getType() + "_" + alerte.getImpact());
        if (pca == null || pca.getGroupe() == null) {
            throw new RuntimeException("Aucun PCA ou groupe associé trouvé pour ce type d'incident");
        }

        Users admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin non trouvé"));

        Notification notification = notificationService.createNotificationForUsers(
                pca.getGroupe().getMembers(), alerte, pca, admin
        );

        emailService.sendGroupNotificationAfterResolution(pca.getGroupe(), alerte, pca, admin, notification.getId());
        emailService.sendGlobalNotificationAfterResolution(userRepository.findAll(), alerte, pca, admin, notification.getId());
    }

    @Override
    public Alerte updateAlerte(Alerte alerte) {
        return alerteRepository.save(alerte);
    }

    @Override
    public void deleteAlerte(Long id) {
        alerteRepository.deleteById(id);
    }

    @Override
    public List<Alerte> getAlertesByEmail(String email) {
        return alerteRepository.findByCreatedByEmail(email);
    }
}
