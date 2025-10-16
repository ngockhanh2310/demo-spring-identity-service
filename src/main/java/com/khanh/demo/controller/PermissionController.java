package com.khanh.demo.controller;

import com.khanh.demo.dto.request.ApiResponse;
import com.khanh.demo.dto.request.PermissionRequest;
import com.khanh.demo.dto.response.PermissionResponse;
import com.khanh.demo.repository.PermissionRepository;
import com.khanh.demo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor // Thay thế @Autowired
@Slf4j
public class PermissionController {
    final PermissionRepository permissionRepository;
    final PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) { // @RequestBody dùng để nhận dữ liệu từ client
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permissionId}")
    ApiResponse<Void> delete(@PathVariable String permissionId) { // @PathVariable dùng để nhận dữ liệu từ client
        permissionService.delete(permissionId);
        return ApiResponse.<Void>builder().build();
    }
}
