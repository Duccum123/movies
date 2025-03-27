package com.example.phimmoi.mapper;

import com.example.phimmoi.dto.request.UserRequest;
import com.example.phimmoi.dto.response.UserResponse;
import com.example.phimmoi.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserRequest userCreationRequest) {
        if ( userCreationRequest == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( userCreationRequest.getUsername() );
        user.password( userCreationRequest.getPassword() );
        user.email( userCreationRequest.getEmail() );
        user.role( userCreationRequest.getRole() );

        return user.build();
    }

    @Override
    public UserResponse toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse userResponse = new UserResponse();

        userResponse.setId( user.getId() );
        userResponse.setUsername( user.getUsername() );
        userResponse.setPassword( user.getPassword() );
        userResponse.setEmail( user.getEmail() );
        userResponse.setRole( user.getRole() );
        userResponse.setCreated_at( user.getCreated_at() );
        userResponse.setEnabled( user.isEnabled() );

        return userResponse;
    }
}
