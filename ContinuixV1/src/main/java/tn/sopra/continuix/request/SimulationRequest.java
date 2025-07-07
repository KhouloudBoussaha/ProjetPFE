package tn.sopra.continuix.request;

import lombok.Getter;
import lombok.Setter;
import tn.sopra.continuix.entities.Impact;
import tn.sopra.continuix.entities.IncidentType;

@Getter
@Setter
public class SimulationRequest {
    private IncidentType incidentType;
    private Impact impact;
    private Long groupeId;
    private String commentaire;
}
