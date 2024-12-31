package com.akhil.linkedin.post_service.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLikeResponse {

    private Boolean success = false;
    private String message;
}
