package com.tfi.inout.mapper;

import com.tfi.inout.dto.request.EmployeeRequestDto;
import com.tfi.inout.dto.response.EmployeeResponseDto;
import com.tfi.inout.model.Employee;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numberEmployee", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "dateEntry", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Employee toEntity(EmployeeRequestDto employeeRequestDto);

    @Mapping(target = "user", source = "user.id")
    EmployeeResponseDto toDto(Employee employee);

    List<EmployeeResponseDto> toList(List<Employee> employeeList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numberEmployee", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "dateEntry", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEmployee(EmployeeRequestDto employeeRequestDto, @MappingTarget Employee employee);
}
