package com.circular.Circular.economy.service;

import com.circular.Circular.economy.dto.PostDTO;
import com.circular.Circular.economy.dto.geocoding.Coordinates;
import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.entity.ResourceType;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.jwt.JwtService;
import com.circular.Circular.economy.repository.PostRepository;
import com.dropbox.core.DbxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private DropboxService dropboxService;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post addNewPost(PostDTO postDTO, String token) throws IOException, DbxException {

        String[] authorizationParts = token.split(" ");
        String tokenExtracted = authorizationParts.length > 1 ? authorizationParts[1] : null;
        String username = jwtService.extractUsername(tokenExtracted);
        User user = userService.getUserByUsername(username);
        Post post = new Post();
        post.setTitle(postDTO.getPostTitle());
        post.setDescription(postDTO.getPostDescription());
        ResourceType resourceType = ResourceType.valueOf(postDTO.getResourceTypeName());
        post.setResourceType(resourceType);
        post.setAddress(postDTO.getAddress());
        try {
            Float price = postDTO.getPrice();
            post.setPrice(price);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format");
        }
        post.setUser(user);
        if (postDTO.getImageFile() != null) {
            try {
                InputStream fileInputStream = postDTO.getImageFile().getInputStream();
                String dropboxFilePath = "/salvage/" + postDTO.getImageFile().getOriginalFilename();
                String dropboxImageUrl = dropboxService.uploadFile(fileInputStream, dropboxFilePath);
                post.setImage(dropboxImageUrl);
            } catch (IOException | DbxException ignored) {
            }
        }
        Coordinates coordinates = geocodingService.getCoordinatesFromAddress(post.getAddress());

        if (coordinates != null) {
            post.setLatitude(coordinates.getLatitude());
            post.setLongitude(coordinates.getLongitude());
        }

        return postRepository.save(post);
    }

    public List<PostDTO> getPosts() throws DbxException {
        List<Post> posts = postRepository.findAll();
        List<PostDTO> postsWithLinkDTO = new ArrayList<>();

        for (Post post : posts) {
            String originalFileName = post.getImage();
            post.setImage(originalFileName);

            PostDTO postWithLinkDTO = new PostDTO(post.getPostId(), post.getTitle(), post.getDescription(), post.getResourceType().name(),
                    post.getAddress(), post.getPrice(), post.getUser().getUsername(), post.getLatitude(), post.getLongitude(),
                    post.getImage(), post.getUser().getPhoneNumber(), post.getUser().getEmail());
            try {
                if (post.getImage() != null) {
                    String dropboxTemporaryLink = dropboxService.getTemporaryLink(originalFileName);
                    postWithLinkDTO.setDropboxTemporaryLink(dropboxTemporaryLink);
                }
            } catch (DbxException e) {
            }

            postsWithLinkDTO.add(postWithLinkDTO);
        }

        return postsWithLinkDTO;
    }

    public List<PostDTO> getPersonalPosts(String token) throws DbxException {
        String[] authorizationParts = token.split(" ");
        String tokenExtracted = authorizationParts.length > 1 ? authorizationParts[1] : null;
        String username = jwtService.extractUsername(tokenExtracted);
        List<Post> posts = postRepository.findByUser_Username(username);
        List<PostDTO> postsWithLinkDTO = new ArrayList<>();

        for (Post post : posts) {
            String originalFileName = post.getImage();

            PostDTO postWithLinkDTO = new PostDTO(post.getPostId(), post.getTitle(), post.getDescription(), post.getResourceType().name(),
                    post.getResourceType().getDescription(), post.getPrice(), post.getAddress(), post.getUser().getUsername(),
                    post.getLatitude(), post.getLongitude(), post.getImage());
            try {
                if (originalFileName!= null) {
                    String dropboxTemporaryLink = dropboxService.getTemporaryLink(originalFileName);
                    postWithLinkDTO.setDropboxTemporaryLink(dropboxTemporaryLink);
                }
            } catch (DbxException ignored) {
            }
            postsWithLinkDTO.add(postWithLinkDTO);
        }
        return postsWithLinkDTO;
    }
    public Optional<PostDTO> getPostDTOById(Long postId) {
        Optional<Post> posts = postRepository.findById(postId);
        Post post = posts.get();
        PostDTO postWithLinkDTO = new PostDTO(post.getPostId(), post.getTitle(), post.getDescription(), post.getResourceType().name(),
                post.getResourceType().getDescription(), post.getPrice(), post.getAddress(), post.getUser().getUsername(),
                post.getLatitude(), post.getLongitude(), post.getImage());
        String originalFileName = post.getImage();
        try {
            if (originalFileName!= null) {
                String dropboxTemporaryLink = dropboxService.getTemporaryLink(originalFileName);
                postWithLinkDTO.setDropboxTemporaryLink(dropboxTemporaryLink);
            }
        } catch (DbxException e) {
        }
        return Optional.of(postWithLinkDTO);
    }
    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public boolean deletePost(Long postId, String token) {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isEmpty()) {
            return false;
        }

        Post post = optionalPost.get();
        String[] authorizationParts = token.split(" ");
        String tokenExtracted = authorizationParts.length > 1 ? authorizationParts[1] : null;
        String username = jwtService.extractUsername(tokenExtracted);

        if (!post.getUser().getUsername().equals(username)) {
            return false;
        }
        if(post.getImage() != null)
        {
            try {
                dropboxService.deleteFile(post.getImage());
            } catch (DbxException ignored) {
            }
        }

        postRepository.deleteById(postId);

        return true;
    }

    public boolean deletePosts(List<Long> postIds, String token) {
        boolean allDeleted = true;

        for (Long postId : postIds) {
            boolean deleted = deletePost(postId, token);
            if (!deleted) {
                allDeleted = false;
                break;
            }
        }
        return allDeleted;
    }

    @Transactional
    public Post updatePost(PostDTO postDTO) {
        Optional<Post> optionalPost = postRepository.findById(postDTO.getPostId());
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();

            if (postDTO.getImageFile() != null) {
                try {
                    if(post.getImage() != null)
                    {
                        dropboxService.deleteFile(post.getImage());
                    }
                    InputStream fileInputStream = postDTO.getImageFile().getInputStream();
                    String dropboxFilePath = "/salvage/" + postDTO.getImageFile().getOriginalFilename();
                    String dropboxImageUrl = dropboxService.uploadFile(fileInputStream, dropboxFilePath);
                    post.setImage(dropboxImageUrl);
                } catch (IOException | DbxException ignored) {
                }
            }
            post.setTitle(postDTO.getPostTitle());
            post.setDescription(postDTO.getPostDescription());
            if(!Objects.equals(post.getAddress(), postDTO.getAddress())){
                post.setAddress(postDTO.getAddress());
                Coordinates coordinates = geocodingService.getCoordinatesFromAddress(post.getAddress());

                if (coordinates != null) {
                    post.setLatitude(coordinates.getLatitude());
                    post.setLongitude(coordinates.getLongitude());
                }
            }
            post.setPrice(postDTO.getPrice());
            ResourceType resourceType = ResourceType.valueOf(postDTO.getResourceTypeName());
            post.setResourceType(resourceType);
            return post;
        }
        return null;
    }
    public List<Long> getPostIdsByUserId(Long userId) {
        List<Post> userPosts = postRepository.findByUserUserId(userId);
        return userPosts.stream()
                .map(Post::getPostId)
                .collect(Collectors.toList());
    }
}
