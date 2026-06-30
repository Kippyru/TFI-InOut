package com.tfi.inout.service.impl;

import com.tfi.inout.dto.request.RoleRequestDto;
import com.tfi.inout.dto.response.RoleResponseDto;
import com.tfi.inout.handler.ResourceNotFoundException;
import com.tfi.inout.mapper.RoleMapper;
import com.tfi.inout.model.Role;
import com.tfi.inout.repository.RoleRepository;
import com.tfi.inout.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public void createRole(RoleRequestDto roleRequestDto) {
        Role role = roleMapper.toEntity(roleRequestDto);
        roleRepository.save(role);
    }

    @Override
    public List<RoleResponseDto> list() {
        return roleMapper.toList(roleRepository.findAll());
    }

    @Override
    public RoleResponseDto listId(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return roleMapper.toDto(role);
    }

    @Override
    public RoleResponseDto edit(Long id, RoleRequestDto roleRequestDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        roleMapper.updateRole(roleRequestDto, role);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void restore(Long id) {
        roleRepository.restoreById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        role.setActive(false);
        role.setDeletedAt(LocalDateTime.now());
        roleRepository.save(role);
    }
}
