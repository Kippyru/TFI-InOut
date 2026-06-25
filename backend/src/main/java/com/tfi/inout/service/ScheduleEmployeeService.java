package com.tfi.inout.service;

import com.tfi.inout.dto.request.ScheduleEmployeeRequestDto;
import com.tfi.inout.dto.response.ScheduleEmployeeResponseDto;

import java.util.List;
import java.util.Optional;

public interface ScheduleEmployeeService {
    ScheduleEmployeeResponseDto assignSchedule(ScheduleEmployeeRequestDto dto);
    Optional<ScheduleEmployeeResponseDto> getActiveScheduleForEmployee(Long employeeId);
    List<ScheduleEmployeeResponseDto> getAllSchedulesForEmployee(Long employeeId);
}
