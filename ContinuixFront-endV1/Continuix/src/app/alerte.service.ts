import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Alerte } from './models/alerte';
import { IncidentType } from './incident-type';

@Injectable({
  providedIn: 'root'
})
export class AlerteService {

   private apiUrl = 'http://localhost:8075/api/alertes';

  constructor(private http: HttpClient) { }

  getAllAlertes(): Observable<Alerte[]> {
    return this.http.get<Alerte[]>(`${this.apiUrl}/all`);
  }
  resolveAlerte(id: number): Observable<Alerte> {
  return this.http.put<Alerte>(`${this.apiUrl}/${id}/resolve`, {});
}

   getMyAlertes(): Observable<Alerte[]> {
    return this.http.get<Alerte[]>(`${this.apiUrl}/my-alertes`);
  }

  createAlerte(alerte: Alerte): Observable<Alerte> {
    return this.http.post<Alerte>(this.apiUrl, alerte);
  }

  updateAlerte(id: number, alerte: Alerte): Observable<Alerte> {
    return this.http.put<Alerte>(`${this.apiUrl}/${id}`, alerte);
  }

  deleteAlerte(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}