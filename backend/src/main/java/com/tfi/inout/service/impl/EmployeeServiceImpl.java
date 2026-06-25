package com.tfi.inout.service.impl;

import com.tfi.inout.dto.request.EmployeeRequestDto;
import com.tfi.inout.dto.response.EmployeeResponseDto;
import com.tfi.inout.handler.ResourceNotFoundException;
import com.tfi.inout.mapper.EmployeeMapper;
import com.tfi.inout.model.Employee;
import com.tfi.inout.model.Role;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.EmployeeRepository;
import com.tfi.inout.repository.RoleRepository;
import com.tfi.inout.repository.UserRepository;
import com.tfi.inout.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Genera el número de empleado (legajo) automáticamente para evitar duplicados
    private String generateEmployeeNumber() {
        Long max = employeeRepository.findMaxEmployeeNumber();
        if (max == null || max < 1000) {
            return "1000";
        }
        return String.valueOf(max + 1);
    }

    @Override
    @Transactional
    public EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequestDto) {
        Role role = roleRepository.findByName("EMPLOYEE")
                .orElseThrow(() -> new ResourceNotFoundException("Role EMPLOYEE not found"));

        String employeeNumber = generateEmployeeNumber();

        User user = new User();
        user.setUsername(employeeNumber);
        user.setPassword(passwordEncoder.encode(employeeRequestDto.getDni()));
        user.setRole(role);
        user = userRepository.save(user);

        Employee employee = employeeMapper.toEntity(employeeRequestDto);
        employee.setNumberEmployee(employeeNumber);
        employee.setUser(user);
        employee.setDateEntry(LocalDate.now());
        employee = employeeRepository.save(employee);

        return employeeMapper.toDto(employee);
    }

    @Override
    public List<EmployeeResponseDto> list() {
        return employeeMapper.toList(employeeRepository.findAll());
    }

    @Override
    public EmployeeResponseDto getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // username == numberEmployee (legajo) for employees
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Employee employee = employeeRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for current user"));
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeResponseDto listId(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeResponseDto edit(Long id, EmployeeRequestDto employeeRequestDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employeeMapper.updateEmployee(employeeRequestDto, employee);
        return employeeMapper.toDto(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void restore(Long id) {
        employeeRepository.restoreById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setActive(false);
        employee.setDeletedAt(LocalDateTime.now());
        employeeRepository.save(employee);
    }
}
