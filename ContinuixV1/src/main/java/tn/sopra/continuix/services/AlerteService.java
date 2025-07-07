package tn.sopra.continuix.services;

import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import tn.sopra.continuix.dtos.GroupRequestDTO;
import tn.sopra.continuix.entities.Alerte;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface AlerteService {
    Alerte createAlerte(Alerte alerte);

    Alerte getAlerteById(Long id);

    List<Alerte> getAllAlertes();


    void resolveAlerteAndNotify(Long alerteId, String adminName) ;

    Alerte updateAlerte(Alerte alerte);

    void deleteAlerte(Long id);
    List<Alerte> getAlertesByEmail(String email);


}
