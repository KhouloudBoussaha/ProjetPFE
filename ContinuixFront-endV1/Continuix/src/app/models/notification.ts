import { Impact } from "../impact";
import { IncidentType } from "../incident-type";

export interface Notification {
 id: number;
  title: string;
  message: string;
  incidentType: IncidentType;
  impact: Impact;
  createdAt: string; // Ou LocalDateTime si tu le traites avec une lib type dayjs
  createdById: number;
  recipientId: number;
  accepted: boolean;

}
