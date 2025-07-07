import { Component, OnInit } from '@angular/core';
import { UserNode } from '../user-node';
import { OrgChartService } from '../org-chart.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  orgChart: UserNode[] = [];

  constructor(private orgChartService: OrgChartService) {}

  ngOnInit(): void {
    this.orgChartService.getOrgChart().subscribe({
      next: (data) => {
        this.orgChart = data.map(node => this.mapSubordinatesToChildren(node));
        console.log('OrgChart transformée:', this.orgChart);
      },
      error: (err) => {
        console.error('Erreur API :', err);
      }
    });
  }

  private mapSubordinatesToChildren(node: UserNode): UserNode {
    return {
      ...node,
      label: node.username,
      data: {
        name: node.username,
        role: node.role,
     
      },
      children: node.subordinates ? node.subordinates.map(sub => this.mapSubordinatesToChildren(sub)) : []
    };
  }
}
