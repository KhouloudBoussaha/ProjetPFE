import { Component, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { NotificationService } from '../notification.service';
import { Notification } from 'src/app/models/notification';


@Component({
  selector: 'app-notification-history',
  templateUrl: './notification-history.component.html',
  styleUrls: ['./notification-history.component.css']
})
export class NotificationHistoryComponent implements OnInit {
  notifications: Notification[] = [];

  constructor(private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.notificationService.getNotificationHistory().subscribe({
      next: data => this.notifications = data,
      error: err => {
        Swal.fire({
          icon: 'error',
          title: 'Error loading notifications',
          text: 'An error occurred while loading the notification history.',
          footer: `<code>${err.message}</code>`
        });
      }
    });
  }


}
