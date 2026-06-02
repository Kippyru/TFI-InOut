package com.tfi.inout.service;

import com.tfi.inout.dto.EmployeeDto;
import com.tfi.inout.handler.ResourceNotFoundException;
import com.tfi.inout.mapper.EmployeeMapper;
import com.tfi.inout.model.Employee;
import com.tfi.inout.model.Role;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.EmployeeRepository;
import com.tfi.inout.repository.RoleRepository;
import com.tfi.inout.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Role role = roleRepository.findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("Rol not found"));

        User user = new User();
        user.setUsername(employeeDto.getNumberEmployee());
        user.setPassword(passwordEncoder.encode(employeeDto.getDni()));
        user.setRole(role);
        user.setState("Activo");

        user = userRepository.save(user);

        Employee employee = employeeMapper.toEntity(employeeDto);
        employee.setUser(user);
        employee = employeeRepository.save(employee);

        return employeeMapper.toDto(employee);
    }

    public List<EmployeeDto> list() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toList(employees);
    }

    public EmployeeDto listId(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return employeeMapper.toDto(employee);
    }

    public EmployeeDto edit(Long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employeeMapper.updateEmployee(employeeDto, employee);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(updatedEmployee);
    }

    @Transactional
    public void restore(Long id) {
        employeeRepository.restoreById(id);
    }

    @Transactional
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employee.setActive(false);
        employee.setDeletedAt(LocalDateTime.now());

        employeeRepository.save(employee);
    }
}
