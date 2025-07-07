// src/app/pca.component.ts
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PcaService } from '../pca.service';
import { GroupService } from '../group.service';
import { Group } from '../models/group';
import Swal from 'sweetalert2';
import { PCA } from '../models/PCA';
import { Impact } from '../impact';
import { IncidentType } from '../incident-type';
declare var bootstrap: any;

@Component({
  selector: 'app-pca',
  templateUrl: './pca.component.html',
  styleUrls: ['./pca.component.css']
})
export class PCAComponent implements OnInit {
  pcaForm: FormGroup;
  editForm: FormGroup;
  pcas: PCA[] = [];
  groups: Group[] = [];
  editingPCA: PCA | null = null;
  modalInstance: any;

  @ViewChild('editModal') editModal!: ElementRef;

  incidentTypes = Object.values(IncidentType);
impacts = Object.values(Impact);

  constructor(
    private pcaService: PcaService,
    private groupService: GroupService,
    private fb: FormBuilder
  ) {
    this.pcaForm = this.fb.group({
      incidentType: ['', Validators.required],
      impact: ['', Validators.required],
      recommendedAction: ['', Validators.required],
      additionalDetails: [''],
      groupName: ['', Validators.required]
    });

    this.editForm = this.fb.group({
      incidentType: ['', Validators.required],
      impact: ['', Validators.required],
      recommendedAction: ['', Validators.required],
      additionalDetails: [''],
      groupName: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadPCAs();
    this.loadGroups();
  }

  loadPCAs(): void {
    this.pcaService.getAllPCAs().subscribe({
      next: (data) => {
        this.pcas = data;
      },
      error: (err) => {
        Swal.fire('Erreur', 'Erreur lors du chargement des PCA: ' + err.message, 'error');
      }
    });
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
  loadGroups(): void {
    this.groupService.getAllGroups().subscribe({
      next: (data) => {
        this.groups = data;
      },
      error: (err) => {
        Swal.fire('Erreur', 'Erreur lors du chargement des groupes: ' + err.message, 'error');
      }
    });
  }

  createPCA(): void {
    if (this.pcaForm.valid) {
      const pca: PCA = {
        incidentType: this.pcaForm.value.incidentType,
        impact: this.pcaForm.value.impact,
        recommendedAction: this.pcaForm.value.recommendedAction,
        additionalDetails: this.pcaForm.value.additionalDetails
      };
      this.pcaService.addPCA(pca, this.pcaForm.value.groupName).subscribe({
        next: (pca) => {
          this.pcas.push(pca);
          this.pcaForm.reset();
          Swal.fire('Succès', 'PCA créé avec succès !', 'success');
        },
        error: (err) => {
          Swal.fire('Erreur', 'Erreur lors de la création du PCA: ' + err.message, 'error');
        }
      });
    }
  }

  editPCA(pca: PCA): void {
    this.editingPCA = { ...pca };
    this.editForm.patchValue({
      incidentType: pca.incidentType,
      impact: pca.impact,
      recommendedAction: pca.recommendedAction,
      additionalDetails: pca.additionalDetails,
      groupName: pca.groupe?.name || ''
    });
    setTimeout(() => {
      this.modalInstance = new bootstrap.Modal(this.editModal.nativeElement);
      this.modalInstance.show();
    });
  }

  updatePCA(): void {
    if (this.editingPCA?.id && this.editForm.valid) {
      const pca: PCA = {
        id: this.editingPCA.id,
        incidentType: this.editForm.value.incidentType,
        impact: this.editForm.value.impact,
        recommendedAction: this.editForm.value.recommendedAction,
        additionalDetails: this.editForm.value.additionalDetails
      };
      this.pcaService.updatePCA(this.editingPCA.id, pca, this.editForm.value.groupName).subscribe({
        next: () => {
          this.loadPCAs();
          this.closeModal();
          Swal.fire('Succès', 'PCA modifié avec succès !', 'success');
        },
        error: (err) => {
          Swal.fire('Erreur', 'Erreur lors de la modification du PCA: ' + err.message, 'error');
        }
      });
    }
  }

  deletePCA(id: number): void {
    Swal.fire({
      title: 'Êtes-vous sûr ?',
      text: 'Cette action est irréversible.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui, supprimer',
      cancelButtonText: 'Annuler'
    }).then((result) => {
      if (result.isConfirmed) {
        this.pcaService.deletePCA(id).subscribe({
          next: () => {
            this.pcas = this.pcas.filter(p => p.id !== id);
            Swal.fire('Supprimé', 'PCA supprimé avec succès.', 'success');
          },
          error: (err) => {
            Swal.fire('Erreur', 'Erreur lors de la suppression: ' + err.message, 'error');
          }
        });
      }
    });
  }

  closeModal(): void {
    this.modalInstance?.hide();
    this.editingPCA = null;
    this.editForm.reset();
  }
}