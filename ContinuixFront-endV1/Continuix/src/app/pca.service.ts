import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {  throwError ,Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { PCA } from './models/PCA';
import { SimulationRequest } from './models/simulation-request';



@Injectable({
  providedIn: 'root'
})
export class PcaService {

   private apiUrl = 'http://localhost:8075/api/pca'; 

  constructor(private http: HttpClient) {}

  getAllPCAs(): Observable<PCA[]> {
    return this.http.get<PCA[]>(this.apiUrl);
  }

 addPCA(pca: PCA, groupName: string): Observable<PCA> {
    return this.http
      .post<PCA>(`${this.apiUrl}?groupName=${encodeURIComponent(groupName)}`, pca)
      .pipe(catchError(this.handleError));
  }

  updatePCA(id: number, pca: PCA, groupName: string): Observable<PCA> {
    return this.http
      .put<PCA>(`${this.apiUrl}/${id}?groupName=${encodeURIComponent(groupName)}`, pca)
      .pipe(catchError(this.handleError));
  }

  deletePCA(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getPCAByIncidentTypeAndImpact(incidentType: string, impact: string): Observable<PCA> {
    return this.http
      .get<PCA>(
        `${this.apiUrl}/recommend?incidentType=${encodeURIComponent(incidentType)}&impact=${encodeURIComponent(impact)}`
      )
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Une erreur est survenue';
    if (error.status === 404) {
      errorMessage = 'Ressource non trouvée (PCA ou groupe)';
    } else if (error.status === 400) {
      errorMessage = error.error || 'Requête invalide';
    }
    return throwError(() => new Error(errorMessage));
  }
   simulatePca(request: SimulationRequest): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/simulate`, request);
  }
}
