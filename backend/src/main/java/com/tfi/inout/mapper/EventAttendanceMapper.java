package com.tfi.inout.mapper;

import com.tfi.inout.dto.EventAttendanceDto;
import com.tfi.inout.model.EventAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventAttendanceMapper {

    @Mapping(source = "employee", target = "employee.id")
    EventAttendance toEntity(EventAttendanceDto dto);

    @Mapping(source = "employee.id", target = "employee")
    EventAttendanceDto toDto(EventAttendance eventAttendance);


}
