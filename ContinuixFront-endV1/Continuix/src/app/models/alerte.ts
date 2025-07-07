import { Impact } from "../impact";
import { IncidentType } from "../incident-type";
import { User } from "../user";

export interface Alerte {
  id?: number;
  description: string;
   type: string     // si tu veux, tu peux mettre IncidentType | string
  impact: string;   // Impact | string
  createdBy?: User;
   resolved?: boolean;

}
