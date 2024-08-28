package com.example.molla.domain.user.dto;

import com.example.molla.domain.user.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;

    public UserResponseDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
