package com.example.molla.domain.user.service;

import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.user.dto.UserLoginDTO;
import com.example.molla.domain.user.dto.UserResponseDTO;
import com.example.molla.domain.user.dto.UserSignUpDTO;
import com.example.molla.exception.BusinessException;
import com.example.molla.exception.ErrorCode;
import com.example.molla.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 1. 존재하는 회원인지 이메일로 검증
     * 2. 존재하는 회원이면 예외 발생
     * 3. 존재하지 않는 회원이면 저장 (회원가입)
     * @return user.id
     */
    @Transactional
    public Long signUp(UserSignUpDTO userSignUpDTO) {

        userRepository.findByEmail(userSignUpDTO.getEmail())
                .ifPresent(existingUser -> {
                    throw new BusinessException(ErrorCode.USER_ALREADY_EXIST);
                });

        String encryptedPassword = passwordEncoder.encode(userSignUpDTO.getPassword());

        User user = userSignUpDTO.toEntity(encryptedPassword);
        User signUpUser = userRepository.save(user);
        return signUpUser.getId();
    }

        /**
         * 1. 존재하는 회원인지 이메일로 검증
         * 2. 존재하는 회원이면 예외 발생
         * 3. 비밀번호가 일치하지 않으면 예외 발생
         * @return UserResponseDTO
         */
        public UserResponseDTO login(UserLoginDTO userLoginDTO) {

            User existUser = userRepository.findByEmail(userLoginDTO.getEmail())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            if (!passwordEncoder.matches(userLoginDTO.getPassword(), existUser.getPassword())) {
                throw new BusinessException(ErrorCode.LOGIN_INFO_NOT_MATCHED);
            }

            return UserResponseDTO.fromEntity(existUser);
        }
}
