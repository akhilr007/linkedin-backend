package com.akhil.linkedin.post_service.controllers;

import com.akhil.linkedin.post_service.auth.UserContextHolder;
import com.akhil.linkedin.post_service.dtos.requests.PostRequestDTO;
import com.akhil.linkedin.post_service.dtos.responses.PostResponseDTO;
import com.akhil.linkedin.post_service.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostRequestDTO postRequestDTO) {
        log.info("Received request to create post with content: {}", postRequestDTO.getContent());

        Long userId = UserContextHolder.getCurrentUserId();
        log.info("User ID: {}", userId);

        PostResponseDTO responseDTO = postService.createPost(postRequestDTO, userId);

        log.info("Post created successfully with ID: {}", responseDTO.getId());
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{postId}")
    ResponseEntity<PostResponseDTO> getPost(@PathVariable Long postId) {

        log.info("Received request to get post with ID: {}", postId);
        PostResponseDTO responseDTO = postService.getPostById(postId);

        log.info("Returning post with ID: {}", postId);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/users/{userId}/all")
    ResponseEntity<List<PostResponseDTO>> getAllPostsOfUser(@PathVariable Long userId) {

        log.info("Received request to get all posts of user with ID: {}", userId);
        List<PostResponseDTO> responseDTOs = postService.getAllPostsOfUser(userId);

        log.info("Returning all posts of user with ID: {}", userId);
        return ResponseEntity.ok(responseDTOs);
    }
}