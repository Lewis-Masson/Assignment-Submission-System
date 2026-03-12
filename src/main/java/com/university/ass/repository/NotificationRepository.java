package com.university.ass.repository;

import com.university.ass.model.Notification;
import com.university.ass.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByRecipientAndIsReadFalse(User recipient);
    List<Notification> findByRecipient(User recipient);
}