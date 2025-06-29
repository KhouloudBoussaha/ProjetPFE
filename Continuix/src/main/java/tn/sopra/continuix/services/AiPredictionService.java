package tn.sopra.continuix.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import tn.sopra.continuix.dtos.AiRequestDTO;
import tn.sopra.continuix.dtos.FeatureEncoder;
import tn.sopra.continuix.entities.Notification;

import org.springframework.http.HttpHeaders;
import tn.sopra.continuix.entities.PCA;

import java.util.HashMap;
import java.util.Map;
@RequiredArgsConstructor
@Service
public class AiPredictionService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final PCAService pcaService;

    private final String flaskApiUrl = "https://projetpfe-ai.onrender.com/predict";

    public PCA predictAndFetchPCA(Notification notification) {
        int incidentEncoded = FeatureEncoder.encodeIncidentType(notification.getIncidentType());
        int impactEncoded = FeatureEncoder.encodeImpact(notification.getImpact());

        AiRequestDTO requestDTO = new AiRequestDTO(incidentEncoded, impactEncoded);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AiRequestDTO> request = new HttpEntity<>(requestDTO, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(flaskApiUrl, request, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String label = (String) response.getBody().get("pca_label");

                return pcaService.getPCAByLabel(label)
                        .orElseThrow(() -> new RuntimeException("PCA not found for label: " + label));
            } else {
                throw new RuntimeException("Flask API returned an error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error during PCA prediction");
        }
    }
}
