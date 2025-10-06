package com.khanh.demo.controller;

import com.khanh.demo.dto.request.ApiResponse;
import com.khanh.demo.dto.request.UserCreationRequest;
import com.khanh.demo.dto.request.UserUpdateRequest;
import com.khanh.demo.dto.response.UserResponse;
import com.khanh.demo.entity.User;
import com.khanh.demo.repository.UserRepository;
import com.khanh.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor // Thay thế @Autowired
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    // Create user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setResult(userService.createUser(request));
        apiResponse.setMessage("User created successfully");
        return apiResponse;
    }

    // Get all user
    @GetMapping
    List<User> getUsers() {
        return userService.getAllUsers();
    }

    // Get user by ID
    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId) {
        return userService.getUserResponse(userId);
    }

    // Update user
    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUserResponse(userId, request);
    }

    // Delete user by ID
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        if (userRepository.existsById(userId)) {
            userService.deleteUser(userId);
            return "User deleted successfully";
        }
        return "User not found";
    }
}
