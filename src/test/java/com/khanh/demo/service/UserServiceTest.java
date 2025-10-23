package com.khanh.demo.service;

import com.khanh.demo.dto.request.UserCreationRequest;
import com.khanh.demo.dto.request.UserUpdateRequest;
import com.khanh.demo.dto.response.UserResponse;
import com.khanh.demo.entity.User;
import com.khanh.demo.exception.AppException;
import com.khanh.demo.repository.RoleRepository;
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
import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource("/test.properties")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private RoleRepository roleRepository;
    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
    private UserUpdateRequest userUpdateRequest;
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
        userUpdateRequest = UserUpdateRequest.builder()
                .password("012345678")
                .firstName("Jack")
                .lastName("Sparrow")
                .dob(LocalDate.parse("2000-01-01"))
                .roles(List.of("ADMIN", "USER"))
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

    @Test
    @WithMockUser(username = "JohnWick", authorities = {"ROLE_ADMIN"})
    void updateUser_validRequest_success() {
        // GIVEN
        when(userRepository.findById(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);
        when(roleRepository.findAllById(any())).thenReturn(List.of());
        // WHEN
        var result = userService.updateUser("j1i4ba5na213ba", userUpdateRequest);
        // THEN

        // THEN
        Assertions.assertThat(result.getUsername()).isEqualTo("JohnWick");
        Assertions.assertThat(result.getFirstName()).isEqualTo("Jack");
        Assertions.assertThat(result.getLastName()).isEqualTo("Sparrow");
        Assertions.assertThat(result.getDob()).isEqualTo(LocalDate.of(2000, 1, 1));
    }

    @Test
    @WithMockUser(username = "JohnWick", authorities = {"ROLE_ADMIN"})
    void updateUser_userNotFound_error() {
        // GIVEN
        when(userRepository.findById(anyString())).thenReturn(Optional.empty());
        // WHEN
        var exception = assertThrows(RuntimeException.class, () -> userService.updateUser("j1i4ba5na213ba", userUpdateRequest));
        // THEN
        Assertions.assertThat(exception.getMessage()).isEqualTo("User not found");
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void deleteUser_validId_success() {
        // GIVEN
        String userId = "j1i4ba5na213ba";
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // WHEN
        userService.deleteUser(userId);

        // THEN
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    void deleteUser_userNotFound_fail() {
        // GIVEN
        String userId = "not-exist";
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // WHEN - THEN
        var exception = assertThrows(AppException.class, () -> userService.deleteUser(userId));

        Assertions.assertThat(exception.getErrorCode().getCode()).isEqualTo(1009);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).deleteById(anyString());
    }

}