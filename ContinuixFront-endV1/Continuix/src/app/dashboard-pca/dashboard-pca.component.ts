import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UsersService } from '../Services/users.service';

@Component({
  selector: 'app-dashboard-pca',
  templateUrl: './dashboard-pca.component.html',
  styleUrls: ['./dashboard-pca.component.css']
})
export class DashboardPCAComponent {
  userStats: any;
 constructor(private router: Router, private userService: UsersService) {}

  ngOnInit(): void {
    this.userService.getUserStats().subscribe({
      next: (data) => {
        this.userStats = data;
      },
      error: (err) => {
        console.error('Erreur chargement stats', err);
      }
    });
  }
}
