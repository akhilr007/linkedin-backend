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

    @PostMapping("/send/{receiverId}")
    ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("Received connection request from senderId: {} for receiverId: {}", senderId, receiverId);

        boolean response = connectionService.sendConnectionRequest(senderId, receiverId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/accept/{senderId}")
    ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("Received connection accept request from senderId: {} for receiverId: {}", senderId, receiverId);

        boolean response = connectionService.acceptConnectionRequest(senderId, receiverId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reject/{senderId}")
    ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("Received rejection connection request from senderId: {} for receiverId: {}", senderId, receiverId);

        boolean response = connectionService.rejectConnectionRequest(senderId, receiverId);

        return ResponseEntity.ok(response);
    }
}
