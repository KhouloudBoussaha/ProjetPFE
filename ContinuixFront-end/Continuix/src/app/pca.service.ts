import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PCA } from './models/PCA';
import { environment } from 'src/environments/environments';



@Injectable({
  providedIn: 'root'
})
export class PcaService {
private apiUrl = `${environment.apiUrl}/api/pca`;

  constructor(private http: HttpClient) {}

  getAllPCAs(): Observable<PCA[]> {
    return this.http.get<PCA[]>(this.apiUrl);
  }

  addPCA(pca: PCA): Observable<PCA> {
    return this.http.post<PCA>(this.apiUrl, pca);
  }

  updatePCA(id: number, pca: PCA): Observable<PCA> {
    return this.http.put<PCA>(`${this.apiUrl}/${id}`, pca);
  }

  deletePCA(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getPCAByIncidentTypeAndImpact(incidentType: string, impact: string): Observable<PCA> {
    return this.http.get<PCA>(
      `${this.apiUrl}/recommend?incidentType=${incidentType}&impact=${impact}`
    );
  }
}
