package com.circular.Circular.economy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO implements Serializable {

    private Long postId;
    private String postTitle;
    private String postDescription;
    private String address;
    private Float price;
    private String resourceTypeName;
    private MultipartFile imageFile;
    private String resourceTypeDescription;
    private Float latitude;
    private Float longitude;
    private String imageName;
    private String dropboxTemporaryLink;
    private String username;
    private String phoneNumber;
    private String email;

    public PostDTO(String postTitle, String postDescription, String address, Float price, String resourceTypeName) {
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.address = address;
        this.price = price;
        this.resourceTypeName = resourceTypeName;
    }
    public PostDTO(String postTitle, String postDescription, String address, Float price, String resourceTypeName, MultipartFile imageFile) {
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.address = address;
        this.price = price;
        this.resourceTypeName = resourceTypeName;
        this.imageFile = imageFile;
    }
    public PostDTO(String postTitle, String postDescription, String address, Float price, String resourceTypeName, String imageName) {
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.address = address;
        this.price = price;
        this.resourceTypeName = resourceTypeName;
        this.imageName = imageName;
    }

    public PostDTO(Long postId, String title, String description, String resourceTypeName,
                   String address, Float price, String username, Float latitude, Float longitude, String imageName, String phoneNumber,
                   String email) {
        this.postId = postId;
        this.postTitle = title;
        this.postDescription = description;
        this.address = address;
        this.price = price;
        this.resourceTypeName = resourceTypeName;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageName = imageName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public PostDTO(Long postId, String title, String description, String resourceTypeName, String resourceTypeDescription, Float price,
                   String address, String username, Float latitude, Float longitude, String imageName) {
        this.postId = postId;
        this.postTitle = title;
        this.postDescription = description;
        this.address = address;
        this.price = price;
        this.resourceTypeDescription = resourceTypeDescription;
        this.resourceTypeName = resourceTypeName;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageName = imageName;
    }
}