package tn.sopra.continuix.request;

import lombok.Getter;
import lombok.Setter;
import tn.sopra.continuix.entities.Impact;
import tn.sopra.continuix.entities.IncidentType;

@Getter
@Setter
public class NotificationRequest {
    private Long id;
    private String title;
    private String message;
    private IncidentType incidentType;
    private Impact impact;


}
