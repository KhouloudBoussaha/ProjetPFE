import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GroupRequestDTO } from './models/group-request-dto';
import { Group } from './models/group';


@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private apiUrl = 'http://localhost:8075/api/groups';

  constructor(private http: HttpClient) {}

  // ✅ Récupérer tous les groupes
  getAllGroups(): Observable<Group[]> {
    return this.http.get<Group[]>(this.apiUrl);
  }

 createGroup(groupDto: GroupRequestDTO): Observable<Group> {
  return this.http.post<Group>(this.apiUrl, groupDto);
}

  // ✅ Supprimer un groupe
  deleteGroup(groupId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${groupId}`);
  }
updateGroup(groupDto: GroupRequestDTO): Observable<Group> {
  return this.http.put<Group>(`${this.apiUrl}/${groupDto.id}`, groupDto);
}

}
