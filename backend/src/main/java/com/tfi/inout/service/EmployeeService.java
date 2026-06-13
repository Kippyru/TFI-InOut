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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

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

    //agregue esto, para generar el numero de empleado, osea legajo, automaticamente, asi se evitan duplicados y se ahorran problemas
    private String generateEmployeeNumber() {
        Long max = employeeRepository.findMaxEmployeeNumber();

        if (max == null || max < 1000) {
            return "1000";
        }

        return String.valueOf(max + 1);
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Role role = roleRepository.findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        String employeeNumber = generateEmployeeNumber();

        User user = new User();
        user.setUsername(employeeNumber);
        user.setPassword(passwordEncoder.encode(employeeDto.getDni()));
        user.setRole(role);
        user = userRepository.save(user);

        Employee employee = employeeMapper.toEntity(employeeDto);
        employee.setNumberEmployee(employeeNumber);
        employee.setUser(user);
        employee.setDateEntry(LocalDate.now());
        employee = employeeRepository.save(employee);

        return employeeMapper.toDto(employee);
    }

    public List<EmployeeDto> list() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toList(employees);
    }

    public EmployeeDto getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // username == numberEmployee (legajo) for employees
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Employee employee = employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for current user"));
        return employeeMapper.toDto(employee);
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
