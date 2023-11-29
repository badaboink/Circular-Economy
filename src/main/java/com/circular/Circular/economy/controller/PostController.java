package com.circular.Circular.economy.controller;

import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PostController {
    @Autowired
    private PostRepository postRepo;

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<Post>> getAllPosts(){
        try{
            List<Post> postList = new LinkedList<>();
            postList.addAll(postRepo.findAll());
            if(postList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/getPostByID/{id}")
    public ResponseEntity<Post> getPostByID(@PathVariable Long id){
        Optional<Post> postObj = postRepo.findById(id);
        if (postObj.isPresent()) {
            return new ResponseEntity<>(postObj.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/addPost")
    public ResponseEntity<Post> addPost(@RequestBody Post post){
        try {
            Post postObj = postRepo.save(post);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping("/deleteByID/{id}")
    public ResponseEntity<HttpStatus> deleteByID(@PathVariable Long id){
        try {
            postRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/updatePostByID/{id}")
    public ResponseEntity<Post> updatePostByID(@PathVariable Long id, @RequestBody Post post){
        try{
            Optional<Post> postData = postRepo.findById(id);
            if (postData.isPresent()) {
                Post updatedPostData = getPost(post, postData);

                Post postObj = postRepo.save(updatedPostData);
                return new ResponseEntity<>(postObj, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static Post getPost(Post post, Optional<Post> postData) {
        Post updatedPostData = postData.get();
        updatedPostData.setTitle(post.getTitle());
        updatedPostData.setDescription(post.getDescription());
        updatedPostData.setAddress(post.getAddress());
        updatedPostData.setUser(post.getUser());
        updatedPostData.setImage(post.getImage());
        updatedPostData.setLatitude(post.getLatitude());
        updatedPostData.setLongitude(post.getLongitude());
        updatedPostData.setPrice(post.getPrice());
        updatedPostData.setResourceType(post.getResourceType());
        return updatedPostData;
    }
    @DeleteMapping("/deleteAllPosts")
    public ResponseEntity<HttpStatus> deleteAllPosts(){
        try{
            postRepo.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
