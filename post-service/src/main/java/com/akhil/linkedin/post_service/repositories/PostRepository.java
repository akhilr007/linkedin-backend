package com.akhil.linkedin.post_service.repositories;

import com.akhil.linkedin.post_service.entitiies.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}