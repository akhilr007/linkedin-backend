package com.akhil.linkedin.post_service.services;

import com.akhil.linkedin.post_service.dtos.requests.PostRequestDTO;
import com.akhil.linkedin.post_service.dtos.responses.PostResponseDTO;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postRequestDTO, Long userId);
}
