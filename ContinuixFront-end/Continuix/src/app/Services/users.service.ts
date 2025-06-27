import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

 private baseURL = "http://localhost:8075/api/users";

  constructor(private httpClient: HttpClient) { }
   private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  getUsersList(): Observable<User[]>{
    return this.httpClient.get<User[]>(`${this.baseURL}`);
  }

  createUser(employee: User): Observable<Object>{
    return this.httpClient.post(`${this.baseURL}`, employee);
  }

  getUserById(id: number): Observable<User>{
    return this.httpClient.get<User>(`${this.baseURL}/${id}`);
  }

  updateUser(id: number, user: User): Observable<Object>{
    return this.httpClient.put(`${this.baseURL}/${id}`, user);
  }

  deleteUser(id: number): Observable<Object>{
    return this.httpClient.delete(`${this.baseURL}/${id}`);
  }

  resetPassword(token: string, newPassword: string): Observable<Object> {
  const payload = { token, newPassword };
  return this.httpClient.post(`${this.baseURL}/reset-password`, payload);
}
getUsername(): string | null {
  return localStorage.getItem('username');
}

}
