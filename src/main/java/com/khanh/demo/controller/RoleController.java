package com.khanh.demo.controller;

import com.khanh.demo.dto.request.ApiResponse;
import com.khanh.demo.dto.request.RoleRequest;
import com.khanh.demo.dto.response.RoleResponse;
import com.khanh.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor // Thay thế @Autowired
@Slf4j
public class RoleController {
    final RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) { // @RequestBody dùng để nhận dữ liệu từ client
        //ApiResponse apiResponse = new ApiResponse();
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> delete(@PathVariable String roleId) { // @PathVariable dùng để nhận dữ liệu từ client
        roleService.delete(roleId);
        return ApiResponse.<Void>builder().build();
    }
}
