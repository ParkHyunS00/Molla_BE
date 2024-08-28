package com.example.molla.domain.user.dto;

import com.example.molla.domain.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter // 테스트 위해 열어둠
public class UserSignUpDTO {

    @NotBlank
    private String username;

    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank
    private String password;

    public User toEntity(String encryptedPassword) {
        return User.builder()
                .username(this.username)
                .email(this.email)
                .password(encryptedPassword)
                .build();
    }
}
