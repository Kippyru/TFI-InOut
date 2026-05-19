package com.tfi.inout.mapper;

import com.tfi.inout.dto.ScheduleEmployeeDto;
import com.tfi.inout.model.ScheduleEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleEmployeeMapper {
    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    ScheduleEmployee toEntity(ScheduleEmployeeDto dto);

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "schedule.id", target = "scheduleId")
    ScheduleEmployeeDto toDto(ScheduleEmployee entity);
}
