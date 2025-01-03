package com.akhil.linkedin.post_service.services.impl;

import com.akhil.linkedin.post_service.dtos.requests.PostRequestDTO;
import com.akhil.linkedin.post_service.dtos.responses.PostResponseDTO;
import com.akhil.linkedin.post_service.entitiies.Post;
import com.akhil.linkedin.post_service.exceptions.ResourceNotFoundException;
import com.akhil.linkedin.post_service.repositories.PostRepository;
import com.akhil.linkedin.post_service.services.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    @Override
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO, Long userId) {
        log.info("Starting to create post with content: {}", postRequestDTO.getContent());

        Post post = modelMapper.map(postRequestDTO, Post.class);
        post.setContent(postRequestDTO.getContent().trim());
        post.setUserId(1L); //TODO - set the user ID from the request

        log.debug("Mapped PostRequestDTO to Post entity: {}", post);

        Post savedPost = postRepository.save(post);
        log.info("Post created successfully with ID: {}", savedPost.getId());

        PostResponseDTO responseDTO = modelMapper.map(savedPost, PostResponseDTO.class);
        log.debug("Mapped saved Post entity to PostResponseDTO: {}", responseDTO);

        log.info("Returning response DTO with ID: {}", responseDTO.getId());
        return responseDTO;
    }

    @Override
    public PostResponseDTO getPostById(Long postId) {
        log.info("Starting to get post with ID: {}", postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));
        log.debug("Found post: {}", post);

        PostResponseDTO responseDTO = modelMapper.map(post, PostResponseDTO.class);
        log.debug("Mapped Post entity to PostResponseDTO: {}", responseDTO);

        log.info("Returning response DTO with ID: {}", responseDTO.getId());
        return responseDTO;
    }

    @Override
    public List<PostResponseDTO> getAllPostsOfUser(Long userId) {
        log.info("Starting to get all posts of user with ID: {}", userId);

        List<Post> posts = postRepository.findByUserId(userId);
        log.debug("Found posts: {}", posts);

        List<PostResponseDTO> responseDTOs = posts.stream()
                .map(post -> modelMapper.map(post, PostResponseDTO.class))
                .toList();
        log.debug("Mapped Post entities to PostResponseDTOs: {}", responseDTOs);

        log.info("Returning response DTOs of user with ID: {}", userId);
        return responseDTOs;
    }
}