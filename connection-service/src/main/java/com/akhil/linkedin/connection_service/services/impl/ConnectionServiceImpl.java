package com.akhil.linkedin.connection_service.services.impl;

import com.akhil.linkedin.connection_service.entities.Connection;
import com.akhil.linkedin.connection_service.exceptions.ResourceNotFoundException;
import com.akhil.linkedin.connection_service.repositories.ConnectionRepository;
import com.akhil.linkedin.connection_service.services.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    private final ConnectionRepository connectionRepository;

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
    public void connectUsers(Long userId1, Long userId2) {
        log.info("Connecting users: {} and {}", userId1, userId2);

        // Validate that both users exist
        connectionRepository.findByUserId(userId1)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId1 + " not found"));
        connectionRepository.findByUserId(userId2)
                .orElseThrow(() -> new ResourceNotFoundException("User " + userId2 + " not found"));


        // Don't allow self-connections
        if (userId1.equals(userId2)) {
            throw new IllegalArgumentException("Users cannot connect to themselves");
        }

        if (!connectionRepository.areUsersConnected(userId1, userId2)) {
            connectionRepository.createConnection(userId1, userId2);
            log.info("Created connection from userId: {} to userId: {}", userId1, userId2);

            // Create reverse connection for undirected graph
            log.info("Creating reverse connection from userId: {} to userId: {}", userId2, userId1);
            connectionRepository.createConnection(userId2, userId1);
        }
        else {
            log.info("Users: {} and {} are already connected", userId1, userId2);
        }
    }

    public Connection createUser(Long userId, String name, String email) {

        Connection connection = Connection.builder()
                .userId(userId)
                .name(name)
                .email(email)
                .build();
        return connectionRepository.save(connection);
    }
}
