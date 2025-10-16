package com.khanh.demo.service;

import com.khanh.demo.dto.request.PermissionRequest;
import com.khanh.demo.dto.response.PermissionResponse;
import com.khanh.demo.entity.Permission;
import com.khanh.demo.mapper.PermissionMapper;
import com.khanh.demo.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionService {
    final PermissionRepository permissionRepository;
    final PermissionMapper permissionMapper;

    // create permission
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    // get all permissions
    public List<PermissionResponse> getAll() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
        //return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).toList();
    }

    // delete permission
    public void delete(String permissionId) {
        permissionRepository.deleteById(permissionId);
        System.out.println("Permission deleted");
    }
}
