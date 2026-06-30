package com.tfi.inout.controller;

import com.tfi.inout.dto.request.RoleRequestDto;
import com.tfi.inout.dto.response.RoleResponseDto;
import com.tfi.inout.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<String> createRole(@RequestBody RoleRequestDto roleDto) {
        roleService.createRole(roleDto);
        return ResponseEntity.ok("Role successfully created");
    }

    @GetMapping("/list")
    public List<RoleResponseDto> listRoles() {
        return roleService.list();
    }

    @GetMapping("/list/{id}")
    public RoleResponseDto getRole(@PathVariable Long id) {
        return roleService.listId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRole(@PathVariable Long id,
                                                  @Valid @RequestBody RoleRequestDto roleRequestDto) {
        roleService.edit(id, roleRequestDto);
        return ResponseEntity.ok("Role successfully updated");
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreRole(@PathVariable Long id) {
        roleService.restore(id);
        return ResponseEntity.ok("Role successfully restored");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.ok("Role successfully deleted");
    }
}
