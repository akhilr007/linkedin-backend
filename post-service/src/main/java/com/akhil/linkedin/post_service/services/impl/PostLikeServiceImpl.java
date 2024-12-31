package com.akhil.linkedin.post_service.services.impl;

import com.akhil.linkedin.post_service.dtos.responses.PostLikeResponse;
import com.akhil.linkedin.post_service.entitiies.PostLike;
import com.akhil.linkedin.post_service.exceptions.ResourceNotFoundException;
import com.akhil.linkedin.post_service.repositories.PostLikeRepository;
import com.akhil.linkedin.post_service.repositories.PostRepository;
import com.akhil.linkedin.post_service.services.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    @Override
    public PostLikeResponse likePost(Long postId, Long userId) {

        log.info("Starting to like post with ID: {} by user ID: {}", postId, userId);

        if(postRepository.findById(postId).isEmpty()) {
            log.error("Post not found with ID: {}", postId);
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        if(postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            log.error("Post already liked by user ID: {}", userId);
            throw new IllegalArgumentException("Post already liked by user");
        }

        PostLike postLike = PostLike.builder()
                .postId(postId)
                .userId(userId)
                .build();

        postLikeRepository.save(postLike);
        log.info("Post liked successfully with ID: {} by user ID: {}", postId, userId);

        return PostLikeResponse.builder()
                .success(true)
                .message("Post liked successfully")
                .build();
    }

    @Override
    public void unlikePost(Long postId, Long userId) {

    }
}
