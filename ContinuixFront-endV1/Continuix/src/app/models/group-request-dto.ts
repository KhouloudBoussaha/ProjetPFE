import { Impact } from "../impact";
import { IncidentType } from "../incident-type";

export interface GroupRequestDTO {
     id?: number;
  name: string;
  description?: string;
  memberIds: number[];
  
}
