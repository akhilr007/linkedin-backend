package com.akhil.linkedin.connection_service.repositories;

import com.akhil.linkedin.connection_service.entities.Connection;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends Neo4jRepository<Connection, Long> {

    Optional<Connection> findByUserId(Long userId);

    // Get first-degree connections
    @Query("MATCH (user:Connection {userId: $userId}) -[:CONNECTED_TO]-> (connection:Connection) " +
            "RETURN connection")
    List<Connection> findFirstDegreeConnections(@Param("userId") Long userId);

    // Get second-degree connections
    @Query("MATCH (user:Connection {userId: $userId}) -[:CONNECTED_TO]-> (:Connection)-[:CONNECTED_TO] ->(connection:Connection) " +
            "WHERE NOT (user)-[:CONNECTED_TO]->(connection) AND user <> connection " +
            "RETURN DISTINCT connection")
    List<Connection> findSecondDegreeConnections(@Param("userId") Long userId);


    @Query("MATCH (sender:Connection)-[r:REQUESTED_TO]->(receiver:Connection) " +
            "WHERE sender.userId=$senderId AND receiver.userId=$receiverId " +
            " RETURN COUNT(r) > 0")
    boolean connectionRequestExists(Long senderId, Long receiverId);

    @Query("MATCH (sender:Connection)-[r:REQUESTED_TO]-(receiver:Connection) " +
            "WHERE sender.userId=$senderId AND receiver.userId=$receiverId " +
            " RETURN COUNT(r) > 0")
    boolean alreadyConnected(Long senderId, Long receiverId);

    @Query("MATCH (sender:Connection), (receiver:Connection) " +
            "WHERE sender.userId=$senderId AND receiver.userId=$receiverId " +
            " CREATE (sender)-[:REQUESTED_TO]->(receiver)")
    void addConnectionRequest(Long senderId, Long receiverId);


    @Query("MATCH (sender:Connection)-[r:REQUESTED_TO]->(receiver:Connection) " +
            "WHERE sender.userId=$senderId AND receiver.userId=$receiverId " +
            "DELETE r " +
            "CREATE (sender)-[:CONNECTED_TO]->(receiver), (receiver)-[:CONNECTED_TO]->(sender)")
    void acceptConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (sender:Connection)-[r:REQUESTED_TO]->(receiver:Connection) " +
            "WHERE sender.userId=$senderId AND receiver.userId=$receiverId " +
            "DELETE r ")
    void rejectConnectionRequest(Long senderId, Long receiverId);

    @Query("MERGE (user:Connection {userId: $userId, name: $name, email: $email})")
    void createUserNode(Long userId, String name, String email);

}
