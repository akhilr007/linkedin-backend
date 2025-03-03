package com.akhil.linkedin.connection_service.entities;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Connection {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "userId")
    private Long userId;

    private String name;

    private String email;
}
