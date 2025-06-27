import { Impact } from "../impact";
import { IncidentType } from "../incident-type";

export interface Notification {
 id: number;
  title: string;
  message: string;
  incidentType: IncidentType;
  impact: Impact;
  createdAt: string;
  accepted: boolean;
  createdById: number;
  recipientId: number;

}
