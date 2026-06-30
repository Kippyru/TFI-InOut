package com.tfi.inout.controller;


import com.tfi.inout.dto.request.UserRequestDto;
import com.tfi.inout.service.UserService;
import com.tfi.inout.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        userService.createUser(userRequestDto);
        return ResponseEntity.ok("User successfully created");
    }

    @GetMapping("/list")
    public List<UserResponseDto> listUsers() {
        return userService.list();
    }

    @GetMapping("/list/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        return userService.listId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @Valid @RequestBody UserRequestDto userRequestDto) {
        userService.edit(id, userRequestDto);
        return ResponseEntity.ok("User successfully updated");
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreUser(@PathVariable Long id) {
        userService.restore(id);
        return ResponseEntity.ok("User successfully restored");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("User successfully deleted");
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }

}
