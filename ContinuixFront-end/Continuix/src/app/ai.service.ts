import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AiRequestDTO } from './ai-request-dto';
import { Observable } from 'rxjs';
import { PCA } from './models/PCA';

@Injectable({
  providedIn: 'root'
})
export class AiService {

  private apiUrl = 'http://localhost:8075/api/ai/predict'; // Backend Java endpoint

  constructor(private http: HttpClient) {}

  predictPca(data: AiRequestDTO): Observable<PCA> {
    return this.http.post<PCA>(this.apiUrl, data);
  }
}
