package com.akhil.linkedin.post_service.controllers;

import com.akhil.linkedin.post_service.dtos.responses.PostLikeResponseDTO;
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
@RequestMapping("/unlikes")
@RequiredArgsConstructor
@Slf4j
public class PostUnlikeController {

    private final PostLikeService postLikeService;

    @PostMapping("/{postId}")
    public ResponseEntity<PostLikeResponseDTO> unlikePost(@PathVariable Long postId, HttpServletRequest request) {
        log.info("Received request to unlike post with ID: {} by user with ID: {}", postId, 1L);
        PostLikeResponseDTO response = postLikeService.unlikePost(postId, 1L);

        log.info("Post unliked successfully with ID: {} by user with ID: {}", postId, 1L);
        return ResponseEntity.ok(response);
    }
}
