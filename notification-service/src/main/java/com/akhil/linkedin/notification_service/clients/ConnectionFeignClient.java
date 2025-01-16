package com.akhil.linkedin.notification_service.clients;

import com.akhil.linkedin.notification_service.dtos.response.ConnectionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "connection-service", path = "/connections")
public interface ConnectionFeignClient {

    @GetMapping("/first-degree")
    ConnectionResponse getFirstDegreeConnections(@RequestHeader("X-User-Id") Long userId);
}
