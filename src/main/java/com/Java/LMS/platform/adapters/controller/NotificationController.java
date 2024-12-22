package com.Java.LMS.platform.adapters.controller;
import com.Java.LMS.platform.domain.Entities.Notification;
import com.Java.LMS.platform.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Fetch all notifications for the authenticated user and mark them as read
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        Long userId = getAuthenticatedUserId();
        List<Notification> notifications = notificationService.getAndMarkNotificationsAsRead(userId);
        return ResponseEntity.ok(notifications);
    }

    // Fetch only unread notifications for the authenticated user
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        Long userId = getAuthenticatedUserId();
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    // Mark a specific notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    // Utility method to extract authenticated user ID from SecurityContext
    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        // Assuming the principal contains user details with an ID
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        return userDetails.getUserId();
    }
}
