package com.circular.Circular.economy.service;


import com.circular.Circular.economy.dto.geocoding.Coordinates;
import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.jwt.JwtService;
import com.circular.Circular.economy.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

//@Component //tells that this class is Spring bean
@Service //the same as @Component annotation
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private GeocodingService geocodingService;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post addNewPost(Post post, String token) {
        String[] authorizationParts = token.split(" ");
        String tokenExtracted = authorizationParts.length > 1 ? authorizationParts[1] : null;
        String username = jwtService.extractUsername(tokenExtracted);
        User user = userService.getUserByUsername(username);
        post.setUser(user);

//        Coordinates coordinates = geocodingService.getCoordinatesFromAddress(post.getAddress());
//
//        if (coordinates != null) {
//            post.setLatitude(coordinates.getLatitude());
//            post.setLongitude(coordinates.getLongitude());
//        }


        return postRepository.save(post);
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    public void deletePost(Long postId) {
        boolean exists = postRepository.existsById(postId);
        if (!exists) {
            throw new IllegalStateException(
                    "post with id " + postId + " does not exists");
        }
        postRepository.deleteById(postId);

    }
    @Transactional //with this annotation entity post goes into managed state
    public void updatePost(Long postId, String title, String description) {
            Post post = postRepository.findById(postId)
                    .orElseThrow( ()->new IllegalStateException("Post with id " + postId +" does not exist" ));
            System.out.println("updatePost metode postId" + postId);
            if (title != null &&
                title.length()>0 && !Objects.equals(post.getTitle(),title)) {
                post.setTitle(title);
            }
            if (description != null &&
                description.length()>0 && !Objects.equals(post.getDescription(),description)) {
                post.setDescription(description);
            }/*
            if (price >= 0 && !Objects.equals(post.getPrice(),price)) {
                 post.setPrice(price);
            } else throw new NumberFormatException ("Price cannot be lower than zero"); */

    }
}
