package com.example.molla.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {

    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @NotBlank
    private String password;
}
