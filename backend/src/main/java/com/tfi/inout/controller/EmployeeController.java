package com.tfi.inout.controller;

import com.tfi.inout.dto.EmployeeDto;
import com.tfi.inout.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<String> createEmployee(@RequestBody EmployeeDto employeeDto) {
        employeeService.createEmployee(employeeDto);
        return ResponseEntity.ok("Employee successfully created");
    }

    @GetMapping("/list")
    public List<EmployeeDto> listEmployees() {
        return employeeService.list();
    }

    @GetMapping("/list/{id}")
    public EmployeeDto getEmployee(@PathVariable Long id) {
        return employeeService.listId(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id,
                                                 @Valid @RequestBody EmployeeDto employeeDto) {
        employeeService.edit(id, employeeDto);
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
