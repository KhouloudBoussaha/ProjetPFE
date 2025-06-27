import { Component, ElementRef, AfterViewInit, ViewChild, OnInit } from '@angular/core';

import { UserNode } from '../user-node';
import { OrgChartService } from '../org-chart.service';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  orgChart: UserNode[] = [];

  constructor(private orgChartService: OrgChartService ,private authService: AuthService) {}

  ngOnInit(): void {
    this.orgChartService.getOrgChart().subscribe({
      next: (data) => {
        console.log('Structure des données orgChart :', JSON.stringify(data, null, 2));
        this.orgChart = data;
      },
      error: (err) => {
        console.error('Erreur API :', err);
      }
    });
  }

  
}