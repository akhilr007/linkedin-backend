package com.akhil.linkedin.post_service.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequestDTO {

    @NotBlank(message = "Content must not be blank")
    private String content;
}
