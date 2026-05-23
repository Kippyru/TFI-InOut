package com.tfi.inout.service;

import com.tfi.inout.dto.ScheduleDto;
import com.tfi.inout.dto.DetailScheduleDto;
import com.tfi.inout.mapper.ScheduleMapper;
import com.tfi.inout.mapper.DetailScheduleMapper;
import com.tfi.inout.model.Schedule;
import com.tfi.inout.model.DetailSchedule;
import com.tfi.inout.repository.ScheduleRepository;
import com.tfi.inout.repository.DetailScheduleRepository;
import com.tfi.inout.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final DetailScheduleRepository detailScheduleRepository;
    private final ScheduleMapper scheduleMapper;
    private final DetailScheduleMapper detailScheduleMapper;

    @Transactional
    public ScheduleDto createSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = scheduleMapper.toEntity(scheduleDto);
        return scheduleMapper.toDto(scheduleRepository.save(schedule));
    }

    public List<ScheduleDto> listSchedules() {
        return scheduleRepository.findAll().stream().map(scheduleMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public DetailScheduleDto addDetailToSchedule(Long scheduleId, DetailScheduleDto detailDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found"));
        detailDto.setScheduleId(scheduleId);
        DetailSchedule detail = detailScheduleMapper.toEntity(detailDto);
        return detailScheduleMapper.toDto(detailScheduleRepository.save(detail));
    }
}
