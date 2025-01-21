package com.akhil.linkedin.connection_service.services.impl;

import com.akhil.linkedin.connection_service.entities.Connection;
import com.akhil.linkedin.connection_service.exceptions.ResourceNotFoundException;
import com.akhil.linkedin.connection_service.kafka.events.AcceptConnectionRequestEvent;
import com.akhil.linkedin.connection_service.kafka.events.SendConnectionRequestEvent;
import com.akhil.linkedin.connection_service.repositories.ConnectionRepository;
import com.akhil.linkedin.connection_service.services.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final KafkaTemplate<Long, SendConnectionRequestEvent> sendConnectionRequestTemplate;
    private final KafkaTemplate<Long, AcceptConnectionRequestEvent> acceptConnectionRequestTemplate;

    @Override
    public List<Connection> findFirstDegreeConnections(Long userId) {
        log.info("Finding first-degree connections for userId: {}", userId);

        connectionRepository.findByUserId(userId).orElseThrow(() -> {
            log.error("User with userId: {} not found", userId);
            return new ResourceNotFoundException("User not found with userId: " + userId);
        });

        List<Connection> connections = connectionRepository.findFirstDegreeConnections(userId);

        log.info("Found {} first-degree connections for userId: {}", connections.size(), userId);
        return connections;
    }

    @Override
    public List<Connection> findSecondDegreeConnections(Long userId) {
        log.info("Finding second-degree connections for userId: {}", userId);

        connectionRepository.findByUserId(userId).orElseThrow(() -> {
            log.error("User with userId: {} not found", userId);
            return new ResourceNotFoundException("User not found with userId: " + userId);
        });

        List<Connection> connections = connectionRepository.findSecondDegreeConnections(userId);

        log.info("Found {} second-degree connections for userId: {}", connections.size(), userId);
        return connections;
    }

    @Override
    public boolean sendConnectionRequest(Long senderId, Long receiverId) {
        log.info("Sending connection request from senderId: {} to receiverId: {}", senderId, receiverId);

        if(senderId.equals(receiverId)) {
            throw new RuntimeException("Both sender and receiver are same");
        }

        boolean alreadySentRequest = connectionRepository.connectionRequestExists(senderId, receiverId);
        if(alreadySentRequest) {
            throw new RuntimeException("Connection request already sent, cannot send again");
        }

        boolean alreadyConnected = connectionRepository.alreadyConnected(senderId, receiverId);
        if(alreadyConnected) {
            throw new RuntimeException("Already connected users, cannot add connection request");
        }

        log.info("Successfully sent the connection request");
        connectionRepository.addConnectionRequest(senderId, receiverId);

        SendConnectionRequestEvent  sendConnectionRequestEvent = SendConnectionRequestEvent
                .builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .build();

        sendConnectionRequestTemplate.send("send-connection-request-topic", sendConnectionRequestEvent);
        log.info("Successfully send the send notification request to receiverId {}, from senderId {}", receiverId, senderId);
        return true;
    }

    @Override
    public boolean acceptConnectionRequest(Long senderId, Long receiverId) {
        log.info("Accepting connection request from senderId: {} to receiverId: {}", senderId, receiverId);

        boolean connectionRequestExists = connectionRepository.connectionRequestExists(senderId, receiverId);
        if(!connectionRequestExists) {
            throw new RuntimeException("Connection request doest not exists to accept");
        }

        connectionRepository.acceptConnectionRequest(senderId, receiverId);

        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent
                .builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .build();
        acceptConnectionRequestTemplate.send("accept-connection-request-topic", acceptConnectionRequestEvent);
        log.info("Successfully accepted the connection request by receiverId {}, from senderId {}", receiverId, senderId);
        return true;
    }

    @Override
    public boolean rejectConnectionRequest(Long senderId, Long receiverId) {
        log.info("Rejecting connection request from senderId: {} to receiverId: {}", senderId, receiverId);

        boolean connectionRequestExists = connectionRepository.connectionRequestExists(senderId, receiverId);
        if(!connectionRequestExists) {
            throw new RuntimeException("Connection request doest not exists to reject");
        }

        connectionRepository.rejectConnectionRequest(senderId, receiverId);

        log.info("Successfully rejected the connection request from senderId");
        return true;
    }
}
