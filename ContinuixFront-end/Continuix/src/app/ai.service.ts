import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { AiRequestDTO } from './ai-request-dto';
import { Observable } from 'rxjs';
import { PCA } from './models/PCA';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class AiService {
private apiUrl = `${environment.apiUrl}/api/ai/predict`;
 

  constructor(private http: HttpClient) {}

  predictPca(data: AiRequestDTO): Observable<PCA> {
    return this.http.post<PCA>(this.apiUrl, data);
  }
}
