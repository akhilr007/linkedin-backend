package com.akhil.linkedin.post_service.controllers;

import com.akhil.linkedin.post_service.dtos.requests.PostRequestDTO;
import com.akhil.linkedin.post_service.dtos.responses.PostResponseDTO;
import com.akhil.linkedin.post_service.services.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {

    private final PostService postService;

    @PostMapping
    ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostRequestDTO postRequestDTO,
                                               HttpServletRequest request) {

        log.info("Received request to create post with content: {}", postRequestDTO.getContent());
        PostResponseDTO responseDTO = postService.createPost(postRequestDTO, 1L);

        log.info("Post created successfully with ID: {}", responseDTO.getId());
        return ResponseEntity.ok(responseDTO);
    }
}