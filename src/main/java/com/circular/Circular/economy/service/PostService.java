package com.circular.Circular.economy.service;


import com.circular.Circular.economy.entity.Post;
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
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void addNewPost(Post post) {

    postRepository.save(post);
    System.out.println(post);

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
    public void updatePost(Long postId, String title, String description, float price) {
            Post post = postRepository.findById(postId)
                    .orElseThrow( ()->new IllegalStateException("Post with id " + postId +" does not exist" ));

            if (title != null &&
                title.length()>0 && !Objects.equals(post.getTitle(),title)) {
                post.setTitle(title);
            }
            if (description != null &&
                description.length()>0 && !Objects.equals(post.getDescription(),description)) {
                post.setDescription(description);
            }
            if (price >= 0 && !Objects.equals(post.getPrice(),price)) {
                 post.setPrice(price);
            } else throw new NumberFormatException ("Price cannot be lover than zero");

    }
}
