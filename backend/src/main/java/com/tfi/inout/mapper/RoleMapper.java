package com.tfi.inout.mapper;

import com.tfi.inout.dto.request.RoleRequestDto;
import com.tfi.inout.dto.response.RoleResponseDto;
import com.tfi.inout.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Role toEntity(RoleRequestDto roleRequestDto);

    RoleResponseDto toDto(Role role);

    List<RoleResponseDto> toList(List<Role> roles);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateRole(RoleRequestDto roleRequestDto, @MappingTarget Role role);
}
