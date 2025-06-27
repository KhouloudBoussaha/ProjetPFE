import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationService } from '../notification.service';
import { Notification } from '../models/notification';
import { PcaService } from '../pca.service';
import { AiRequestDTO } from '../ai-request-dto';
import { PCA } from '../models/PCA';
import { IncidentType } from '../incident-type';
import { Impact } from '../impact';
import { AiService } from '../ai.service';
import { AuthService } from '../auth.service';


@Component({
  selector: 'app-notification-consult',
  templateUrl: './notification-consult.component.html',
  styleUrls: ['./notification-consult.component.css']
})
export class NotificationConsultComponent implements OnInit {
  notification: Notification | null = null;
  loading = true;
  message: string | null = null;
  selectedPCA?: PCA;
  isAcknowledgeRoute: boolean = false;
  acknowledgeSuccess: boolean = false;
  acknowledgeError: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private notificationService: NotificationService,
    private aiservice: AiService,
    private authService: AuthService,
    private router: Router,
    
  ) {}
ngOnInit(): void {
  if (!this.authService.isLoggedIn()) {
    this.router.navigate(['/login'], { queryParams: { redirect: this.router.url } });
    return;
  }

  this.loading = true;
  const id = +this.route.snapshot.params['id'];

  if (!id || isNaN(id)) {
    this.message = "ID de notification invalide.";
    this.loading = false;
    return;
  }

  this.notificationService.getNotificationById(id).subscribe({
    next: data => {
      this.notification = data;
      this.loading = false;
    },
    error: err => {
      console.error('Erreur lors de la récupération de la notification :', err);
      this.message = 'Erreur lors du chargement de la notification.';
      this.loading = false;
    }
  });
}




onAcknowledge(): void {
  if (!this.notification || !this.notification.id) {
    console.error('Notification est null ou id manquant, impossible de faire acknowledge.');
    return;  // Stop l'exécution si notification invalide
  }

  this.notificationService.acknowledgeNotification(this.notification.id).subscribe({
    next: () => {
      if (this.notification) {
        this.notification.accepted = true;
        this.acknowledgeSuccess = true;
        this.acknowledgeError = null;
        this.message = 'Accusé de réception enregistré avec succès.';
      }
    },
    error: err => {
      this.acknowledgeSuccess = false;
      this.acknowledgeError = err.status === 409 ? 'Notification déjà accusée.' : 
                             err.status === 404 ? 'Notification ou utilisateur non trouvé.' : 
                             'Erreur lors de l\'accusé de réception.';
      this.message = this.acknowledgeError;
    }
  });
}



  predict(notification: Notification) {
    const dto: AiRequestDTO = {
      incident_type_encoded: this.encodeIncidentType(notification.incidentType),
      impact_encoded: this.encodeImpact(notification.impact)
    };

    this.aiservice.predictPca(dto).subscribe({
      next: (res) => this.selectedPCA = res,
      error: () => alert('Erreur de prédiction PCA')
    });
  }

  closeModal() {
    this.selectedPCA = undefined;
  }

  private encodeIncidentType(type: IncidentType): number {
    switch (type) {
      case IncidentType.AUTHENTICATION_ISSUE: return 1;
      case IncidentType.NETWORK_ISSUE: return 2;
      case IncidentType.SYSTEM_ERROR: return 3;
      case IncidentType.USER_REQUEST: return 4;
      case IncidentType.NATURAL_INCIDENT: return 5;
      case IncidentType.DEVELOPMENT_ENVIRONMENT: return 6;
      case IncidentType.OTHER_IT_INCIDENT: return 7;
      case IncidentType.UNKNOWN: return 8;
      default: return 0;
    }
  }

  private encodeImpact(impact: Impact): number {
    switch (impact) {
      case Impact.LOW: return 1;
      case Impact.MEDIUM: return 2;
      case Impact.HIGH: return 3;
      default: return 0;
    }
  }
}