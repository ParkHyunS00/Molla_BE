package com.example.molla.domain.user.controller;

import com.example.molla.common.StandardResponse;
import com.example.molla.domain.user.dto.UserLoginDTO;
import com.example.molla.domain.user.dto.UserResponseDTO;
import com.example.molla.domain.user.dto.UserSignUpDTO;
import com.example.molla.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<StandardResponse<UserResponseDTO>> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

        UserResponseDTO userResponseDTO = userService.login(userLoginDTO);
        return StandardResponse.ofOk(userResponseDTO);
    }

    @PostMapping("/signup")
    public ResponseEntity<StandardResponse<Long>> signup(@Valid @RequestBody UserSignUpDTO userSignUpDTO) {

        Long userId = userService.signUp(userSignUpDTO);
        return StandardResponse.of(userId, HttpStatus.CREATED);
    }
}
