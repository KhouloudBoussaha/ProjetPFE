import { Component } from '@angular/core';
import { NotificationService } from '../notification.service';
import { AuthService } from '../auth.service';
import Swal from 'sweetalert2';
import { IncidentType } from '../incident-type';
import { Impact } from '../impact';

@Component({
  selector: 'app-notification-form',
  templateUrl: './notification-form.component.html',
  styleUrls: ['./notification-form.component.css']
})
export class NotificationFormComponent {
  title = '';
  message = '';
  incidentType = '';
  impact = '';
   incidentTypes: string[] = Object.keys(IncidentType).filter(key => isNaN(Number(key)));
  impacts: string[] = Object.keys(Impact).filter(key => isNaN(Number(key)));
  constructor(private notificationService: NotificationService, public authService: AuthService) {}

  submitNotification() {
    this.notificationService.createNotification( this.title, this.message, this.incidentType , this.impact)
      .subscribe({
        next: () => {
          // ✅ Popup de succès
          Swal.fire({
            icon: 'success',
            title: 'Notification envoyée avec succès !',
            confirmButtonText: 'Ok, super !',
            confirmButtonColor: '#1BC5BD'
          });
          this.title = '';
        this.message = '';
        this.incidentType = '';
        this.impact = '';

          // Optionnel : reset formulaire après envoi
        
        },
        error: (error) => {
          console.error('Erreur lors de l’envoi :', error);

          // ❌ Popup d’erreur
          Swal.fire({
            icon: 'error',
            title: 'Oops !',
            text: 'Une erreur est survenue lors de l’envoi de la notification.',
            confirmButtonColor: '#F64E60'
          });
        }
      });
  }
}
