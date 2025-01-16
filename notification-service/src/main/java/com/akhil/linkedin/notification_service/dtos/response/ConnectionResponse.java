package com.akhil.linkedin.notification_service.dtos.response;

import lombok.Data;

import java.util.List;

@Data
public class ConnectionResponse {

    private String timestamp;
    private List<Connection> data;
    private String error;

    @Data
    public static class Connection {
        private Long id;
        private Long userId;
        private String name;
        private String email;
    }
}
