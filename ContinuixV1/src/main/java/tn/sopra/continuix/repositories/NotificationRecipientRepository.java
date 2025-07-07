package tn.sopra.continuix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tn.sopra.continuix.entities.NotificationRecipient;
import tn.sopra.continuix.entities.Notification;
import tn.sopra.continuix.entities.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRecipientRepository  extends JpaRepository<NotificationRecipient, Long> {

    List<Notification> findByRecipientId(@Param("recipientId") Long recipientId);

    Optional<NotificationRecipient> findByNotificationAndRecipient(Notification notification, Users recipient);

    List<NotificationRecipient> findByRecipient(Users recipient);
    @Modifying // ✅ Nécessaire pour DELETE ou UPDATE
    @Transactional
    @Query("DELETE FROM NotificationRecipient nr WHERE nr.recipient.id = :userId")
    void deleteByRecipientId(@Param("userId") Long userId);
}
