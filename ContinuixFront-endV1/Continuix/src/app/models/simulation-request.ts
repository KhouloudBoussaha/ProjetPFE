import { Impact } from "../impact";
import { IncidentType } from "../incident-type";

export interface SimulationRequest {
    incidentType: IncidentType;
  impact: Impact;
  groupeId: number;
  commentaire?: string;
}

