package tn.sopra.continuix.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.sopra.continuix.dtos.AiRequestDTO;
import tn.sopra.continuix.entities.Impact;
import tn.sopra.continuix.entities.IncidentType;
import tn.sopra.continuix.entities.Notification;
import tn.sopra.continuix.entities.PCA;
import tn.sopra.continuix.services.AiPredictionService;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiPredictionController {
    private final AiPredictionService aiPredictionService;

    @PostMapping("/predict")
    public PCA predictPCA(@RequestBody AiRequestDTO dto) {
        Notification n = new Notification();
        n.setImpact(decodeImpact(dto.getImpact_encoded()));
        n.setIncidentType(decodeIncidentType(dto.getIncident_type_encoded()));

        return aiPredictionService.predictAndFetchPCA(n);
    }

    private Impact decodeImpact(int code) {
        switch (code) {
            case 1: return Impact.LOW;
            case 2: return Impact.MEDIUM;
            case 3: return Impact.HIGH;
            default: throw new IllegalArgumentException("Impact inconnu: " + code);
        }
    }

    private IncidentType decodeIncidentType(int code) {
        switch (code) {
            case 1: return IncidentType.AUTHENTICATION_ISSUE;
            case 2: return IncidentType.NETWORK_ISSUE;
            case 3: return IncidentType.SYSTEM_ERROR;
            case 4: return IncidentType.USER_REQUEST;
            case 5: return IncidentType.NATURAL_INCIDENT;
            case 6: return IncidentType.DEVELOPMENT_ENVIRONMENT;
            case 7: return IncidentType.OTHER_IT_INCIDENT;
            case 8: return IncidentType.UNKNOWN;
            default: throw new IllegalArgumentException("IncidentType inconnu: " + code);
        }
    }
}

