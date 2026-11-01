package miralhas.github.stalkers.domain.notification.repository;

import miralhas.github.stalkers.domain.notification.model.NotificationRecipient;
import miralhas.github.stalkers.domain.notification.model.NotificationRecipientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRecipientRepository extends JpaRepository<NotificationRecipient, NotificationRecipientId> {
  @Query("SELECT nr from NotificationRecipient nr where nr.recipient.id = :userId and nr.isRead = FALSE")
  List<NotificationRecipient> findAllUserNotificationRecipients(Long userId);

  @Modifying
  @Query("UPDATE NotificationRecipient nr SET nr.isRead = TRUE where nr.recipient.id = :userId and nr.isRead = FALSE ")
  void updateAllUserUnreadNotifications(Long userId);
}