package com.circular.Circular.economy.service;

import com.circular.Circular.economy.dto.PostDTO;
import com.circular.Circular.economy.dto.PostWithLinkDTO;
import com.circular.Circular.economy.dto.geocoding.Coordinates;
import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.entity.ResourceType;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.jwt.JwtService;
import com.circular.Circular.economy.repository.PostRepository;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.GetTemporaryLinkResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    private String ACCESS_TOKEN ="api_access_token";
    public Post addNewPost(PostDTO postDTO, String token) throws IOException, DbxException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("salvage").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        InputStream fileInputStream = postDTO.getImageFile().getInputStream();
        String dropboxFilePath = "/salvage/" + postDTO.getImageFile().getOriginalFilename();
        FileMetadata uploadedFileMetadata = client.files().uploadBuilder(dropboxFilePath).uploadAndFinish(fileInputStream);
        String dropboxImageUrl = uploadedFileMetadata.getPathDisplay();

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
            Float price = Float.parseFloat(postDTO.getPrice());
            post.setPrice(price);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid price format");
        }
        post.setUser(user);
        post.setImage(dropboxImageUrl);
        Coordinates coordinates = geocodingService.getCoordinatesFromAddress(post.getAddress());

        if (coordinates != null) {
            post.setLatitude(coordinates.getLatitude());
            post.setLongitude(coordinates.getLongitude());
        }

        return postRepository.save(post);
    }

    public List<PostWithLinkDTO> getPosts() throws DbxException {
        List<Post> posts = postRepository.findAll();
        List<PostWithLinkDTO> postsWithLinkDTO = new ArrayList<>();

        for (Post post : posts) {
            String originalFileName = post.getImage();
            post.setImage(originalFileName);

            PostWithLinkDTO postWithLinkDTO = new PostWithLinkDTO();
            if(post.getImage()!= null)
            {
                DbxRequestConfig config = DbxRequestConfig.newBuilder("salvage").build();
                DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
                GetTemporaryLinkResult temporaryLinkResult = client.files().getTemporaryLink(originalFileName);

                String dropboxTemporaryLink = temporaryLinkResult.getLink();

                postWithLinkDTO.setDropboxTemporaryLink(dropboxTemporaryLink);
            }
            postWithLinkDTO.setPostId(post.getPostId());
            postWithLinkDTO.setTitle(post.getTitle());
            postWithLinkDTO.setDescription(post.getDescription());
            postWithLinkDTO.setResourceTypeName(post.getResourceType().name());
            postWithLinkDTO.setAddress(post.getAddress());
            postWithLinkDTO.setPrice(post.getPrice());
            postWithLinkDTO.setUsername(post.getUser().getUsername());
            postWithLinkDTO.setLatitude(post.getLatitude());
            postWithLinkDTO.setLongitude(post.getLongitude());
            postWithLinkDTO.setImageName(post.getImage());

            postsWithLinkDTO.add(postWithLinkDTO);
        }

        return postsWithLinkDTO;
    }

    public Optional<Post> getPostById(Long postId) {
        return postRepository.findById(postId);
    }

    public boolean deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            /*throw new IllegalStateException(
                    "post with id " + postId + " does not exist");*/
            return false;

        }
        postRepository.deleteById(postId);
        return true;
    }

    @Transactional //with this annotation entity post goes into managed state
    public boolean updatePost(Long postId, String title, String description) {
            boolean changes = false;
            Post post = postRepository.findById(postId)
                    .orElseThrow( ()->new IllegalStateException("Post with id " + postId +" does not exist" ));
            System.out.println("updatePost method postId" + postId);
            if (title != null &&
                title.length()>0 && !Objects.equals(post.getTitle(),title)) {
                post.setTitle(title);
                changes=true;
            }
            if (description != null &&
                description.length()>0 && !Objects.equals(post.getDescription(),description)) {
                post.setDescription(description);
                changes=true;
            }/*
            if (price >= 0 && !Objects.equals(post.getPrice(),price)) {
                 post.setPrice(price);
            } else throw new NumberFormatException ("Price cannot be lower than zero"); */
        return changes;
    }


}
