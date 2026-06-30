package com.tfi.inout.mapper;

import com.tfi.inout.dto.request.UserRequestDto;
import com.tfi.inout.dto.response.UserResponseDto;
import com.tfi.inout.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    User toEntity(UserRequestDto userRequestDto);

    @Mapping(target = "role", source = "role.id")
    UserResponseDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateUser(UserRequestDto userRequestDto, @MappingTarget User user);

    List<UserResponseDto> userList(List<User> users);
}
