package com.tfi.inout.service;

import com.tfi.inout.dto.AttendanceStatusDto;
import com.tfi.inout.dto.response.EventAttendanceResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface EventAttendanceService {
    AttendanceStatusDto getAttendanceStatus(Long employeeId);
    EventAttendanceResponseDto registerAttendance(Long employeeId, String device);
    List<EventAttendanceResponseDto> list(LocalDate date);
    String determineEventType(Long employeeId, LocalDate date);
}