package com.circular.Circular.economy.controller;

import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers(){
        try{
            List<User> userList = new LinkedList<>();
            userList.addAll(userRepository.findAll());
            if(userList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/getUserByID/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable Long id){
        Optional<User> userObj = userRepository.findById(id);
        if (userObj.isPresent()) {
            return new ResponseEntity<>(userObj.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user){
        try {
            User userObj = userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping("/deleteByID/{id}")
    public ResponseEntity<HttpStatus> deleteByID(@PathVariable Long id){
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/updateUserByID/{id}")
    public ResponseEntity<User> updateUserByID(@PathVariable Long id, @RequestBody User user){
        try{
            Optional<User> userData = userRepository.findById(id);
            if (userData.isPresent()) {
                User updatedPostData = getUser(user, userData);

                User userObj = userRepository.save(updatedPostData);
                return new ResponseEntity<>(userObj, HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static User getUser(User user, Optional<User> userData) {
        User updatedUserData = userData.get();
        updatedUserData.setEmail(user.getEmail());
        updatedUserData.setPassword(user.getPassword());
        updatedUserData.setPhoneNumber(user.getPhoneNumber());
        updatedUserData.setUsername(user.getUsername());
        return updatedUserData;
    }
    @DeleteMapping("/deleteAllUsers")
    public ResponseEntity<HttpStatus> deleteAllUsers(){
        try{
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
