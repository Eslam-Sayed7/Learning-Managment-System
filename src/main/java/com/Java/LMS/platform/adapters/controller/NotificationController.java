package com.Java.LMS.platform.adapters.controller;

import com.Java.LMS.platform.domain.Entities.Notification;
import com.Java.LMS.platform.enums.NotificationType;
import com.Java.LMS.platform.service.NotificationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<Notification>> getAllNotifications(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        List<Notification> notifications = notificationService.getAndMarkNotificationsAsRead(userId);
        return ResponseEntity.ok(notifications);
    }

    // Fetch only unread notifications for the authenticated user
    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(HttpServletRequest request) {
        Long userId = getUserIdFromToken(request);
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    // Mark a specific notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationService.markNotificationAsRead(notificationId);
        return ResponseEntity.ok(notification);
    }

    // Extract user ID from the JWT token in the Authorization header
    private Long getUserIdFromToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = header.substring(7);
        Claims claims = Jwts.parserBuilder()
                .setSigningKey("your-secret-key".getBytes()) // Replace with your actual secret key
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.get("userId", String.class)); // Ensure the token includes the "userId" claim
    }
}