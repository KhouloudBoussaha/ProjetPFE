// src/app/alerte.component.ts
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Alerte } from '../models/alerte';
import { AlerteService } from '../alerte.service';
import { PcaService } from '../pca.service';
import { AuthService } from '../auth.service';

import Swal from 'sweetalert2';
import { PCA } from '../models/PCA';
import { Impact } from '../impact';
import { IncidentType } from '../incident-type';
declare var bootstrap: any;

@Component({
  selector: 'app-alerte',
  templateUrl: './alerte.component.html',
  styleUrls: ['./alerte.component.css']
})
export class AlerteComponent implements OnInit {
  alerteForm: FormGroup;
  editForm: FormGroup;
  alertes: Alerte[] = [];
  editingAlerte: Alerte | null = null;
  recommendedPCA: PCA | null = null;
  modalInstance: any;

  @ViewChild('editModal') editModal!: ElementRef;

  incidentTypes = Object.values(IncidentType);
impacts = Object.values(Impact);

  constructor(
    private alerteService: AlerteService,
    private pcaService: PcaService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.alerteForm = this.fb.group({
      description: ['', Validators.required],
      type: ['', Validators.required],
      impact: ['', Validators.required]
    });

    this.editForm = this.fb.group({
      description: ['', Validators.required],
      type: ['', Validators.required],
      impact: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadAlertes();
  }
// Méthode pour déterminer la classe du badge en fonction de l'impact
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
  loadAlertes(): void {
    if (this.authService.isAdmin()) {
      this.alerteService.getAllAlertes().subscribe({
        next: (data) => (this.alertes = data),
        error: (err) => Swal.fire('Erreur', 'Erreur chargement alertes admin: ' + err.message, 'error')
      });
    } else {
      this.alerteService.getMyAlertes().subscribe({
        next: (data) => (this.alertes = data),
        error: (err) => Swal.fire('Erreur', 'Erreur chargement alertes user: ' + err.message, 'error')
      });
    }
  }

  createAlerte(): void {
    if (this.alerteForm.valid) {
      const alerte: Alerte = this.alerteForm.value;
      this.alerteService.createAlerte(alerte).subscribe({
        next: (alerte) => {
          this.alertes.push(alerte);
          this.alerteForm.reset();
          Swal.fire({
            icon: 'success',
            title: 'Alerte créée avec succès !',
            confirmButtonText: 'OK',
            confirmButtonColor: '#1BC5BD'
          });
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Impossible de créer l\'alerte: ' + err.message
          });
        }
      });
    }
  }

  editAlerte(alerte: Alerte): void {
    this.editingAlerte = { ...alerte };
    this.editForm.patchValue({
      description: alerte.description,
      type: alerte.type,
      impact: alerte.impact
    });
    setTimeout(() => {
      if (this.editModal?.nativeElement) {
        this.modalInstance = new bootstrap.Modal(this.editModal.nativeElement);
        this.modalInstance.show();
      }
    });
  }

  updateAlerte(): void {
    if (this.editingAlerte?.id && this.editForm.valid) {
      const alerte: Alerte = {
        id: this.editingAlerte.id,
        ...this.editForm.value
      };
      this.alerteService.updateAlerte(this.editingAlerte.id, alerte).subscribe({
        next: () => {
          this.loadAlertes();
          this.closeModal();
          Swal.fire({
            icon: 'success',
            title: 'Alerte modifiée avec succès !',
            confirmButtonText: 'OK',
            confirmButtonColor: '#1BC5BD'
          });
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Erreur',
            text: 'Impossible de modifier l\'alerte: ' + err.message
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
    }).then((result) => {
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
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
              text: 'Impossible de supprimer l\'alerte: ' + err.message
            });
          }
        });
      }
    });
  }

  resolveAlerte(id: number): void {
    Swal.fire({
      title: 'Résoudre cette alerte ?',
      text: 'Un PCA sera appliqué et les notifications seront envoyées.',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Oui, résoudre',
      cancelButtonText: 'Annuler',
      confirmButtonColor: '#1BC5BD',
      cancelButtonColor: '#E0E0E0'
    }).then((result) => {
      if (result.isConfirmed) {
        this.alerteService.resolveAlerte(id).subscribe({
          next: () => {
            const alerte = this.alertes.find(a => a.id === id);
            if (alerte) {
              this.pcaService.getPCAByIncidentTypeAndImpact(alerte.type, alerte.impact).subscribe({
                next: (pca) => {
                  this.recommendedPCA = pca;
                  Swal.fire({
                    icon: 'success',
                    title: 'Alerte résolue !',
                    html: `PCA recommandé : ${pca.recommendedAction}<br>Détails : ${pca.additionalDetails || 'N/A'}`,
                    confirmButtonText: 'OK',
                    confirmButtonColor: '#1BC5BD'
                  });
                  this.loadAlertes();
                },
                error: (err) => {
                  Swal.fire({
                    icon: 'error',
                    title: 'Erreur',
                    text: 'Aucun PCA trouvé pour ce type et impact: ' + err.message
                  });
                }
              });
            }
          },
          error: (err) => {
            Swal.fire({
              icon: 'error',
              title: 'Erreur',
       
              text: 'Impossible de résoudre l\'alerte: ' + err.message
            });
          }
        });
      }
    });
  }

  closeModal(): void {
    this.modalInstance?.hide();
    this.editingAlerte = null;
    this.editForm.reset();
  }
}