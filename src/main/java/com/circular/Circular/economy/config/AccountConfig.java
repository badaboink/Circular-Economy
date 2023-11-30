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
            User user1 = new User(100001L,
                    "John",
                    "email@address.com",
                    "secretpassword",
                    "+37060066666");
            ResourceType resourceType = ResourceType.TEST;

            Post firstPost = new Post(10001L,
                    "Raudonos plytos",
                    "I sell 100 years old red bricks",
                    "http://path_to_picture",
                    120.00F,
                    "Freeway str. 120, Kaunas City",
                    19.0649070739746F,
                    73.1308670043945F,resourceType,user1);

            repository.saveAll(List.of(firstPost));
        };

    }



}
