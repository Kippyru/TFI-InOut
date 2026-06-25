package com.tfi.inout.service.impl;

import com.tfi.inout.dto.request.UserRequestDto;
import com.tfi.inout.dto.response.UserResponseDto;
import com.tfi.inout.handler.ResourceNotFoundException;
import com.tfi.inout.mapper.UserMapper;
import com.tfi.inout.model.Role;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.RoleRepository;
import com.tfi.inout.repository.UserRepository;
import com.tfi.inout.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void createUser(UserRequestDto userRequestDto) {
        Role role = roleRepository.findById(userRequestDto.getRole())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        User user = userMapper.toEntity(userRequestDto);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDto> list() {
        return userMapper.userList(userRepository.findAll());
    }

    @Override
    public UserResponseDto listId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto edit(Long id, UserRequestDto userRequestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.updateUser(userRequestDto, user);
        if (userRequestDto.getPassword() != null && !userRequestDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        }
        if (userRequestDto.getRole() != null) {
            Role role = roleRepository.findById(userRequestDto.getRole())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            user.setRole(role);
        }
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void restore(Long id) {
        userRepository.restoreById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(false);
        user.setDeletedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
