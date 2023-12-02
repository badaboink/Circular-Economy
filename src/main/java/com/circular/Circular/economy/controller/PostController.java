package com.circular.Circular.economy.controller;

import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.entity.ResourceType;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.jwt.JwtService;
import com.circular.Circular.economy.service.PostService;
import com.circular.Circular.economy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path="api/v1/post")
public class PostController {
    private final PostService postService;

    @Autowired //tells that variable postService has to be instantiated an injected into this constructor
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping //rest endpoint
    public List<Post> getPosts() {
        return postService.getPosts();
    }

    @PostMapping
    public ResponseEntity<?> createNewPost(@RequestBody Post post, @RequestHeader(name = "Authorization") String token) {
        Post savedPost = postService.addNewPost(post, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("post", savedPost));
    }

    @DeleteMapping(path = "{postId}")
    public void deletePost(@PathVariable("postId") Long postId) {
        postService.deletePost(postId);
    }

    @PutMapping(path = "{postId}")
    public void updatePost(
            @PathVariable("postId") Long postId,
            @RequestParam(required=false) String title,
            @RequestParam(required=false) String description
            ) {
        postService.updatePost(postId,title,description);
    }
    
}
