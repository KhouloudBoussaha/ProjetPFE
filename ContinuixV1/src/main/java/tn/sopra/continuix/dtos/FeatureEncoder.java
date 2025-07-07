package tn.sopra.continuix.dtos;

import tn.sopra.continuix.entities.Impact;
import tn.sopra.continuix.entities.IncidentType;

public class FeatureEncoder {
    public static int encodeIncidentType(IncidentType type) {
        switch (type) {
            case AUTHENTICATION_ISSUE: return 1;
            case NETWORK_ISSUE: return 2;
            case SYSTEM_ERROR: return 3;
            case USER_REQUEST: return 4;
            case NATURAL_INCIDENT: return 5;
            case DEVELOPMENT_ENVIRONMENT: return 6;
            case OTHER_IT_INCIDENT: return 7;
            case UNKNOWN: return 8;
            default: return 0;
        }
    }

    public static int encodeImpact(Impact impact) {
        switch (impact) {
            case LOW: return 1;
            case MEDIUM: return 2;
            case HIGH: return 3;
            default: return 0;
        }
    }
}
