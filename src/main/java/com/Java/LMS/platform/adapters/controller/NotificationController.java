package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.domain.Entities.Notification;
import com.Java.LMS.platform.domain.Entities.User;
import com.Java.LMS.platform.enums.NotificationType;
import com.Java.LMS.platform.infrastructure.repository.UserRepository;
import com.Java.LMS.platform.service.NotificationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Bean;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private UserRepository userRepository;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService , UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    // Fetch all notifications for the authenticated user and mark them as read
    @GetMapping
    public ResponseEntity<List<Notification>> getAllNotifications() {
        Long userId = getUserIdFromToken();
        List<Notification> notifications = notificationService.getAndMarkNotificationsAsRead(userId);
        if (notifications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(notifications);
    }

    // Fetch only unread notifications for the authenticated user
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications() {
        Long userId = getUserIdFromToken();
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        if (notifications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(notifications);
    }

    // Mark a specific notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationService.markNotificationAsRead(notificationId);
        if (notification == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(notification);
    }

    // Extract user ID from the JWT token in the Authorization header
    private Long getUserIdFromToken() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUserId();
    }
}