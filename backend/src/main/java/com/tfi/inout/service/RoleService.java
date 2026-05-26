package com.tfi.inout.service;

import com.tfi.inout.dto.RoleDto;
import com.tfi.inout.mapper.RoleMapper;
import com.tfi.inout.model.Role;
import com.tfi.inout.repository.RoleRepository;
import com.tfi.inout.handler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleMapper roleMapper;

    public void createRole(RoleDto roleDto) {
        Role role = roleMapper.toEntity(roleDto);
        roleRepository.save(role);
    }

    public List<RoleDto> list() {
        List<Role> role = roleRepository.findAll();
        return roleMapper.toList(role);
    }

    public RoleDto listId(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        return roleMapper.toDto(role);
    }

    public RoleDto edit(Long id, RoleDto roleDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        roleMapper.updateRole(roleDto, role);
        Role updateRole = roleRepository.save(role);
        return roleMapper.toDto(updateRole);
    }

    @Transactional
    public void restore(Long id) {
        roleRepository.restoreById(id);
    }

    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found");
        }
        roleRepository.deleteById(id);
    }
}