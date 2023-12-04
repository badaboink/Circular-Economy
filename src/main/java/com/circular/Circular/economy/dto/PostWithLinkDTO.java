package com.circular.Circular.economy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostWithLinkDTO {

    private Long postId;
    private String title;
    private String description;
    private String resourceTypeName;
    private String address;
    private Float price;
    private String username;
    private Float latitude;
    private Float longitude;
    private String imageName;
    private String dropboxTemporaryLink;

}