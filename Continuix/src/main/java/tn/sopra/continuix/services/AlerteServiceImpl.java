package tn.sopra.continuix.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.sopra.continuix.entities.Alerte;
import tn.sopra.continuix.repositories.AlerteRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlerteServiceImpl implements AlerteService {

    private final AlerteRepository alerteRepository;
    private final EmailService mailService;
    @Override
    public Alerte createAlerte(Alerte alerte) {
        Alerte savedAlerte = alerteRepository.save(alerte);
        mailService.sendAlerteNotification(savedAlerte);
        return savedAlerte;
    }

    @Override
    public Alerte getAlerteById(Long id) {
        return alerteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Alerte> getAllAlertes() {
        return (List<Alerte>) alerteRepository.findAll();
    }

    @Override
    public Alerte updateAlerte(Alerte alerte) {
        return alerteRepository.save(alerte);
    }

    @Override
    public void deleteAlerte(Long id) {
        alerteRepository.deleteById(id);
    }
}
