package com.tfi.inout.service;

import com.tfi.inout.dto.request.DetailScheduleRequestDto;
import com.tfi.inout.dto.response.DetailScheduleResponseDto;

import java.util.List;

public interface DetailScheduleService {
    List<DetailScheduleResponseDto> getDetailsBySchedule(Long scheduleId);
    DetailScheduleResponseDto addDetailToSchedule(Long scheduleId, DetailScheduleRequestDto detailDto);
    List<DetailScheduleResponseDto> createBulk(Long scheduleId, List<DetailScheduleRequestDto> dtos);
    DetailScheduleResponseDto update(Long id, DetailScheduleRequestDto dto);
    void delete(Long id);
}
