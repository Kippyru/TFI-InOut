package com.tfi.inout.controller;

import com.tfi.inout.dto.RoleDto;
import com.tfi.inout.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<String> createRole(@RequestBody RoleDto roleDto) {
        roleService.createRole(roleDto);
        return ResponseEntity.ok("Role successfully created");
    }

    @GetMapping("/list")
    public List<RoleDto> listRoles() {
        return roleService.list();
    }

    @GetMapping("/list/{id}")
    public RoleDto getUsers(@PathVariable Long id) {
        return roleService.listId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRole(@PathVariable Long id,
                                                 @RequestBody RoleDto roleDto) {
        roleService.edit(id, roleDto);
        return ResponseEntity.ok("Role successfully updated");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.ok("Role successfully deleted");
    }
}
