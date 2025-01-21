package com.akhil.linkedin.connection_service.services;

import com.akhil.linkedin.connection_service.entities.Connection;

import java.util.List;

public interface ConnectionService {

    List<Connection> findFirstDegreeConnections(Long userId);

    List<Connection> findSecondDegreeConnections(Long userId);

    boolean sendConnectionRequest(Long senderId, Long receiverId);

    boolean acceptConnectionRequest(Long senderId, Long receiverId);

    boolean rejectConnectionRequest(Long senderId, Long receiverId);
}
