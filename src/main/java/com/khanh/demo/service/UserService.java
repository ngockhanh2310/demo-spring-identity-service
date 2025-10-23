package com.khanh.demo.service;

import com.khanh.demo.dto.request.UserCreationRequest;
import com.khanh.demo.dto.request.UserUpdateRequest;
import com.khanh.demo.dto.response.UserResponse;
import com.khanh.demo.entity.User;
import com.khanh.demo.enums.Role;
import com.khanh.demo.exception.AppException;
import com.khanh.demo.exception.ErrorCode;
import com.khanh.demo.mapper.UserMapper;
import com.khanh.demo.repository.RoleRepository;
import com.khanh.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    // Create user
    public UserResponse createUser(UserCreationRequest request) {
        log.info("Service: Create user");
        // check username exist
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXIST);
        }

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
        // user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Get all users
    //@PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponse> getAllUsers() {
        log.info("Get all users");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    // Get info user
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();//

        User user = userRepository.findByUsername(name).orElseThrow(
                () -> new AppException(ErrorCode.USERNAME_EXIST)
        );
        return userMapper.toUserResponse(user);
    }

    // Get user by ID
    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {
        log.info("Get user by ID: {}", userId);
        return userMapper.toUserResponse(userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    // Update user
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // find user by ID

        userMapper.updateUser(user, request); // perform mapping from request to user
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user)); // save to database
    }

    // Delete user by ID
    public void deleteUser(String userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.deleteById(userId);
    }
}
