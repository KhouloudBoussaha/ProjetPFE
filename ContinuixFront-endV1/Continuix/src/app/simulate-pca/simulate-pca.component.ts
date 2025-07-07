import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IncidentType } from '../incident-type';
import { Impact } from '../impact';
import { PcaService } from '../pca.service';
import { SimulationRequest } from '../models/simulation-request';
import { GroupService } from '../group.service';

@Component({
  selector: 'app-simulate-pca',
  templateUrl: './simulate-pca.component.html',
  styleUrls: ['./simulate-pca.component.css']
})
export class SimulatePcaComponent implements OnInit {
  simulateForm!: FormGroup;
  incidentTypes = Object.values(IncidentType);
  impacts = Object.values(Impact);
  groupes: any[] = []; // À remplir via backend

  isSubmitting = false;
  submissionMessage = '';
  errorMessage = '';

  constructor(private fb: FormBuilder, private pcaService: PcaService,  private groupService: GroupService ) {}

  ngOnInit(): void {
    // Initialiser le formulaire avec valeurs par défaut et validation
    this.simulateForm = this.fb.group({
      incidentType: [this.incidentTypes[0], Validators.required],
      impact: [this.impacts[0], Validators.required],
      groupeId: [null, Validators.required],
      commentaire: ['']
    });

    // Charger la liste des groupes depuis backend
    this.loadGroupes();
  }

 loadGroupes(): void {
    this.groupService.getAllGroups().subscribe({
      next: (data) => {
        this.groupes = data;
      },
      error: (err) => {
        console.error('Erreur chargement groupes :', err);
      }
    });
  }
  onSubmit(): void {
    if (this.simulateForm.invalid) {
      this.simulateForm.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    this.submissionMessage = '';
    this.errorMessage = '';

    const request: SimulationRequest = this.simulateForm.value;

    this.pcaService.simulatePca(request).subscribe({
      next: (res) => {
        this.submissionMessage = res;
        this.simulateForm.reset();
        this.isSubmitting = false;
      },
      error: (err) => {
        this.errorMessage = err.error || 'Erreur serveur';
        this.isSubmitting = false;
      }
    });
  }
}
