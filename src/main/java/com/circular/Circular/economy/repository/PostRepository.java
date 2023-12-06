package com.circular.Circular.economy.repository;

import com.circular.Circular.economy.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser_Username(String username);
    List<Post> findByUserUserId(Long userId);
}
