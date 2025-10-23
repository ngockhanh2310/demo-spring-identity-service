package com.khanh.demo.service;

import com.khanh.demo.dto.request.RoleRequest;
import com.khanh.demo.dto.response.PermissionResponse;
import com.khanh.demo.dto.response.RoleResponse;
import com.khanh.demo.repository.PermissionRepository;
import com.khanh.demo.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@TestPropertySource("/test.properties")
public class RoleServiceTest {
    @Autowired
    private RoleService roleService;
    @MockitoBean
    private RoleRepository roleRepository;
    @MockitoBean
    private PermissionRepository permissionRepository;

    private RoleRequest roleRequest;
    private RoleResponse roleResponse;

    @BeforeEach
    void initData() {
        roleRequest = RoleRequest.builder()
                .name("USER")
                .description("User roles")
                .permissions(Set.of("READ", "WRITE"))
                .build();
        roleResponse = RoleResponse.builder()
                .name("USER")
                .description("User roles")
                .permissions(Set.of(
                        new PermissionResponse("READ", ""),
                        new PermissionResponse("", "")
                ))
                .build();
    }

    @Test
    void createRole_validRequest_success() {
        // GIVEN

    }
}
