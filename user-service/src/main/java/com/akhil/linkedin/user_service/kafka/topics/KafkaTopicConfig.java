package com.akhil.linkedin.user_service.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic userSignupTopic() {
        return new NewTopic("user-signup-topic", 3, (short) 1);
    }
}
