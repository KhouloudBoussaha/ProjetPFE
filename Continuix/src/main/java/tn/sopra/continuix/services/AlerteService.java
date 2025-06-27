package tn.sopra.continuix.services;

import org.springframework.data.domain.Page;
import tn.sopra.continuix.entities.Alerte;

import java.util.List;

public interface AlerteService {
    Alerte createAlerte(Alerte alerte);

    Alerte getAlerteById(Long id);

    List<Alerte> getAllAlertes();

    Alerte updateAlerte(Alerte alerte);

    void deleteAlerte(Long id);
}
