package tn.sopra.continuix.services;

import tn.sopra.continuix.dtos.NotificationDTO;
import tn.sopra.continuix.entities.Alerte;
import tn.sopra.continuix.entities.Notification;
import tn.sopra.continuix.entities.PCA;
import tn.sopra.continuix.entities.Users;
import tn.sopra.continuix.request.NotificationRequest;

import java.util.List;

public interface NotificationService {
    NotificationDTO viewNotification(Long notificationId, String email);
    void acknowledgeNotification(Long notificationId, String email);
    Notification createNotificationForUsers(List<Users> users, Alerte alerte, PCA pca, Users admin);


}
