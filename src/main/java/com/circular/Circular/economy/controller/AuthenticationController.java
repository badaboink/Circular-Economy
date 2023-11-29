package com.circular.Circular.economy.controller;

import com.circular.Circular.economy.authentication.AuthenticationResponse;
import com.circular.Circular.economy.authentication.AuthenticationService;
import com.circular.Circular.economy.request.AuthenticationRequest;
import com.circular.Circular.economy.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

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