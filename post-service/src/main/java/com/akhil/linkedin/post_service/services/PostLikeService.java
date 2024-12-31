package com.akhil.linkedin.post_service.services;

import com.akhil.linkedin.post_service.dtos.responses.PostLikeResponseDTO;

public interface PostLikeService {

    PostLikeResponseDTO likePost(Long postId, Long userId);

    void unlikePost(Long postId, Long userId);
}
