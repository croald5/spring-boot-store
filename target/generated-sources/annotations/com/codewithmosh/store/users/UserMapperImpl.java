package com.codewithmosh.store.users;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-10-01T17:05:25+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String email = null;
        Role role = null;

        id = user.getId();
        name = user.getName();
        email = user.getEmail();
        role = user.getRole();

        UserDto userDto = new UserDto( id, name, email, role );

        return userDto;
    }

    @Override
    public User toEntity(RegisterUserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( userRequest.getName() );
        user.email( userRequest.getEmail() );
        user.password( userRequest.getPassword() );

        return user.build();
    }

    @Override
    public User toEntity(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.id( userDto.getId() );
        user.name( userDto.getName() );
        user.email( userDto.getEmail() );
        user.role( userDto.getRole() );

        return user.build();
    }

    @Override
    public void toUpdate(UpdateUserRequest userRequest, User user) {
        if ( userRequest == null ) {
            return;
        }

        user.setName( userRequest.getName() );
        user.setEmail( userRequest.getEmail() );
    }
}
