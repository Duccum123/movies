package com.example.phimmoi.mapper;

import com.example.phimmoi.dto.request.UserRequest;
import com.example.phimmoi.dto.response.UserResponse;
import com.example.phimmoi.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest userCreationRequest);
    @Mapping(source = "id", target = "id")
    UserResponse toUserResponse(User user);

}
