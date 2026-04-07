package com.tfi.inout.mapper;

import com.tfi.inout.dto.EmployeeDto;
import com.tfi.inout.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeDto employeeDto);

    EmployeeDto toDto(Employee employee);

    List<EmployeeDto> toList(List<Employee> employeeList);

    void updateEmployee(EmployeeDto employeeDto, @MappingTarget Employee employee);
}
