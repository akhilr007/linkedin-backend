package com.akhil.linkedin.post_service.services;

import com.akhil.linkedin.post_service.dtos.requests.PostRequestDTO;
import com.akhil.linkedin.post_service.dtos.responses.PostResponseDTO;

import java.util.List;

public interface PostService {

    PostResponseDTO createPost(PostRequestDTO postRequestDTO, Long userId);

    PostResponseDTO getPostById(Long postId);

    List<PostResponseDTO> getAllPostsOfUser(Long userId);
}
