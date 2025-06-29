import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { UserNode } from './user-node';
import { environment } from 'src/environments/environments';

@Injectable({
  providedIn: 'root'
})
export class OrgChartService {
 
  private apiUrl = `${environment.apiUrl}/notification/orgchart`;

  constructor(private http: HttpClient) {}

  getOrgChart(): Observable<UserNode[]> {
    return this.http.get<UserNode[]>(this.apiUrl).pipe(
      map(nodes => this.transformNodes(nodes))
    );
  }

  private transformNodes(nodes: UserNode[]): UserNode[] {
    return nodes.map(node => ({
      ...node,
      label: node.username, // PrimeNG utilise 'label' pour afficher le texte du nœud
      expanded: true, // Ouvre les nœuds par défaut
      data: {
        name: node.username,
        role: node.role
      },
      children: node.subordinates ? this.transformNodes(node.subordinates) : []
    }));
  }

  getImageForRole(role: string): string {
    const images = {
      ADMIN: 'assets/images/admin.png',
      MANAGER: 'assets/images/manager.png',
      TEAM_LEADER: 'assets/images/team-leader.png',
      EMPLOYEE: 'assets/images/employee.png'
    };
    return images[role as keyof typeof images] || 'assets/images/default.png';
  }
}