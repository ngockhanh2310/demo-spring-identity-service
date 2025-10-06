package com.khanh.demo.service;

import com.khanh.demo.dto.request.UserCreationRequest;
import com.khanh.demo.dto.request.UserUpdateRequest;
import com.khanh.demo.dto.response.UserResponse;
import com.khanh.demo.entity.User;
import com.khanh.demo.exception.AppException;
import com.khanh.demo.exception.ErrorCode;
import com.khanh.demo.mapper.UserMapper;
import com.khanh.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // Create user
    public User createUser(UserCreationRequest request) {
        // check username exist
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXIST);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public UserResponse getUserResponse(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    // Update user
    public UserResponse updateUserResponse(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // find user by ID
        userMapper.updateUser(user, request); // perform mapping from request to user
        return userMapper.toUserResponse(userRepository.save(user)); // save to database
    }

    // Delete user by ID
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
