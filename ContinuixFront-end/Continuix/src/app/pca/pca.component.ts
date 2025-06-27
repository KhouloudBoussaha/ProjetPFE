import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { PCA } from '../models/PCA';
import { PcaService } from '../pca.service';
import { IncidentType } from '../incident-type';
import Swal from 'sweetalert2';
declare var bootstrap: any;
@Component({
  selector: 'app-pca',
  templateUrl: './pca.component.html',
  styleUrls: ['./pca.component.css']
})
export class PCAComponent  {
   pcas: PCA[] = [];
  newPCA: PCA = {
    incidentType: '',
    impact: '',
    recommendedAction: '',
    additionalDetails: '',
    label:''
  };

  editingPCA: PCA | null = null;
  modalInstance: any;

  @ViewChild('editModal') editModal!: ElementRef;

  constructor(private pcaService: PcaService) {}

  ngOnInit(): void {
    this.loadPCAs();
  }

  loadPCAs(): void {
    this.pcaService.getAllPCAs().subscribe({
      next: (data) => {
        this.pcas = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des PCA:', err);
      }
    });
  }

  createPCA(): void {
    this.pcaService.addPCA(this.newPCA).subscribe({
      next: (pca) => {
        this.pcas.push(pca);
        this.newPCA = {
          incidentType: '',
          impact: '',
          recommendedAction: '',
          additionalDetails: '',
          label:''
        };
        Swal.fire('Success', 'PCA created successfully!', 'success');
      },
      error: (err) => {
        console.error('Erreur de création:', err);
        Swal.fire('Error', 'Failed to create PCA.', 'error');
      }
    });
  }

  editPCA(pca: PCA): void {
    this.editingPCA = { ...pca };
    setTimeout(() => {
      this.modalInstance = new bootstrap.Modal(this.editModal.nativeElement);
      this.modalInstance.show();
    });
  }

  updatePCA(): void {
    if (this.editingPCA?.id) {
      this.pcaService.updatePCA(this.editingPCA.id, this.editingPCA).subscribe({
        next: () => {
          this.loadPCAs();
          this.closeModal();
          Swal.fire('Success', 'PCA updated successfully!', 'success');
        },
        error: (err) => {
          console.error('Erreur de modification:', err);
          Swal.fire('Error', 'Failed to update PCA.', 'error');
        }
      });
    }
  }

  deletePCA(id: number): void {
    Swal.fire({
      title: 'Are you sure? ?',
      text: 'This action cannot be undone.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Yes, delete it',
      cancelButtonText: 'Cancel'
    }).then(result => {
      if (result.isConfirmed) {
        this.pcaService.deletePCA(id).subscribe({
          next: () => {
            this.pcas = this.pcas.filter(p => p.id !== id);
            Swal.fire('Deleted', 'PCA has been deleted.', 'success');
          },
          error: (err) => {
            console.error('Erreur de suppression:', err);
            Swal.fire('Erreur', 'Impossible de supprimer la PCA.', 'error');
          }
        });
      }
    });
  }

  closeModal(): void {
    this.modalInstance?.hide();
    this.editingPCA = null;
  }
}
