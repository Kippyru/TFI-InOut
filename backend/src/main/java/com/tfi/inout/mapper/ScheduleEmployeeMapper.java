package com.tfi.inout.mapper;

import com.tfi.inout.dto.request.ScheduleEmployeeRequestDto;
import com.tfi.inout.dto.response.ScheduleEmployeeResponseDto;
import com.tfi.inout.model.ScheduleEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleEmployeeMapper {

    @Mapping(source = "employeeId", target = "employee.id")
    @Mapping(source = "scheduleId", target = "schedule.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    ScheduleEmployee toEntity(ScheduleEmployeeRequestDto dto);

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "schedule.id", target = "scheduleId")
    @Mapping(source = "schedule.name", target = "scheduleName")
    ScheduleEmployeeResponseDto toDto(ScheduleEmployee entity);
}
