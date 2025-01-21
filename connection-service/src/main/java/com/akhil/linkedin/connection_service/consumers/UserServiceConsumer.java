package com.akhil.linkedin.connection_service.consumers;

import com.akhil.linkedin.connection_service.repositories.ConnectionRepository;
import com.akhil.linkedin.user_service.kafka.events.UserSignupEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceConsumer {

    private final ConnectionRepository connectionRepository;

    @KafkaListener(topics = "user-signup-topic")
    public void handleUserSignupEvent(UserSignupEvent userSignupEvent) {
        connectionRepository
                .createUserNode(userSignupEvent.getUserId(), userSignupEvent.getName(), userSignupEvent.getEmail());
        log.info("Successfully created user node for UserSignupEvent {}", userSignupEvent);
    }
}
