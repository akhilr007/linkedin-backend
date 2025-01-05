package com.akhil.linkedin.connection_service.services;

import com.akhil.linkedin.connection_service.entities.Connection;

import java.util.List;

public interface ConnectionService {

    List<Connection> findFirstDegreeConnections(Long userId);

    List<Connection> findSecondDegreeConnections(Long userId);

    void connectUsers(Long userId1, Long userId2);

    Connection createUser(Long userId, String name, String email);
}
