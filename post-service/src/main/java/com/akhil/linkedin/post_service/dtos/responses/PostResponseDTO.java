package com.akhil.linkedin.post_service.dtos.responses;

import lombok.Data;

@Data
public class PostResponseDTO {

    private Long id;
    private String content;
    private Long userId;
    private String createdAt;
}
