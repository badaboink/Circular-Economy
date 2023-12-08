package com.circular.Circular.economy.dto;

import com.circular.Circular.economy.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;

    public static UserDTO fromRegisterRequest(RegisterRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(request.getUsername());
        userDTO.setEmail(request.getEmail());
        userDTO.setPassword(request.getPassword());
        userDTO.setPhoneNumber(request.getPhoneNumber());
        userDTO.setRole(request.getRole());
        return userDTO;
    }
    public static UserDTO fromAuthenticationRequest(AuthenticationRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(request.getUsername());
        userDTO.setPassword(request.getPassword());
        return userDTO;
    }

    public UserDTO(long userId, String username, String email, String phoneNumber) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}