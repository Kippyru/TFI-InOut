package com.tfi.inout.service;

import com.tfi.inout.dto.request.EmployeeRequestDto;
import com.tfi.inout.dto.response.EmployeeResponseDto;

import java.util.List;

public interface EmployeeService {
    EmployeeResponseDto createEmployee(EmployeeRequestDto employeeRequestDto);
    List<EmployeeResponseDto> list();
    EmployeeResponseDto getMe();
    EmployeeResponseDto listId(Long id);
    EmployeeResponseDto edit(Long id, EmployeeRequestDto employeeRequestDto);
    void restore(Long id);
    void delete(Long id);
}
