package com.tfi.inout.service;

import com.tfi.inout.dto.UserDto;
import com.tfi.inout.mapper.UserMapper;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.UserRepository;
import com.tfi.inout.handler.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public List<UserDto> list() {
        List<User> user = userRepository.findAll();
        return userMapper.userList(user);
    }

    public UserDto listId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    public UserDto edit(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userMapper.updateUser(userDto, user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User updateUser = userRepository.save(user);
        return userMapper.toDto(updateUser);
    }

    @Transactional
    public void restore(Long id) {
        userRepository.restoreById(id);
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setActive(false);
        user.setDeletedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}
