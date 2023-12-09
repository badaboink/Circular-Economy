package com.circular.Circular.economy.service;

import com.circular.Circular.economy.dto.UserDTO;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.jwt.JwtService;
import com.circular.Circular.economy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;
import java.util.Optional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

    public UserDTO getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User userTemp = user.get();
            return new UserDTO(userTemp.getUserId(), userTemp.getUsername(), userTemp.getEmail(), userTemp.getPhoneNumber());
        } else {
            throw new NoSuchElementException("User not found");
        }
    }
    public UserDTO updateUser(Long id, User updatedUser) {
        try {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            existingUser.getUsername(),
                            updatedUser.getPassword()
                    )
            );
            boolean usernameUpdated = !Objects.equals(existingUser.getUsername(), updatedUser.getUsername());
            if(usernameUpdated)
            {
                existingUser.setUsername(updatedUser.getUsername());
            }
            String jwtToken = null;

            if (usernameUpdated) {
                jwtToken = jwtService.generateToken(savedUser);
            }

            return new UserDTO(savedUser.getUserId(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getPhoneNumber(), jwtToken);
        } catch (DataIntegrityViolationException | NoSuchElementException | BadCredentialsException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException("An error occurred while processing the request", e);
        }
    }
    public void deleteUser(Long id) {
        try {
            userRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException | NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while processing the request", e);
        }
    }
}
