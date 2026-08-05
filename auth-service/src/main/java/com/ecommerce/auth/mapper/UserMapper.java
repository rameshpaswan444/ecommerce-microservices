package com.ecommerce.auth.mapper;

import com.ecommerce.auth.dto.request.RegisterRequest;
import com.ecommerce.auth.dto.response.UserResponse;
import com.ecommerce.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(RegisterRequest request);

    @Mapping(target = "role", source = "role.name")
    UserResponse toResponse(User user);

}
