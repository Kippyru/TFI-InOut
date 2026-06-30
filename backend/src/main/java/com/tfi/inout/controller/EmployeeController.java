package com.tfi.inout.controller;

import com.tfi.inout.dto.request.EmployeeRequestDto;
import com.tfi.inout.service.EmployeeService;
import com.tfi.inout.dto.response.EmployeeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
        employeeService.createEmployee(employeeRequestDto);
        return ResponseEntity.ok("Employee successfully created");
    }

    @GetMapping("/me")
    public ResponseEntity<EmployeeResponseDto> getMe() {
        return ResponseEntity.ok(employeeService.getMe());
    }

    @GetMapping("/list")
    public List<EmployeeResponseDto> listEmployees() {
        return employeeService.list();
    }

    @GetMapping("/list/{id}")
    public EmployeeResponseDto getEmployee(@PathVariable Long id) {
        return employeeService.listId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id,
                                                  @Valid @RequestBody EmployeeRequestDto employeeRequestDto) {
        employeeService.edit(id, employeeRequestDto);
        return ResponseEntity.ok("Employee successfully updated");
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreEmployee(@PathVariable Long id) {
        employeeService.restore(id);
        return ResponseEntity.ok("Employee successfully restored");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.ok("Employee successfully deleted");
    }
}
