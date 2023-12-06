package com.circular.Circular.economy.authentication;

import com.circular.Circular.economy.entity.Role;
import com.circular.Circular.economy.jwt.JwtService;
import com.circular.Circular.economy.dto.AuthenticationRequest;
import com.circular.Circular.economy.dto.RegisterRequest;
import com.circular.Circular.economy.entity.User;
import com.circular.Circular.economy.dto.UserDTO;
import com.circular.Circular.economy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    UserDTO userDTO = UserDTO.fromRegisterRequest(request);

    var user = User.builder()
          .username(userDTO.getUsername())
          .email(userDTO.getEmail())
          .password(passwordEncoder.encode(userDTO.getPassword()))
            .role(userDTO.getRole() == null || userDTO.getRole().name().isEmpty() ? Role.USER : userDTO.getRole())
            .phoneNumber(userDTO.getPhoneNumber())
          .build();
    User savedUser = userRepository.save(user);
    var jwtToken = jwtService.generateToken(savedUser);

    return AuthenticationResponse.builder()
          .accessToken(jwtToken)
          .build();
  }

  public AuthenticationResponse login(AuthenticationRequest request) {
    UserDTO userDTO = UserDTO.fromAuthenticationRequest(request);

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
                userDTO.getUsername(),
                userDTO.getPassword()
        )
    );

    var user = userRepository.findByUsername(userDTO.getUsername())
            .orElseThrow();

    var jwtToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .build();
  }
}
