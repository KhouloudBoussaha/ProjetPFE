import { Component, ElementRef, ViewChild } from '@angular/core';
import { Alerte } from '../models/alerte';
import { IncidentType } from '../incident-type';
import { AlerteService } from '../alerte.service';
import Swal from 'sweetalert2';

declare var bootstrap: any;

@Component({
  selector: 'app-alerte',
  templateUrl: './alerte.component.html',
  styleUrls: ['./alerte.component.css']
})
export class AlerteComponent {
  alertes: Alerte[] = [];
  incidentTypes = Object.values(IncidentType);
  newAlerte: Alerte = { description: '', type: IncidentType.AUTHENTICATION_ISSUE };
  editingAlerte: Alerte | null = null;
  filterType: IncidentType | '' = '';
  startDate: string = '';
  endDate: string = '';
  showUnresolved: boolean = false;

  @ViewChild('editModal') editModal!: ElementRef;

  constructor(private alerteService: AlerteService) {}

  ngOnInit(): void {
    this.loadAlertes();
  }

  loadAlertes(): void {
    if (this.showUnresolved) {
      this.alerteService.getUnresolvedAlertes().subscribe(alertes => this.alertes = alertes);
    } else if (this.filterType) {
      this.alerteService.getAlertesByType(this.filterType as IncidentType).subscribe(alertes => this.alertes = alertes);
    } else if (this.startDate && this.endDate) {
      this.alerteService.getAlertesByDateRange(this.startDate, this.endDate).subscribe(alertes => this.alertes = alertes);
    } else {
      this.alerteService.getAllAlertes().subscribe(alertes => this.alertes = alertes);
    }
  }

  createAlerte(): void {
    this.alerteService.createAlerte(this.newAlerte).subscribe({
      next: (alerte) => {
        this.alertes.push(alerte);
        this.newAlerte = { description: '', type: IncidentType.AUTHENTICATION_ISSUE };

        Swal.fire({
          icon: 'success',
          title: 'Alerte créée avec succès !',
          confirmButtonText: 'OK',
          confirmButtonColor: '#1BC5BD'
        });
      },
      error: (err) => {
        console.error('Error creating alerte:', err);
        Swal.fire({
          icon: 'error',
          title: 'Erreur',
          text: 'Impossible de créer l\'alerte.'
        });
      }
    });
  }

  editAlerte(alerte: Alerte): void {
    this.editingAlerte = { ...alerte };
    setTimeout(() => {
      const modal = new bootstrap.Modal(this.editModal.nativeElement);
      modal.show();
    });
  }

  updateAlerte(): void {
    if (this.editingAlerte && this.editingAlerte.id) {
      this.alerteService.updateAlerte(this.editingAlerte.id, this.editingAlerte).subscribe({
        next: (updatedAlerte) => {
          const index = this.alertes.findIndex(a => a.id === updatedAlerte.id);
          if (index !== -1) {
            this.alertes[index] = updatedAlerte;
          }

          const modalInstance = bootstrap.Modal.getInstance(this.editModal.nativeElement);
          modalInstance?.hide();

          this.editingAlerte = null;

          Swal.fire({
            icon: 'success',
            title: 'Alerte modifiée avec succès !',
            confirmButtonText: 'OK',
            confirmButtonColor: '#1BC5BD'
          });
        },
        error: (err) => {
          console.error('Error updating alerte:', err);
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Impossible de modifier l\'alerte.'
          });
        }
      });
    }
  }

  deleteAlerte(id: number): void {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: 'Cette action est irréversible.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler',
      confirmButtonColor: '#F64E60',
      cancelButtonColor: '#E0E0E0'
    }).then(result => {
      if (result.isConfirmed) {
        this.alerteService.deleteAlerte(id).subscribe({
          next: () => {
            this.alertes = this.alertes.filter(a => a.id !== id);
            Swal.fire({
              icon: 'success',
              title: 'Alerte supprimée !',
              confirmButtonText: 'OK',
              confirmButtonColor: '#1BC5BD'
            });
          },
          error: (err) => {
            console.error('Error deleting alerte:', err);
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: 'Impossible de supprimer l\'alerte.'
            });
          }
        });
      }
    });
  }
}
