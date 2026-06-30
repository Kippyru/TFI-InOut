package com.tfi.inout.service;

import com.tfi.inout.dto.request.UserRequestDto;
import com.tfi.inout.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    void createUser(UserRequestDto userRequestDto);
    List<UserResponseDto> list();
    UserResponseDto listId(Long id);
    UserResponseDto getMe();
    UserResponseDto edit(Long id, UserRequestDto userRequestDto);
    void restore(Long id);
    void delete(Long id);
}
