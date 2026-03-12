package com.university.ass.service;

import com.university.ass.model.Notification;
import com.university.ass.model.User;
import com.university.ass.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification createNotification(User recipient, String message) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setMessage(message);
        return notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications(User recipient) {
        return notificationRepository.findByRecipientAndIsReadFalse(recipient);
    }

    public List<Notification> getAllNotifications(User recipient) {
        return notificationRepository.findByRecipient(recipient);
    }

    public void markAsRead(int notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    public void markAllAsRead(User recipient) {
        List<Notification> notifications = notificationRepository.findByRecipientAndIsReadFalse(recipient);
        notifications.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(notifications);
    }
}