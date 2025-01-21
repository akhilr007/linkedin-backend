package com.akhil.linkedin.notification_service.services;

import com.akhil.linkedin.notification_service.entities.Notification;
import com.akhil.linkedin.notification_service.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(Long userId, String message) {
        log.info("Sending Notification to UserId: {}", userId);

        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);

        notificationRepository.save(notification);
        log.info("Notification message: {}", notification.getMessage());
        log.info("Notification successfully saved in DB for {}", userId);

    }
}
