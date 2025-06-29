import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Alerte } from './models/alerte';
import { IncidentType } from './incident-type';
import { environment } from 'src/environments/environment';



@Injectable({
  providedIn: 'root'
})
export class AlerteService {
private apiUrl = `${environment.apiUrl}/api/alertes`;
   
  constructor(private http: HttpClient) { }

  getAllAlertes(): Observable<Alerte[]> {
    return this.http.get<Alerte[]>(this.apiUrl);
  }

  getAlerteById(id: number): Observable<Alerte> {
    return this.http.get<Alerte>(`${this.apiUrl}/${id}`);
  }

  getAlertesByType(type: IncidentType): Observable<Alerte[]> {
    return this.http.get<Alerte[]>(`${this.apiUrl}/type/${type}`);
  }

  getAlertesByDateRange(start: string, end: string): Observable<Alerte[]> {
    return this.http.get<Alerte[]>(`${this.apiUrl}/date-range?start=${start}&end=${end}`);
  }

  getUnresolvedAlertes(): Observable<Alerte[]> {
    return this.http.get<Alerte[]>(`${this.apiUrl}/unresolved`);
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