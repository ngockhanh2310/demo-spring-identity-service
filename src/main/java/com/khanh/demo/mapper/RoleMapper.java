package com.khanh.demo.mapper;

import com.khanh.demo.dto.request.RoleRequest;
import com.khanh.demo.dto.response.RoleResponse;
import com.khanh.demo.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
