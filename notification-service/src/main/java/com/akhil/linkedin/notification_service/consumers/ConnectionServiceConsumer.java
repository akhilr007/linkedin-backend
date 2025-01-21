package com.akhil.linkedin.notification_service.consumers;

import com.akhil.linkedin.connection_service.kafka.events.AcceptConnectionRequestEvent;
import com.akhil.linkedin.connection_service.kafka.events.SendConnectionRequestEvent;
import com.akhil.linkedin.notification_service.services.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics="send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent) {
        log.info("Send Connection Request Event received {}", sendConnectionRequestEvent);

        Long senderId = sendConnectionRequestEvent.getSenderId();
        Long receiverId = sendConnectionRequestEvent.getReceiverId();
        String message = String.format("You have received a connection request from SenderId %d", senderId);

        notificationService.send(receiverId, message);
        log.info("Successfully sent the connection request to ReceiverId {}", receiverId);
    }

    @KafkaListener(topics="accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent) {
        log.info("Accept Connection Request Event received {}", acceptConnectionRequestEvent);

        Long senderId = acceptConnectionRequestEvent.getSenderId();
        Long receiverId = acceptConnectionRequestEvent.getReceiverId();
        String message = String.format("Your connection request has been accepted by ReceiverId %d", receiverId);

        notificationService.send(senderId, message);
        log.info("Successfully accepted the connection request from senderId {}", senderId);
    }
}
