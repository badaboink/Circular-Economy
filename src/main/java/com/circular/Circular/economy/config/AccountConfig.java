package com.circular.Circular.economy.config;

import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.entity.ResourceType;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AccountConfig {
    @Bean
    CommandLineRunner commandLineRunner(PostRepository repository) {
        return args -> {

            User user1 = new User("John",
                    "email@address.com",
                    "secretpassword",
                    "+37060066666");
            /*System.out.println("user1 object " +user1);
            System.out.println(user1.getUserId());
            System.out.println(user1.getUsername());
            System.out.println(user1.getEmail());
            System.out.println(user1.getPassword());
            System.out.println(user1.getPhoneNumber()); */
            ResourceType resourceType = ResourceType.BRICKS;



            Post firstPost = new Post(
                    "Raudonos plytos",
                    "I sell 100 years old red bricks",
                    "http://path_to_picture",
                    120.00F,
                    "Freeway str. 120, Kaunas City",
                    19.0649070739746F,
                    73.1308670043945F,resourceType);
            Post secondPost = new Post(
                    "Red Bricks",
                    "I sell 200 years old red bricks",
                    "http://path_to_picture",
                    120.00F,
                    "Freeway str. 120, Kaunas City",
                    19.0649070739746F,
                    73.1308670043945F,resourceType);

            /*System.out.println("firstPost object "+firstPost);
            System.out.println(firstPost.getPostId());
            System.out.println(firstPost.getDescription());
            System.out.println(firstPost.getImage());
            System.out.println(firstPost.getPrice());
            System.out.println(firstPost.getAddress());
            System.out.println(firstPost.getLatitude());
            System.out.println(firstPost.getLongitude());
            System.out.println(firstPost.getResourceType());
            System.out.println(firstPost.getUser()); */
            repository.saveAll(List.of(firstPost,secondPost));


        };

    }



}
