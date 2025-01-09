package com.akhil.linkedin.connection_service.controllers;

import com.akhil.linkedin.connection_service.auth.UserContextHolder;
import com.akhil.linkedin.connection_service.entities.Connection;
import com.akhil.linkedin.connection_service.services.ConnectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConnectionController {

    private final ConnectionService connectionService;

    @GetMapping("/first-degree")
    ResponseEntity<List<Connection>> getFirstDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Received request for first-degree connections for userId: {}", userId);

        List<Connection> connections = connectionService.findFirstDegreeConnections(userId);

        log.info("Returning {} first-degree connections for userId: {}", connections.size(), userId);
        return ResponseEntity.ok(connections);
    }

    @GetMapping("/second-degree")
    ResponseEntity<List<Connection>> getSecondDegreeConnections() {
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("Received request for second-degree connections for userId: {}", userId);

        List<Connection> connections = connectionService.findSecondDegreeConnections(userId);

        log.info("Returning {} second-degree connections for userId: {}", connections.size(), userId);
        return ResponseEntity.ok(connections);
    }

    @PostMapping("/{userId1}/connect/{userId2}")
    public ResponseEntity<Void> connectUsers(@PathVariable Long userId1, @PathVariable Long userId2) {
        connectionService.connectUsers(userId1, userId2);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-user")
    public ResponseEntity<Connection> createUser(@RequestBody Connection connection) {
        Connection created = connectionService.createUser(
                connection.getUserId(),
                connection.getName(),
                connection.getEmail()
        );
        return ResponseEntity.ok(created);
    }
}
