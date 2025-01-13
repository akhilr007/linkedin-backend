package com.akhil.linkedin.post_service.controllers;

import com.akhil.linkedin.post_service.auth.UserContextHolder;
import com.akhil.linkedin.post_service.dtos.responses.PostLikeResponseDTO;
import com.akhil.linkedin.post_service.services.PostLikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/likes")
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<PostLikeResponseDTO> likePost(@PathVariable Long postId) {

        Long userId = UserContextHolder.getCurrentUserId();

        log.info("Received request to like post with ID: {} by user with ID: {}", postId, userId);
        PostLikeResponseDTO response = postLikeService.likePost(postId, userId);

        log.info("Post liked successfully with ID: {} by user with ID: {}", postId, userId);
        return ResponseEntity.ok(response);
    }
}
