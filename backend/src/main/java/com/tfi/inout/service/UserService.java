package com.tfi.inout.service;

import com.tfi.inout.dto.UserDto;
import com.tfi.inout.mapper.UserMapper;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    public void createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        userRepository.save(user);
    }

    public List<UserDto> list() {
        List<User> user = userRepository.findAll();
        return userMapper.userList(user);
    }

    public UserDto listId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toDto(user);
    }

    public UserDto edit(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userMapper.updateUser(userDto, user);
        User updateUser = userRepository.save(user);
        return userMapper.toDto(updateUser);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
