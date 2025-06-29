import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Notification } from 'src/app/models/notification';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})
export class NotificationService {
private apiUrl = `${environment.apiUrl}/notifications`;
  

  constructor(private http: HttpClient) {}

  /**
   * Envoie une notification à tous les utilisateurs.
   */
  createNotification(title: string, message: string, incidentType: string, impact: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/send`, {
      title,
      message,
      incidentType,
      impact
    });
  }

  /**
   * Récupère toutes les notifications envoyées à un destinataire donné.
   */
  getNotificationsForRecipient(recipientId: number): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/recipient/${recipientId}`);
  }

  /**
   * Récupère l'historique complet des notifications.
   */
  getNotificationHistory(): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/history`);
  }

  /**
   * Récupère une notification spécifique par ID.
   */
 getNotificationById(id: number): Observable<Notification> {
  return this.http.get<Notification>(`${this.apiUrl}/${id}/view`);
}

  /**
   * Accepte (acknowledge) une notification en tant qu'utilisateur connecté.
   * L'utilisateur est identifié côté serveur par son token JWT.
   */
viewNotification(id: number): Observable<Notification> {
  return this.http.get<Notification>(`${this.apiUrl}/${id}/view`);
}

acknowledgeNotification(id: number): Observable<void> {
  return this.http.post<void>(`${this.apiUrl}/${id}/acknowledge`, {});
}

  /**
   * Récupère les notifications de l'utilisateur actuellement connecté.
   */
 // notification.service.ts

getMyNotifications(): Observable<Notification[]> {
  return this.http.get<Notification[]>(`${this.apiUrl}/me`);
}

}
