import { Component, OnInit } from '@angular/core';
import { Alerte } from '../models/alerte';
import { AlerteService } from '../alerte.service';
import { IncidentType } from '../incident-type';
import { Impact } from '../impact';
import { AuthService } from '../auth.service';

@Component({
  selector: 'app-admin-incident-list',
  templateUrl: './admin-incident-list.component.html',
  styleUrls: ['./admin-incident-list.component.css']
})
export class AdminIncidentListComponent implements OnInit {
  alertes: Alerte[] = [];

incidentTypes = Object.keys(IncidentType).filter(key => isNaN(+key));
  impact = Object.keys(Impact).filter(key => isNaN(+key));
  constructor(private alerteService: AlerteService ,private authService: AuthService) {}


  ngOnInit(): void {
    this.loadAlertes(); 
  }


  loadAlertes(): void {
    this.alerteService.getAllAlertes().subscribe({
      next: (data) => {
        console.log('Alertes chargées:', data);
        this.alertes = data;
      },
      error: (error) => {
        console.error('Erreur chargement alertes:', error);
      }
    });
  }

  markAsResolved(alerte: Alerte): void {
  if (!alerte.id) return;

  this.alerteService.resolveAlerte(alerte.id).subscribe({
    next: (updatedAlerte) => {
      // Mise à jour locale de l'alerte dans la liste
      const index = this.alertes.findIndex(a => a.id === updatedAlerte.id);
      if (index !== -1) {
        this.alertes[index] = updatedAlerte;
      }
    },
    error: (err) => console.error('Erreur lors du marquage comme résolu', err)
  });
}
getResolvedBadgeClass(resolved: boolean | undefined): string {
  return resolved ? 'badge bg-success' : 'badge bg-danger';
}


  getImpactBadgeClass(impact: string): string {
    switch (impact) {
      case 'HIGH':
        return 'bg-danger'; // Rouge pour impact élevé
      case 'MEDIUM':
        return 'bg-warning'; // Jaune pour impact moyen
      case 'LOW':
        return 'bg-success'; // Vert pour impact faible
      default:
        return 'bg-secondary'; // Gris par défaut
    }}



}
