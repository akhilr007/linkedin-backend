package com.akhil.linkedin.post_service.controllers;

import com.akhil.linkedin.post_service.dtos.responses.PostLikeResponse;
import com.akhil.linkedin.post_service.services.PostLikeService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<PostLikeResponse> likePost(@PathVariable Long postId, HttpServletRequest request) {
        log.info("Received request to like post with ID: {} by user with ID: {}", postId, 1L);
        PostLikeResponse response = postLikeService.likePost(postId, 1L);

        log.info("Post liked successfully with ID: {} by user with ID: {}", postId, 1L);
        return ResponseEntity.ok(response);
    }
}
