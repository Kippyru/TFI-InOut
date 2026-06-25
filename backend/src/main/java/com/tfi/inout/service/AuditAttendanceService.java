package com.tfi.inout.service;

import com.tfi.inout.dto.DailyAttendanceDto;
import com.tfi.inout.dto.response.AuditAttendanceResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface AuditAttendanceService {
    AuditAttendanceResponseDto auditAttendance(Long adminId, Long eventAttendanceId, String reason, String newValue);
    List<DailyAttendanceDto> getDailyAttendance(LocalDate date);
}
