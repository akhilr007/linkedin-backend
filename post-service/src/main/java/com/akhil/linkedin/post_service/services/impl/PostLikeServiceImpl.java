package com.akhil.linkedin.post_service.services.impl;

import com.akhil.linkedin.post_service.dtos.responses.PostLikeResponseDTO;
import com.akhil.linkedin.post_service.entitiies.Post;
import com.akhil.linkedin.post_service.entitiies.PostLike;
import com.akhil.linkedin.post_service.events.PostLikedEvent;
import com.akhil.linkedin.post_service.exceptions.ResourceNotFoundException;
import com.akhil.linkedin.post_service.repositories.PostLikeRepository;
import com.akhil.linkedin.post_service.repositories.PostRepository;
import com.akhil.linkedin.post_service.services.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    @Override
    public PostLikeResponseDTO likePost(Long postId, Long userId) {

        log.info("Starting to like post with ID: {} by user ID: {}", postId, userId);

        Post post = postRepository.findById(postId).orElseThrow(() -> {
            log.error("Post not found with ID: {}", postId);
            return new ResourceNotFoundException("Post not found with id: " + postId);
        });

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

        PostLikedEvent postLikeEvent = PostLikedEvent.builder()
                .postId(postId)
                .creatorId(post.getUserId())
                .likedByUserId(userId)
                .build();
        // notification will be ordered for the post with ID: postId
        kafkaTemplate.send("post-liked-topic", postId, postLikeEvent);
        log.info("Post created event published to Kafka topic: post-liked-topic");

        return PostLikeResponseDTO.builder()
                .success(true)
                .message("Post liked successfully")
                .build();
    }

    @Override
    public PostLikeResponseDTO unlikePost(Long postId, Long userId) {
        log.info("Starting to unlike post with ID: {} by user ID: {}", postId, userId);

        if(postRepository.findById(postId).isEmpty()) {
            log.error("Post not found with ID: {}", postId);
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        PostLike postLike = postLikeRepository.findByPostIdAndUserId(postId, userId).orElseThrow(() -> {
                log.error("Post is not liked by the user ID: {}", userId);
                return new IllegalArgumentException("Post is not liked by the user with ID: " + userId);
        });

        postLikeRepository.delete(postLike);
        log.info("Post unliked successfully with ID: {} by user ID: {}", postId, userId);

        return PostLikeResponseDTO.builder()
                .success(true)
                .message("Post unliked successfully")
                .build();
    }
}
