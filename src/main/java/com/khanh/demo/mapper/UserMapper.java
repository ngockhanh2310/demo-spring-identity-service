package com.khanh.demo.mapper;

import com.khanh.demo.dto.request.UserCreationRequest;
import com.khanh.demo.dto.request.UserUpdateRequest;
import com.khanh.demo.dto.response.UserResponse;
import com.khanh.demo.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Mapping create User - ignore ID khi tạo mới
    User toUser(UserCreationRequest request);

    // Mapping update User - ignore ID và username khi update
    // Chỉ map các trường khác null trong request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    //@Mapping(source = "password", target = "password")
    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    // Mapping UserResponse
    //@Mapping(target = "password", ignore = true)
    UserResponse toUserResponse(User user);
}
