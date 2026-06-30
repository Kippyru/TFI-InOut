package com.tfi.inout.mapper;

import com.tfi.inout.dto.response.AuditAttendanceResponseDto;
import com.tfi.inout.model.AuditAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditAttendanceMapper {

    @Mapping(source = "eventAttendanceId", target = "eventAttendance.id")
    @Mapping(source = "adminId", target = "admin.id")
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    AuditAttendance toEntity(AuditAttendanceResponseDto dto);

    @Mapping(source = "eventAttendance.id", target = "eventAttendanceId")
    @Mapping(source = "admin.id", target = "adminId")
    AuditAttendanceResponseDto toDto(AuditAttendance entity);
}
