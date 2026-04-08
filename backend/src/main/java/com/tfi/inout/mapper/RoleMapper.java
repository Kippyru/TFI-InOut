package com.tfi.inout.mapper;

import com.tfi.inout.dto.RoleDto;
import com.tfi.inout.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(RoleDto roleDto);

    RoleDto toDto(Role role);

    List<RoleDto> toList(List<Role> roles);

    void updateRole(RoleDto roleDto, @MappingTarget Role role);
}
