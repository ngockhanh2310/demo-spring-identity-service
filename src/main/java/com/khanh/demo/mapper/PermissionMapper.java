package com.khanh.demo.mapper;

import com.khanh.demo.dto.request.PermissionRequest;
import com.khanh.demo.dto.response.PermissionResponse;
import com.khanh.demo.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
