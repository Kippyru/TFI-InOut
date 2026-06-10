package com.tfi.inout.mapper;

import com.tfi.inout.dto.UserDto;
import com.tfi.inout.model.Role;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.RoleRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    protected RoleRepository roleRepository;

    @Mapping(target = "role", source = "role", qualifiedByName = "mapIdtoRole")
    public abstract User toEntity(UserDto  userDto);

    @Mapping(target = "role", source = "role.id")
    public abstract UserDto toDto(User user);

    @Named("mapIdtoRole")
    protected Role mapIdtoRole(Long role) {
        if (role == null) return null;
        return roleRepository.findById(role)
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", source = "role", qualifiedByName = "mapIdtoRole")
    public abstract void updateUser(
            UserDto userDto,
            @MappingTarget User user);

    public abstract List<UserDto> userList(List<User> users);
}

