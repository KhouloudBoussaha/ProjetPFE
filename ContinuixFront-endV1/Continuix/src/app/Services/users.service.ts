import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from '../user';
import { Observable } from 'rxjs';
import { Group } from '../models/group';

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
    return this.httpClient.get<User[]>(`${this.baseURL}`, { headers: this.getHeaders() });
  }

  createUser(employee: User): Observable<Object>{
    return this.httpClient.post(`${this.baseURL}`, employee , { headers: this.getHeaders() });
  }

  getUserById(id: number): Observable<User>{
    return this.httpClient.get<User>(`${this.baseURL}/${id}`, { headers: this.getHeaders() });
  }

  updateUser(id: number, user: User): Observable<Object>{
    return this.httpClient.put(`${this.baseURL}/${id}`, user, { headers: this.getHeaders() });
  }

  deleteUser(id: number): Observable<Object>{
    return this.httpClient.delete(`${this.baseURL}/${id}`, { headers: this.getHeaders() });
  }

  resetPassword(token: string, newPassword: string): Observable<Object> {
  const payload = { token, newPassword };
  return this.httpClient.post(`${this.baseURL}/reset-password`, payload, { headers: this.getHeaders() });
}
getUsername(): string | null {
  return localStorage.getItem('username');
}
// Récupérer tous les groupes d’un utilisateur
getUserGroups(userId: number): Observable<Group[]> {
  return this.httpClient.get<Group[]>(`${this.baseURL}/groups/user/${userId}`, { headers: this.getHeaders() });
}

// Ajouter un utilisateur à un groupe
addUserToGroup(groupId: number, userId: number): Observable<Group> {
  return this.httpClient.post<Group>(`${this.baseURL}/groups/${groupId}/add-user/${userId}`, null, { headers: this.getHeaders() });
}

// Supprimer un utilisateur d’un groupe
removeUserFromGroup(groupId: number, userId: number): Observable<Group> {
  return this.httpClient.post<Group>(`${this.baseURL}/groups/${groupId}/remove-user/${userId}`, null, { headers: this.getHeaders() });
}
getUserStats(): Observable<any> {
  return this.httpClient.get<any>(`${this.baseURL}/stats`, { headers: this.getHeaders() });
}

}
