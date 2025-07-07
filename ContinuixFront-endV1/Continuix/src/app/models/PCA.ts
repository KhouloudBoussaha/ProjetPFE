import { Impact } from "../impact";
import { IncidentType } from "../incident-type";

export interface PCA {
id?: number;
  incidentType: string;
  impact: string;
  recommendedAction: string;
  additionalDetails: string;
 label?: string
   groupe?: { id: number; name: string }; // Optionnel, car le frontend envoie groupName
}
