package com.akhil.linkedin.notification_service.consumers;

import com.akhil.linkedin.notification_service.clients.ConnectionFeignClient;
import com.akhil.linkedin.notification_service.dtos.response.ConnectionResponse;
import com.akhil.linkedin.notification_service.services.NotificationService;
import com.akhil.linkedin.post_service.events.PostCreatedEvent;
import com.akhil.linkedin.post_service.events.PostLikedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceConsumer {

    private final ConnectionFeignClient connectionFeignClient;
    private final NotificationService notificationService;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent) {
        log.info("Post created event received: {}", postCreatedEvent);

       ConnectionResponse firstDegreeConnections = connectionFeignClient.getFirstDegreeConnections(postCreatedEvent.getCreatorId());

        if (firstDegreeConnections == null || firstDegreeConnections.getError() != null) {
            throw new RuntimeException("Error fetching connections: " +
                    (firstDegreeConnections != null ? firstDegreeConnections.getError() : "No response received"));
        }

        for(ConnectionResponse.Connection connection: firstDegreeConnections.getData()) {
            notificationService.send(connection.getUserId(),
                    "Your connection " + connection.getName() + " created a post");
        }
    }

    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent) {
        log.info("Post liked event received: {}", postLikedEvent);

        String message = String.format("Your post, %d has been liked by %d", postLikedEvent.getPostId(),
                postLikedEvent.getLikedByUserId());

            notificationService.send(postLikedEvent.getCreatorId(), message);
    }


}
