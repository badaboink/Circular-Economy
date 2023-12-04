package com.circular.Circular.economy.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Getter
@Setter
public class PostDTO implements Serializable {

    private String postTitle;
    private String address;
    private String postDescription;
    private String price;
    private String resourceTypeName;
    private MultipartFile imageFile;

    public PostDTO(String postTitle, String postDescription, String address, String price, String resourceTypeName, MultipartFile imageFile) {
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.address = address;
        this.price = price;
        this.resourceTypeName = resourceTypeName;
        this.imageFile = imageFile;
    }

}