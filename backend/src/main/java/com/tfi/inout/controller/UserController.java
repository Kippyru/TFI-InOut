package com.tfi.inout.controller;

import com.tfi.inout.dto.EmployeeDto;
import com.tfi.inout.dto.UserDto;
import com.tfi.inout.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.ok("User successfully created");
    }

    @GetMapping("/list")
    public List<UserDto> listUsers() {
        return userService.list();
    }

    @GetMapping("/list/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.listId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id,
                                             @RequestBody UserDto userDto) {
        userService.edit(id, userDto);
        return ResponseEntity.ok("User successfully updated");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("User successfully deleted");
    }
}
