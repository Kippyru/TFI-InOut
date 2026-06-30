package com.tfi.inout.mapper;

import com.tfi.inout.dto.response.EventAttendanceResponseDto;
import com.tfi.inout.model.EventAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventAttendanceMapper {

    @Mapping(source = "employee", target = "employee.id")
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    EventAttendance toEntity(EventAttendanceResponseDto dto);

    @Mapping(source = "employee.id", target = "employee")
    EventAttendanceResponseDto toDto(EventAttendance eventAttendance);

    List<EventAttendanceResponseDto> toList(List<EventAttendance> attendances);
}
