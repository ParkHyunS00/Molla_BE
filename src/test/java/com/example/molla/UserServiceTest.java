package com.example.molla;

import com.example.molla.domain.user.domain.User;
import com.example.molla.domain.user.dto.UserLoginDTO;
import com.example.molla.domain.user.dto.UserResponseDTO;
import com.example.molla.domain.user.dto.UserSignUpDTO;
import com.example.molla.exception.BusinessException;
import com.example.molla.exception.ErrorCode;
import com.example.molla.domain.user.repository.UserRepository;
import com.example.molla.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("사용자 회원가입 테스트 - 존재하는 회원인지 검증")
    public void testFindUserByEmail() {
        // given
        UserSignUpDTO user1 = new UserSignUpDTO();
        user1.setEmail("test@test.com");
        user1.setPassword("123456");
        user1.setUsername("test1");
        userService.signUp(user1);

        // when & then
        UserSignUpDTO user2 = new UserSignUpDTO();
        user2.setEmail("test@test.com");
        user2.setPassword("123456");
        user2.setUsername("test2");
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> userService.signUp(user2));

        Assertions.assertEquals(ErrorCode.USER_ALREADY_EXIST, exception.getErrorCode());
    }

    @Test
    @DisplayName("사용자 로그인 테스트 - 로그인 성공")
    public void testLoginUser() {
        // Given
        UserLoginDTO user1 = new UserLoginDTO();
        user1.setEmail("test@test.com");
        user1.setPassword("password");
        String encryptedPassword = passwordEncoder.encode("password");

        User user = new User("test", user1.getEmail(), encryptedPassword);
        userRepository.save(user);

        UserLoginDTO user2 = new UserLoginDTO();
        user2.setEmail("test@test.com");
        user2.setPassword("password");

        // When
        UserResponseDTO responseDTO = userService.login(user2);

        // Then
        Assertions.assertNotNull(responseDTO);
        Assertions.assertEquals(user1.getEmail(), responseDTO.getEmail());
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시도")
    public void testLoginUserNotExistEmail() {
        // Given
        String email = "test@test.com";
        UserLoginDTO user1 = new UserLoginDTO();
        user1.setEmail(email);
        user1.setPassword("password");

        // When & Then
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            userService.login(user1);
        });
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("로그인 시도 시 비밀번호 일치하지 않을 때")
    public void testLoginUserNotMatchPassword() {
        // Given
        UserLoginDTO user1 = new UserLoginDTO();
        user1.setEmail("test@test.com");
        user1.setPassword("123456");
        String encryptedPassword = passwordEncoder.encode(user1.getPassword());

        User user = User.builder().username("test").email(user1.getEmail()).password(encryptedPassword).build();
        userRepository.save(user);

        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setEmail("test@test.com");
        userLoginDTO.setPassword("12345");

        // When & Then
        BusinessException exception = Assertions.assertThrows(BusinessException.class, () -> {
            userService.login(userLoginDTO);
        });
        Assertions.assertEquals(ErrorCode.LOGIN_INFO_NOT_MATCHED, exception.getErrorCode());
    }
}
