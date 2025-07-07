package tn.sopra.continuix.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.sopra.continuix.entities.*;
import tn.sopra.continuix.repositories.AlerteRepository;
import tn.sopra.continuix.repositories.GroupRepository;
import tn.sopra.continuix.repositories.PCARepository;
import tn.sopra.continuix.repositories.UserRepository;
import tn.sopra.continuix.request.SimulationRequest;

import java.util.List;

@Slf4j
@Service

public class PCAServiceImpl implements PCAService {
    @Autowired
    private PCARepository pcaRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private AlerteRepository alerteRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;

    @Override
    public PCA addPCA(PCA pca, String groupName) {
        Group group = groupRepository.findByName(groupName);
        if (group == null) {
            throw new RuntimeException("Groupe non trouvé : " + groupName);
        }

        pca.setGroupe(group);

        if (pca.getLabel() == null) {
            pca.setLabel(pca.getIncidentType() + "_" + pca.getImpact());
        }

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
    public PCA updatePCA(PCA pca, String groupName) {
        if (!pcaRepository.existsById(pca.getId())) {
            throw new RuntimeException("PCA non trouvé pour l'ID : " + pca.getId());
        }

        Group group = groupRepository.findByName(groupName);
        if (group == null) {
            throw new RuntimeException("Groupe non trouvé : " + groupName);
        }

        pca.setGroupe(group);
        pca.setLabel(pca.getIncidentType() + "_" + pca.getImpact());

        return pcaRepository.save(pca);
    }

    @Override
    public PCA getPCAByLabel(String label) {
        PCA pca = pcaRepository.findByLabel(label);
        if (pca == null) {
            throw new RuntimeException("PCA non trouvé pour le label : " + label);
        }
        return pca;
    }

    @Override
    @Transactional
    public void launchSimulation(SimulationRequest request) {
        log.info("🧪 Début simulation PCA");

        String label = request.getIncidentType() + "_" + request.getImpact();
        PCA pca = pcaRepository.findByLabel(label);
        if (pca == null) {
            throw new RuntimeException("❌ PCA non trouvé pour : " + label);
        }

        Group groupe = groupRepository.findById(request.getGroupeId())
                .orElseThrow(() -> new RuntimeException("❌ Groupe non trouvé"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users admin = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + email));

        Alerte alerte = new Alerte();
        alerte.setType(request.getIncidentType());
        alerte.setImpact(request.getImpact());
        alerte.setDescription(request.getCommentaire());
        alerte.setSimulation(true);
        alerte.setResolved(true);
        alerte.setCreatedBy(admin);
        alerteRepository.save(alerte);

        log.info("✅ Alerte simulée enregistrée : {}", alerte.getId());

        Notification notif = notificationService.createNotificationForUsers(
                groupe.getMembers(), alerte, pca, admin
        );

        log.info("📨 Notification simulation créée : {}", notif.getId());

        emailService.sendGroupNotificationAfterResolution(groupe, alerte, pca, admin, notif.getId());

        log.info("🏁 Fin simulation PCA");
    }
}
