import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Ticket } from './ticket';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TicketPcaService {
 baseURL="http://localhost:8075/ticket";
  constructor(private httpClient:HttpClient) { }
  addTicket(ticket :Ticket): Observable<Object>{
    console.log(ticket);
    return this.httpClient.post(`${this.baseURL}`, ticket);

  } 
}
