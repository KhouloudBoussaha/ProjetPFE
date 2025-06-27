import { IncidentType } from "../incident-type";

export interface Alerte {
  id?: number;
  description: string;
  type: IncidentType;
  dateCreation?: string;
  dateResolution?: string | null;

}
