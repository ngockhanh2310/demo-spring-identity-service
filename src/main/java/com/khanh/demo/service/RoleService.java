package com.khanh.demo.service;

import com.khanh.demo.dto.request.RoleRequest;
import com.khanh.demo.dto.response.RoleResponse;
import com.khanh.demo.mapper.RoleMapper;
import com.khanh.demo.repository.PermissionRepository;
import com.khanh.demo.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleService {
    final RoleRepository roleRepository;
    final PermissionRepository permissionRepository;
    final RoleMapper roleMapper;

    // create role
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        var permissions = permissionRepository.findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    // get all roles
    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    // delete role
    public void delete(String roleId) {
        roleRepository.deleteById(roleId);
    }
}
