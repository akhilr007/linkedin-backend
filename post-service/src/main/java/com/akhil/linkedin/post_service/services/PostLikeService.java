package com.akhil.linkedin.post_service.services;

import com.akhil.linkedin.post_service.dtos.responses.PostLikeResponse;

public interface PostLikeService {

    PostLikeResponse likePost(Long postId, Long userId);

    void unlikePost(Long postId, Long userId);
}
