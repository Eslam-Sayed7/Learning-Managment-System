package com.Java.LMS.platform.service;

import com.Java.LMS.platform.domain.Entities.Notification;
import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.enums.NotificationType;
import com.Java.LMS.platform.infrastructure.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Create a new notification
    public Notification createNotification(User user, User sender, NotificationType type, String content) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setSender(sender);
        notification.setType(type);
        notification.setContent(content);
        return notificationRepository.save(notification);
    }

    // Fetch and mark all notifications as read for the user
    public List<Notification> getAndMarkNotificationsAsRead(Long userId) {
        // Fetch notifications
        List<Notification> notifications = notificationRepository.findByUserUserId(userId);

        // Mark them as read
        notifications.forEach(notification -> notification.setIsRead(true));

        // Save changes
        notificationRepository.saveAll(notifications);

        return notifications;
    }

    // Fetch only unread notifications for the user
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserUserIdAndIsReadFalse(userId);
    }

    // Mark a specific notification as read
    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }
}

