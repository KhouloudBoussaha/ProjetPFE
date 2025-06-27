package tn.sopra.continuix.services;

import tn.sopra.continuix.dtos.NotificationDTO;
import tn.sopra.continuix.request.NotificationRequest;

public interface NotificationService {
    NotificationDTO viewNotification(Long notificationId, String email);
    void acknowledgeNotification(Long notificationId, String email);
    void createNotificationForAllUsers(NotificationRequest request);
}
