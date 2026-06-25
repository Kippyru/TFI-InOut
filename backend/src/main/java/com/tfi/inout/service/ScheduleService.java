package com.tfi.inout.service;

import com.tfi.inout.dto.request.ScheduleRequestDto;
import com.tfi.inout.dto.response.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto);
    List<ScheduleResponseDto> listSchedules();
    ScheduleResponseDto listId(Long id);
    ScheduleResponseDto edit(Long id, ScheduleRequestDto scheduleRequestDto);
    void restore(Long id);
    void delete(Long id);
}
