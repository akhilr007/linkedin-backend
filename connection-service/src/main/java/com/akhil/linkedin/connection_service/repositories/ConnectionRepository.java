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

    // Create connection between two users - optimized query
    @Query("""
            MATCH (a:Connection {userId: $userId1})
            WITH a
            MATCH (b:Connection {userId: $userId2})
            WHERE a <> b
            MERGE (a)-[:CONNECTED_TO]->(b)
            """)
    void createConnection(@Param("userId1") Long userId1, @Param("userId2") Long userId2);

    // Check if two users are connected
    @Query("RETURN EXISTS( " +
            "(:Connection {userId: $userId1})-[:CONNECTED_TO]->(:Connection {userId: $userId2}) " +
            ")")
    boolean areUsersConnected(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
