package com.circular.Circular.economy.webController;

import com.circular.Circular.economy.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.circular.Circular.economy.service.PostService;

import java.util.List;

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

    /*
    @GetMapping("/hello")
    ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
     */
    /*
    //controlleryje return visada turetu buti json formato ir kartu turetu sekti atitinkamas http status kodas pvz:
      public ResponseEntity<?> sayHello() {
return ResponseEntity.status(HttpStatus.OK).body(Map.of("success", "Username already exists"));
    }


     */


    @PostMapping
    public void createNewPost(@RequestBody Post post) { //we take a request body and map into a post
        postService.addNewPost(post);
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
