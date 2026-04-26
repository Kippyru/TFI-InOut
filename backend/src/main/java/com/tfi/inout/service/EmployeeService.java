package com.tfi.inout.service;

import com.tfi.inout.dto.EmployeeDto;
import com.tfi.inout.mapper.EmployeeMapper;
import com.tfi.inout.mapper.UserMapper;
import com.tfi.inout.model.Employee;
import com.tfi.inout.model.Role;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.EmployeeRepository;
import com.tfi.inout.repository.RoleRepository;
import com.tfi.inout.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Role role = roleRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Rol not found"));

        User user = new User();
        user.setUsername(employeeDto.getNumberEmployee());
        user.setPassword(employeeDto.getDni());
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
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employeeMapper.toDto(employee);
    }

    public EmployeeDto edit(Long id, EmployeeDto employeeDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeMapper.updateEmployee(employeeDto, employee);
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDto(updatedEmployee);
    }

    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(id);
    }
}
