package com.tfi.inout.mapper;

import com.tfi.inout.dto.EmployeeDto;
import com.tfi.inout.model.Employee;
import com.tfi.inout.model.User;
import com.tfi.inout.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class EmployeeMapper {
    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "user", source = "user", qualifiedByName = "mapIdToUser")
    public abstract Employee toEntity(EmployeeDto employeeDto);

    @Mapping(target = "user", source = "user.id")
    public abstract EmployeeDto toDto(Employee employee);

    @Named("mapIdToUser")
    protected User mapIdToUser(Long user) {
        if (user == null) return null;
        return userRepository.findById(user)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public abstract List<EmployeeDto> toList(List<Employee> employeeList);

    @Mapping(target = "user", source = "user", qualifiedByName = "mapIdToUser")
    public abstract void updateEmployee(EmployeeDto employeeDto, @MappingTarget Employee employee);
}
