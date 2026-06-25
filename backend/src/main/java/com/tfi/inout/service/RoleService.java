package com.tfi.inout.service;

import com.tfi.inout.dto.request.RoleRequestDto;
import com.tfi.inout.dto.response.RoleResponseDto;

import java.util.List;

public interface RoleService {
    void createRole(RoleRequestDto roleRequestDto);
    List<RoleResponseDto> list();
    RoleResponseDto listId(Long id);
    RoleResponseDto edit(Long id, RoleRequestDto roleRequestDto);
    void restore(Long id);
    void delete(Long id);
}