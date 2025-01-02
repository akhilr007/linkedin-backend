package com.akhil.linkedin.user_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponseDTO {

    private Boolean success;
    private String message;
}