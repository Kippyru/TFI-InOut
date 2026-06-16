package com.tfi.inout.mapper;

import com.tfi.inout.dto.AuditAttendanceDto;
import com.tfi.inout.model.AuditAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuditAttendanceMapper {
    @Mapping(source = "eventAttendanceId", target = "eventAttendance.id")
    @Mapping(source = "adminId", target = "admin.id")
    AuditAttendance toEntity(AuditAttendanceDto dto);

    @Mapping(source = "eventAttendance.id", target = "eventAttendanceId")
    @Mapping(source = "admin.id", target = "adminId")
    AuditAttendanceDto toDto(AuditAttendance entity);
}
