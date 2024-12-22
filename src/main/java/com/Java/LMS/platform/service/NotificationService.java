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
    public Notification createNotification(Long userId, Long senderId, NotificationType type, String content) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setSenderId(senderId);
        notification.setType(type);
        notification.setContent(content);
        return notificationRepository.save(notification);
    }

    // Fetch and mark all notifications as read for the user
    public List<Notification> getAndMarkNotificationsAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        notifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(notifications);
        return notifications;
    }

    // Fetch only unread notifications for the user
    public List<Notification> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    // Mark a specific notification as read
    public Notification markNotificationAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setIsRead(true);
        return notificationRepository.save(notification);
    }
}


