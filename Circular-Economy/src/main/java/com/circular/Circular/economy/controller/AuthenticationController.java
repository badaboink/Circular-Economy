package com.circular.Circular.economy.controller;

import com.circular.Circular.economy.authentication.AuthenticationResponse;
import com.circular.Circular.economy.authentication.AuthenticationService;
import com.circular.Circular.economy.dto.AuthenticationRequest;
import com.circular.Circular.economy.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

  private final AuthenticationService service;


  @CrossOrigin(origins = "http://localhost:3030/")
  @PostMapping("/register")
  public ResponseEntity<?> register(
      @RequestBody RegisterRequest request
  ) {
    try {
      AuthenticationResponse response = service.register(request);
      return ResponseEntity.ok(response);
    } catch (DataAccessException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Username already exists"));
    }
  }


  @CrossOrigin(origins = "http://localhost:3030/")
  @PostMapping("/login")
  public ResponseEntity<?> login(
      @RequestBody AuthenticationRequest request
  ) {
    try{
      AuthenticationResponse response = service.login(request);
      return ResponseEntity.ok(response);
    } catch (AuthenticationException e){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Password is incorrect"));
    }
  }
}