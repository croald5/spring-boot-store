package com.codewithmosh.store.users;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest userRequest);
    User toEntity(UserDto userDto);
    void toUpdate(UpdateUserRequest userRequest, @MappingTarget User user);
}
