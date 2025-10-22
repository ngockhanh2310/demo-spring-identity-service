package com.khanh.demo.service;

import com.khanh.demo.dto.request.UserCreationRequest;
import com.khanh.demo.dto.response.UserResponse;
import com.khanh.demo.entity.User;
import com.khanh.demo.exception.AppException;
import com.khanh.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2000, 1, 1);
        userCreationRequest = UserCreationRequest.builder()
                .username("JohnWick")
                .password("12345678")
                .firstName("John")
                .lastName("Wick")
                .dob(dob)
                .build();
        userResponse = UserResponse.builder()
                .id("j1i4ba5na213ba")
                .username("JohnWick")
                .firstName("John")
                .lastName("Wick")
                .dob(dob)
                .build();
        user = User.builder()
                .id("j1i4ba5na213ba")
                .username("JohnWick")
                .password("12345678")
                .firstName("John")
                .lastName("Wick")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        // WHEN
        UserResponse result = userService.createUser(userCreationRequest);
        // THEN
        Assertions.assertThat(result.getId()).isEqualTo("j1i4ba5na213ba");
        Assertions.assertThat(result.getUsername()).isEqualTo("JohnWick");
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));

        // THEN
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }

    @Test
    @WithMockUser(username = "JohnWick", authorities = {"ROLE_USER"})
    void getMyInfo_validRequest_success() {
        // GIVEN
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        var result = userService.getMyInfo();

        Assertions.assertThat(result.getId()).isEqualTo("j1i4ba5na213ba");
        Assertions.assertThat(result.getUsername()).isEqualTo("JohnWick");
    }

    @Test
    @WithMockUser(username = "JohnWick", authorities = {"ROLE_USER"})
    void getMyInfo_userNotFound_error() {
        // GIVEN
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.getMyInfo());
        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
        Assertions.assertThat(exception.getErrorCode().getMessage()).isEqualTo("User exists");
    }

    @Test
    @WithMockUser(username = "JohnWick", authorities = {"ROLE_ADMIN"})
    void getAllUser_validRequest_success() {
        // GIVEN
        when(userRepository.findAll()).thenReturn(List.of(user));
        // WHEN
        var result = userService.getAllUsers();
        // THEN
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.getFirst().getUsername()).isEqualTo("JohnWick");
    }

    @Test
    @WithMockUser(username = "JohnWick", authorities = {"ROLE_ADMIN"})
    void getUserById_validRequest_success() {
        // GIVEN
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        // WHEN
        var result = userService.getUser("j1i4ba5na213ba");
        Assertions.assertThat(result.getUsername()).isEqualTo("JohnWick");
    }

    @Test
    @WithMockUser(username = "JohnWick", authorities = {"ROLE_ADMIN"})
    void getUserById_userNotFound_error() {
        // GIVEN
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        // WHEN
        var exception = assertThrows(RuntimeException.class, () -> userService.getUser(anyString()));
        // THEN
        Assertions.assertThat(exception.getMessage()).isEqualTo("User not found");
    }
}